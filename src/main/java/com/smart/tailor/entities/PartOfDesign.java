package com.smart.tailor.entities;

import com.smart.tailor.enums.PartOfDesignName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "part_of_design")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PartOfDesign extends AuditEntity implements Serializable {
    @Id
    @Column(name = "part_of_design_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID partOfDesignID;

    @ManyToOne
    @JoinColumn(name = "design_id", referencedColumnName = "design_id")
    private Design design;

    @Column(name = "part_of_design_name")
    @Enumerated(EnumType.STRING)
    private PartOfDesignName partOfDesignName;
}
