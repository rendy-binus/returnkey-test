package com.example.returnkeytest.service;

import com.example.returnkeytest.config.ApplicationConfig;
import com.example.returnkeytest.exception.ErrorCode;
import com.example.returnkeytest.model.OrderRecord;
import com.example.returnkeytest.model.ReturnStatus;
import com.example.returnkeytest.model.dto.ItemDto;
import com.example.returnkeytest.model.dto.OrderReturnDto;
import com.example.returnkeytest.model.dto.createreturn.CreateReturnRequest;
import com.example.returnkeytest.model.dto.createreturn.CreateReturnResponse;
import com.example.returnkeytest.model.dto.updateqcstatus.UpdateQCStatusRequest;
import com.example.returnkeytest.model.entity.OrderReturn;
import com.example.returnkeytest.model.entity.PendingReturn;
import com.example.returnkeytest.model.entity.RefundItem;
import com.example.returnkeytest.model.mapper.OrderReturnMapper;
import com.example.returnkeytest.repository.OrderReturnRepository;
import com.example.returnkeytest.repository.PendingReturnRepository;
import com.example.returnkeytest.repository.RefundItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static java.time.OffsetDateTime.now;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReturnService {
    private final ApplicationConfig applicationConfig;

    private final PendingReturnRepository pendingReturnRepository;
    private final OrderReturnRepository orderReturnRepository;
    private final RefundItemRepository refundItemRepository;

    private final OrderReturnMapper orderReturnMapper;

    public CreateReturnResponse createReturn(CreateReturnRequest request) {
        PendingReturn pendingReturn = getPendingReturn(request.getToken());

        List<ItemDto> items = createOrderReturn(pendingReturn, request.getReturnItems());

        List<CreateReturnResponse.ValidItem> validItems = new ArrayList<>();
        List<CreateReturnResponse.InvalidItem> invalidItems = new ArrayList<>();

        items.forEach(item -> {
            if (item instanceof CreateReturnResponse.ValidItem) {
                validItems.add((CreateReturnResponse.ValidItem) item);
            }

            if (item instanceof CreateReturnResponse.InvalidItem) {
                invalidItems.add((CreateReturnResponse.InvalidItem) item);
            }
        });

        BigDecimal totalRefundAmount = validItems.stream()
                .map(item -> item.getPricePerUnit().multiply(BigDecimal.valueOf(item.getQuantityToReturn())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CreateReturnResponse.builder()
                .id(validItems.size() > 0 ? orderReturnRepository.findFirstByToken(pendingReturn.getToken()).getId() : null)
                .totalRefundAmount(totalRefundAmount.equals(BigDecimal.ZERO) ? null : totalRefundAmount)
                .validItems(validItems)
                .invalidItems(invalidItems)
                .build();
    }

    @Transactional
    public PendingReturn getPendingReturn(String token) {
        PendingReturn pendingReturn = pendingReturnRepository.findFirstByTokenAndValidIsTrue(token)
                .orElseThrow(ErrorCode.RETURN_ERR_01::exception);

        if (pendingReturn.getCreatedDate().isBefore(now().minus(applicationConfig.getToken().getTimeToLive()))) {
            pendingReturn.setValid(false);
            pendingReturnRepository.save(pendingReturn);

            throw ErrorCode.RETURN_ERR_02.exception();
        }

        return pendingReturn;
    }

    @Transactional
    public List<ItemDto> createOrderReturn(PendingReturn pendingReturn, Set<CreateReturnRequest.ReturnItem> returnItems) {
        List<ItemDto> items = new ArrayList<>();

        OrderReturn orderReturn = OrderReturn.builder()
                .token(pendingReturn.getToken())
                .orderId(pendingReturn.getOrderId())
                .status(ReturnStatus.AWAITING_APPROVAL)
                .build();

        returnItems.forEach(returnItem -> {
            Optional<OrderRecord> orderRecordOptional = pendingReturn.getOrderRecords().stream()
                    .filter(or -> or.getSku().equalsIgnoreCase(returnItem.getSku()))
                    .findFirst();

            // if the order record is present then, it is a valid return item
            // add it to Refund Item table and as valid item response
            if (orderRecordOptional.isPresent()) {
                OrderRecord orderRecord = orderRecordOptional.get();

                AtomicInteger availableQuantity = new AtomicInteger(orderRecord.getQuantity());

                // find all requested refund items by SKU and Order ID
                List<RefundItem> refundItems = refundItemRepository.findAllBySkuAndOrderId(orderRecord.getSku(), orderReturn.getOrderId());
                // decrease available quantity for each refund item found
                refundItems.forEach(refundItem -> availableQuantity.addAndGet(-refundItem.getQuantity()));

                // the requested return item quantity must not be greater than the available quantity
                // otherwise add it as invalid item and add quantity details to the object
                if (availableQuantity.get() >= returnItem.getQuantity()) {
                    RefundItem refundItem = RefundItem.builder()
                            .sku(orderRecord.getSku())
                            .itemName(orderRecord.getItemName())
                            .price(orderRecord.getPrice())
                            .quantity(returnItem.getQuantity())
                            .build();

                    CreateReturnResponse.ValidItem validItem = CreateReturnResponse.ValidItem.builder()
                            .sku(refundItem.getSku())
                            .pricePerUnit(refundItem.getPrice())
                            .quantityToReturn(refundItem.getQuantity())
                            .build();

                    orderReturn.addItem(refundItem);
                    items.add(validItem);
                } else {
                    CreateReturnResponse.InvalidItem invalidItem = CreateReturnResponse.InvalidItem.builder()
                            .reason(CreateReturnResponse.InvalidItem.Reason.QUANTITY_INVALID)
                            .sku(returnItem.getSku())
                            .quantityToReturn(returnItem.getQuantity())
                            .quantityAvailable(availableQuantity.get())
                            .build();

                    items.add(invalidItem);
                }
            }
            // if the order record can't be found then, add it to invalid item
            else {
                CreateReturnResponse.InvalidItem invalidItem = CreateReturnResponse.InvalidItem.builder()
                        .reason(CreateReturnResponse.InvalidItem.Reason.SKU_NOT_FOUND)
                        .sku(returnItem.getSku())
                        .build();

                items.add(invalidItem);
            }
        });

        // persist Order Return and Refund Items if only there's Refund Item
        if (orderReturn.getItems().size() > 0) {
            BigDecimal estimatedRefundAmount = orderReturn.getItems().stream()
                    .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            orderReturn.setEstimatedRefundAmount(estimatedRefundAmount);

            orderReturnRepository.save(orderReturn);
            refundItemRepository.saveAll(orderReturn.getItems());
        }

        // invalidate token
        invalidateToken(pendingReturn);

        return items;
    }

    @Transactional
    public void invalidateToken(PendingReturn pendingReturn) {
        pendingReturn.setValid(false);
        pendingReturnRepository.save(pendingReturn);
    }

    public OrderReturnDto getOrderReturn(long id) {
        OrderReturn orderReturn = orderReturnRepository.findById(id)
                .orElseThrow(ErrorCode.RETURN_ERR_03::exception);

        return orderReturnMapper.toDto(orderReturn);
    }

    @Transactional
    public void updateQCStatus(long orderReturnId, long refundItemId, UpdateQCStatusRequest request) {
        OrderReturn orderReturn = orderReturnRepository.findById(orderReturnId)
                .orElseThrow(ErrorCode.RETURN_ERR_03::exception);

        RefundItem refundItem = orderReturn.getItems().stream()
                .filter(item -> item.getId() == refundItemId)
                .findFirst().orElseThrow(ErrorCode.RETURN_ERR_04::exception);

        if (refundItem.getStatus() != null) {
            throw ErrorCode.RETURN_ERR_05.exception();
        }

        refundItem.setStatus(request.getStatus());
        refundItemRepository.save(refundItem);

        // count all refund item with status null
        long countStatusNull = orderReturn.getItems().stream()
                .filter(item -> item.getStatus() == null)
                .count();

        // if there's no refund item with status null then, set Order Return to Complete
        if (countStatusNull == 0) {
            orderReturn.setStatus(ReturnStatus.COMPLETE);
            orderReturnRepository.save(orderReturn);
        }
    }
}
