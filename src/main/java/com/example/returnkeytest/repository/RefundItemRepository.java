package com.example.returnkeytest.repository;

import com.example.returnkeytest.model.entity.RefundItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RefundItemRepository extends JpaRepository<RefundItem, Long> {
    @Query(value = "select ri from RefundItem ri join fetch ri.orderReturn orderReturn where ri.sku = ?1 and orderReturn.orderId = ?2")
    List<RefundItem> findAllBySkuAndOrderId(String sku, String orderId);
}
