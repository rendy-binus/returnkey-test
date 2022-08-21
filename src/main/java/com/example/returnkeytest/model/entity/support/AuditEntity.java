package com.example.returnkeytest.model.entity.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class AuditEntity {
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdDate;

    @UpdateTimestamp
    @Column(nullable = false)
    private OffsetDateTime updatedDate;
}
