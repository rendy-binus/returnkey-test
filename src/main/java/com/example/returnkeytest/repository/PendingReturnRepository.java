package com.example.returnkeytest.repository;

import com.example.returnkeytest.model.entity.PendingReturn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingReturnRepository extends JpaRepository<PendingReturn, Long> {
}
