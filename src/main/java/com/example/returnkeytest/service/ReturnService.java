package com.example.returnkeytest.service;

import com.devskiller.friendly_id.FriendlyId;
import com.example.returnkeytest.config.SftpConfig;
import com.example.returnkeytest.model.OrderCsvHeader;
import com.example.returnkeytest.model.OrderRecord;
import com.example.returnkeytest.model.dto.initreturn.InitReturnRequest;
import com.example.returnkeytest.model.dto.initreturn.InitReturnResponse;
import com.example.returnkeytest.model.entity.PendingReturn;
import com.example.returnkeytest.repository.PendingReturnRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReturnService {
    private final SftpConfig sftpConfig;
    private final SftpService sftpService;

    private final PendingReturnRepository pendingReturnRepository;

    @Transactional
    public InitReturnResponse initReturn(InitReturnRequest request) {
        log.info("orderId: {} | emailAddress: {}", request.getOrderId(), request.getEmailAddress());

        String token = FriendlyId.createFriendlyId();
        String localFilePath = String.format(sftpConfig.getOrder().getLocalFileName(), token);
        String remoteFilePath = String.format("%s/%s", sftpConfig.getOrder().getRemoteFilePath(), sftpConfig.getOrder().getRemoteFileName());

        if (sftpService.downloadFile(localFilePath, remoteFilePath)) {
            List<OrderRecord> orderRecords = getOrderRecords(localFilePath).stream()
                    .filter(or -> or.getOrderId().equalsIgnoreCase(request.getOrderId()) &&
                            or.getEmailAddress().equalsIgnoreCase(request.getEmailAddress()))
                    .collect(Collectors.toList());

            PendingReturn pendingReturn = PendingReturn.builder()
                    .token(token)
                    .orderRecords(orderRecords)
                    .build();

            pendingReturnRepository.save(pendingReturn);
        }

        return InitReturnResponse.builder()
                .token(token)
                .build();
    }

    private List<OrderRecord> getOrderRecords(String localFilePath) {
        List<OrderRecord> orderRecords = new ArrayList<>();

        List<List<String>> records = readOrderCsv(localFilePath);

        // get headers
        List<OrderCsvHeader> headers = records.get(0).stream()
                .map(h -> OrderCsvHeader.fromValue(h.trim()))
                .collect(Collectors.toList());

        records.remove(0);

        // loop through records and map it based on headers
        for (List<String> record : records) {
            if (record.size() < headers.size()) {
                continue;
            }

            OrderRecord orderRecord = new OrderRecord();

            for (int i = 0; i < headers.size(); i++) {
                OrderCsvHeader header = headers.get(i);

                switch (header) {
                    case ORDER_ID:
                        orderRecord.setOrderId(record.get(i).toUpperCase());
                        break;
                    case EMAIL_ADDRESS:
                        orderRecord.setEmailAddress(record.get(i).toLowerCase());
                        break;
                    case SKU:
                        orderRecord.setSku(record.get(i).toUpperCase());
                        break;
                    case QUANTITY:
                        orderRecord.setQuantity(Integer.parseInt(record.get(i)));
                        break;
                    case PRICE:
                        orderRecord.setPrice(new BigDecimal(record.get(i)));
                        break;
                    case ITEM_NAME:
                        orderRecord.setItemName(record.get(i));
                        break;
                    default:
                        break;
                }
            }

            orderRecords.add(orderRecord);
        }

        return orderRecords;
    }

    private List<List<String>> readOrderCsv(String localFilePath) {
        List<List<String>> records = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(
                new InputStreamReader(new BOMInputStream(new FileInputStream(localFilePath)), StandardCharsets.UTF_8)
        )
        ) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        } catch (CsvValidationException | IOException e) {
            throw new RuntimeException(e);
        }

        log.info("csv content:\n{}", records);

        return records;
    }

}
