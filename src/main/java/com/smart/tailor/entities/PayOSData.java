package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payos")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayOSData extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer payOSID;
    private Integer orderCode; //Mã đơn hàng từ cửa hàng
    private Integer amount;
    private String status;
    private String checkoutUrl; //Link thanh toán
    private String qrCode; //Mã VietQR dạng text
}
