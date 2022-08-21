package com.example.returnkeytest.repository;

import com.example.returnkeytest.model.entity.OrderReturn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderReturnRepository extends JpaRepository<OrderReturn, Long> {
    Optional<OrderReturn> findByOrderId(String orderId);

    OrderReturn findFirstByToken(String token);
}
