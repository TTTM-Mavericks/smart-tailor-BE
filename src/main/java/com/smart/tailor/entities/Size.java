package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "size")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Size extends AuditEntity implements Serializable {
    @Id
    @Column(name = "size_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID sizeID;

    @Column(name = "size_name")
    private String sizeName;

    private Boolean status;
}
