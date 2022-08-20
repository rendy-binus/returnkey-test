package com.example.returnkeytest.model.entity;

import com.example.returnkeytest.model.OrderRecord;
import com.example.returnkeytest.model.entity.support.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PendingReturn extends BaseEntity {
    private String token;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<OrderRecord> orderRecords;
}
