package com.smart.tailor.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "payos")
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayOSData {
    @Id
    private Integer orderCode; //Mã đơn hàng từ cửa hàng
    private String bin; //Mã định danh ngân hàng (thường gọi là BIN)
    private String accountNumber; //Số tài khoản ngân hàng thụ hưởng
    private String accountName; //Tên tài khoản ngân hàng
    private String currency; //Đơn vị tiền tệ
    private String paymentLinkId; //Mã link thanh toán
    private Integer amount; //Số tiền thanh toán
    private String description; //Mô tả thanh toán
    private String status; //Trạng thái link thanh toán
    private String checkoutUrl; //Link thanh toán
    private String qrCode; //Mã VietQR dạng text
}
