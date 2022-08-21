package com.example.returnkeytest.model.entity;

import com.example.returnkeytest.model.OrderRecord;
import com.example.returnkeytest.model.entity.support.AuditEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class PendingReturn extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pending_return_id_generator")
    @SequenceGenerator(name = "pending_return_id_generator", sequenceName = "pending_return_seq")
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(updatable = false, nullable = false)
    private String token;

    @Column(updatable = false, nullable = false)
    private String orderId;

    @Column(updatable = false, nullable = false)
    private String emailAddress;

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private List<OrderRecord> orderRecords;

    @Column(columnDefinition = "boolean default true", nullable = false)
    private Boolean valid;

    protected PendingReturn(PendingReturnBuilder<?, ?> b) {
        super(b);
        this.id = b.id;
        this.token = b.token;
        this.orderId = b.orderId;
        this.emailAddress = b.emailAddress;
        this.orderRecords = b.orderRecords;
        this.valid = b.valid == null || b.valid;
    }
}
