package com.example.returnkeytest.model.entity;

import com.example.returnkeytest.model.ReturnStatus;
import com.example.returnkeytest.model.entity.support.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class OrderReturn extends BaseEntity {
    private String returnId;

    @Enumerated(EnumType.STRING)
    private ReturnStatus status;
}
