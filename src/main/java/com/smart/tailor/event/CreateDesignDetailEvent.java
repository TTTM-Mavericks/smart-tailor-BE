package com.smart.tailor.event;

import com.smart.tailor.utils.response.OrderDetailResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class CreateDesignDetailEvent extends ApplicationEvent {
    private OrderDetailResponse orderDetailResponse;

    public CreateDesignDetailEvent(OrderDetailResponse orderDetailResponse){
        super(orderDetailResponse);
        this.orderDetailResponse = orderDetailResponse;
    }
}
