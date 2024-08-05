package com.smart.tailor.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "report")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Report extends AuditEntity implements Serializable {
    @Id
    @Column(name = "report_id", unique = true, nullable = false)
    private String reportID;

    @Column(name = "type_of_report", columnDefinition = "varchar(50) CHARACTER SET utf8 COLLATE utf8_bin")
    private String typeOfReport;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = true, unique = false)
    private Order order;

    @Column(columnDefinition = "text")
    private String content;

    @Column(name = "report_status")
    private Boolean reportStatus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "report")
    @JsonManagedReference
    private List<ReportImage> reportImageList;

    @PrePersist
    private void prePersist() {
        this.reportID = Utilities.generateCustomPrimaryKey();
    }
}
