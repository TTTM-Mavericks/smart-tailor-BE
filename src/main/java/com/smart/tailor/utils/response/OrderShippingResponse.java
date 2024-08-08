package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderShippingResponse {
    private Boolean success;

    private String message;

    private OrderShippingDetailResponse order;

    private String warning_message;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderShippingDetailResponse {
        private String partner_id; // Mã đơn hàng thuộc hệ thống của đối tác

        private String label; // Mã đơn hàng của hệ thống GHTK

        private Integer area;

        private Integer fee; // Cước vận chuyển tính theo VNĐ

        private Integer insurance_fee; // Giá bảo hiểm tính theo VNĐ

        private String estimated_pick_time;

        private String estimated_deliver_time;

        private List<Object> products; // Assuming products can be of any type or empty

        private Integer status_id; //  Mã trạng thái đơn hàng

        private Long tracking_id;

        private String sorting_code;

        private String date_to_delay_pick;

        private Integer pick_work_shift; //  Nếu set bằng 3 đơn hàng sẽ lấy vào buổi tối. 2: buồi chiều. 1: buổi sáng. Giá trị mặc định GHTK set theo ca tự tính.

        private String date_to_delay_deliver;

        private Integer deliver_work_shift; // Nếu set bằng 3 đơn hàng sẽ được giao vào buổi tối. 2: buồi chiều. 1: buổi sáng. Giá trị mặc định GHTK set theo ca tự tính.

        private Integer pkg_draft_id;

        private Integer is_xfast;
    }

}