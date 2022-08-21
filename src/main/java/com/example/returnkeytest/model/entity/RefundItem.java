package com.example.returnkeytest.model.entity;

import com.example.returnkeytest.model.QCStatus;
import com.example.returnkeytest.model.entity.support.AuditEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RefundItem extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_refund_id_generator")
    @SequenceGenerator(name = "item_refund_id_generator", sequenceName = "item_refund_seq")
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(updatable = false, nullable = false)
    private String sku;

    @Column(nullable = false)
    private String itemName;

    @Column(columnDefinition = "integer not null default 1")
    private Integer quantity;

    @Column(columnDefinition = "Decimal(19,2) not null default '0.00'")
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private QCStatus status;

    @ManyToOne
    @JoinColumn(name = "order_return_id")
    @JsonBackReference
    private OrderReturn orderReturn;
}
