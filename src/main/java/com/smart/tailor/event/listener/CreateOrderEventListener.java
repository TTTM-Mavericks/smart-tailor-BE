package com.smart.tailor.event.listener;

import com.smart.tailor.event.CreateOrderEvent;
import com.smart.tailor.service.MailService;
import com.smart.tailor.service.OrderService;
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
public class CreateOrderEventListener implements ApplicationListener<CreateOrderEvent> {
    private final MailService mailService;
    private final Logger logger = LoggerFactory.getLogger(CreateOrderEventListener.class);
    private final OrderService orderService;

    @Value("${client.server.link}")
    private String clientServerLink;

    @Override
    public void onApplicationEvent(CreateOrderEvent event) {
        var orderResponse = event.getOrderResponse();
        try {
            var orderCustomResponse = orderService.getOrderByOrderID(orderResponse.getOrderID());
            var listBrandEmailSelected = orderService.filterBrandForSpecificOrderBaseOnDesign(orderCustomResponse.getDesignResponse().getDesignID());
            // send Mail to selected Brand for Specific Order
            for (var brandEmailSelected : listBrandEmailSelected) {
                mailService.sendMailToSelectedBrandsForSpecificOrder(
                        brandEmailSelected,
                        "Order Design For Brand",
                        clientServerLink + "/" + orderResponse.getOrderID(),
                        orderCustomResponse
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
