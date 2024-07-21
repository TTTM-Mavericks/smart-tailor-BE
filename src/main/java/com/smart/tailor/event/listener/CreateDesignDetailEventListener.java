package com.smart.tailor.event.listener;

import com.smart.tailor.constant.LinkConstant;
import com.smart.tailor.event.CreateDesignDetailEvent;
import com.smart.tailor.service.DesignDetailService;
import com.smart.tailor.service.MailService;
import com.smart.tailor.service.OrderService;
import com.smart.tailor.utils.response.OrderCustomResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateDesignDetailEventListener implements ApplicationListener<CreateDesignDetailEvent> {
    private final MailService mailService;
    private final DesignDetailService designDetailService;
    private final Logger logger = LoggerFactory.getLogger(CreateDesignDetailEventListener.class);
    private final OrderService orderService;

    @Value("${client.server.link}")
    private String clientServerLink;

    @Override
    public void onApplicationEvent(CreateDesignDetailEvent event) {
        var orderDetailResponse = event.getOrderDetailResponse();
        var orderID = orderDetailResponse.getOrderID();
        logger.info("Order ID Line Code 28 {}", orderID);

        OrderCustomResponse orderResponse = null;
        try {
            orderResponse = orderService.getOrderByOrderID(orderID);
            var listBrandEmailSelected = orderService.filterBrandForSpecificOrderBaseOnDesign(orderResponse.getDesignResponse().getDesignID());
            // send Mail to selected Brand for Specific Order
            for(var brandEmailSelected : listBrandEmailSelected){
                mailService.sendMailToSelectedBrandsForSpecificOrder(
                        brandEmailSelected,
                        "Order Design For Brand",
                        clientServerLink + "/" + orderID,
                        orderResponse
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
