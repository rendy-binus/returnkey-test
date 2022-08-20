package com.example.returnkeytest.model.entity.support;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class)
})
@MappedSuperclass
public class BaseEntity {
    @CreationTimestamp
    OffsetDateTime createdDate;
    @UpdateTimestamp
    OffsetDateTime updatedDate;
    @Id
    @GeneratedValue
    private Long id;
}
