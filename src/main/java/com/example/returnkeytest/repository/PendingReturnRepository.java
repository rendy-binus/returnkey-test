package com.example.returnkeytest.repository;

import com.example.returnkeytest.model.entity.PendingReturn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PendingReturnRepository extends JpaRepository<PendingReturn, Long> {
    Optional<PendingReturn> findFirstByTokenAndValidIsTrue(String token);
}
