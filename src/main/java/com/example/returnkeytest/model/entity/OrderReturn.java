package com.example.returnkeytest.model.entity;

import com.example.returnkeytest.model.ReturnStatus;
import com.example.returnkeytest.model.entity.support.AuditEntity;
import com.example.returnkeytest.util.RoundingUtil;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class OrderReturn extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_return_id_generator")
    @SequenceGenerator(name = "order_return_id_generator", sequenceName = "order_return_seq")
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(updatable = false, nullable = false)
    private String token;

    @Column(updatable = false, nullable = false)
    private String orderId;

    @Enumerated(EnumType.STRING)
    private ReturnStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderReturn", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<RefundItem> items = new LinkedHashSet<>();

    @Column(columnDefinition = "Decimal(19,2) not null default '0.00'")
    private BigDecimal estimatedRefundAmount = BigDecimal.ZERO;

    protected OrderReturn(OrderReturnBuilder<?, ?> b) {
        super(b);
        this.id = b.id;
        this.token = b.token;
        this.orderId = b.orderId;
        this.status = b.status;
        this.items = b.items == null ? new LinkedHashSet<>() : b.items;
        this.estimatedRefundAmount = b.estimatedRefundAmount == null ? BigDecimal.ZERO :
                b.estimatedRefundAmount.setScale(RoundingUtil.Currency.SCALE, RoundingUtil.Currency.ROUNDING_MODE);
    }

    public void setEstimatedRefundAmount(BigDecimal estimatedRefundAmount) {
        this.estimatedRefundAmount = estimatedRefundAmount
                .setScale(RoundingUtil.Currency.SCALE, RoundingUtil.Currency.ROUNDING_MODE);
    }

    public Set<RefundItem> getItems() {
        return Collections.unmodifiableSet(items);
    }

    public void setItems(Set<RefundItem> items) {
    }

    public void addItem(RefundItem item) {
        item.setOrderReturn(this);
        items.add(item);
    }
}
