package com.smart.tailor.service.impl;

import com.smart.tailor.entities.Payment;
import com.smart.tailor.entities.User;
import com.smart.tailor.enums.OrderStatus;
import com.smart.tailor.enums.PaymentType;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.request.OrderStatusUpdateRequest;
import com.smart.tailor.utils.request.PaymentRequest;
import com.smart.tailor.utils.response.OrderResponse;
import com.smart.tailor.utils.response.PayOSResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class ScheduleTaskServiceImpl implements ScheduleTaskService {
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(ScheduleTaskServiceImpl.class);
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final PayOSDataService payOSDataService;
    private final PayOSService payOSService;
    private final DesignDetailService detailService;
    private final SystemPropertiesService systemPropertiesService;

    @Scheduled(cron = "0 0 * * * *") // second - minute - hour - dayOfMonth - month - dayOfWeek   // Run every hour
    @Override
    public void deleteUserWithEmailUnverifiedSchedule() {
        logger.info("Scheduled task is running at {}", LocalDateTime.now());
        var listUnverifiedUser = userService.findAllUnverifiedUser();
        var timeBeforeAccountDelete = Integer.parseInt(systemPropertiesService.getByName("TIME_BEFORE_ACCOUNT_DELETION").getPropertyValue());
        for (User user : listUnverifiedUser) {
            LocalDateTime userCreateDateTime = user.getCreateDate();
            // Config System Properties about Time Expire For Each UnVerified User
            // Using Default Expire Time
            LocalDateTime userExpiredDateTime = userCreateDateTime.plusHours(timeBeforeAccountDelete);
            LocalDateTime currentDateTime = LocalDateTime.now();
            if (currentDateTime.isAfter(userExpiredDateTime)) {
                logger.info("Delete User {}", user);
                userService.deleteUnverifiedUser(user.getUserID());
            }
        }
    }

    @Scheduled(cron = "0 * * * * *") // Run every minute
    @Override
    @Transactional
    public void checkValidOrderAfterExpirationTimeOrder() throws Exception {
        logger.info("Inside Method checkValidOrderAfterExpirationTimeOrder");
        var orders = orderService.getAllParentOrder();
        for (OrderResponse orderResponse : orders) {
            var checkOrderExpireTime = orderService.isOrderExpireTime(orderResponse.getOrderID());
            var orderStatus = orderResponse.getOrderStatus();
            if (checkOrderExpireTime) {
                var status = orderService.isOrderCompletelyPicked(orderResponse.getOrderID());
                var order = orderService.getOrderById(orderResponse.getOrderID()).get();
                if (status) {
                    var detail = detailService.findAllByOrderID(orderResponse.getOrderID());
                    var design = detail.getDesign();
                    var sender = design.getUser();
                    var recipient = userService.getUserByEmail("accountantsmarttailor123@gmail.com");

                    var paymentList = paymentService.findAllByOrderID(orderResponse.getOrderID());
                    for (Payment p : paymentList) {
                        var orderCode = p.getPaymentCode();
                        var onlinePayOS = payOSService.getPaymentInfo(orderCode).getData();
                        var checkPayOSData = payOSDataService.findByOrderCode(orderCode);
                        if (checkPayOSData.isPresent()) {
                            var payOSData = checkPayOSData.get();
                            payOSData.setStatus(onlinePayOS.getStatus());
                            payOSDataService.save(payOSData);
                            p.setPaymentStatus(payOSData.getStatus().equals("PAID"));
                            paymentService.updatePayment(p);
                        }
                    }
                    logger.error("PAYMENT LIST: {}", paymentList);
                    switch (orderStatus) {
                        case PENDING -> {
                            logger.error("INCASE PENDING");
                            /**
                             * UPDATE STATUS TO DEPOSIT
                             */
                            logger.info("Change Status Start Order");
                            orderService.changeOrderStatus(
                                    OrderStatusUpdateRequest
                                            .builder()
                                            .orderID(order.getOrderID().toString())
                                            .status(OrderStatus.DEPOSIT.name())
                                            .build()
                            );
                            orderService.confirmOrder(order.getOrderID());
                            var payOSResponse = paymentService.createPayOSPayment(
                                    PaymentRequest
                                            .builder()
                                            .orderID(orderResponse.getOrderID())

                                            .paymentSenderID(sender.getUserID())
                                            .paymentSenderName(sender.getFullName())
                                            .paymentSenderBankCode("")
                                            .paymentSenderBankNumber("")

                                            .paymentRecipientID(recipient.getUserID())
                                            .paymentRecipientName(recipient.getFullName())
                                            .paymentRecipientBankCode("OCB")
                                            .paymentRecipientBankNumber("0163100007285002")

                                            .paymentType(PaymentType.DEPOSIT)
                                            .paymentAmount(order.getTotalPrice())
                                            .itemList(null)
                                            .build()
                            );
                        }
                    }
                } else {
                    logger.info("Change Status Delete Order");
                    orderService.changeOrderStatus(
                            OrderStatusUpdateRequest
                                    .builder()
                                    .orderID(order.getOrderID().toString())
                                    .status(OrderStatus.CANCEL.name())
                                    .build()
                    );
                }
            }
        }
    }

    @Scheduled(cron = "0 * * * * *") // Run every minute
    @Override
    @Transactional
    public void updatePayOS() throws Exception {
        logger.info("Inside Method updatePayOS");
        var paymentList = paymentService.getAllPayment();
        for (Payment payment : paymentList) {
            if (payment != null) {
                if (payment.getPaymentType() != null) {
                    PayOSResponse payOS = null;
                    if (payment.getPaymentType().equals(PaymentType.BRAND_INVOICE)) {
                        payOS = payOSService.getBrandPaymentInfo(payment.getPaymentCode());
                    } else {
                        if (payment.getPaymentType().equals(PaymentType.ORDER_REFUND)) {
                            payOS = payOSService.getRefundPaymentInfo(payment.getPaymentCode());
                        } else
                            payOS = payOSService.getPaymentInfo(payment.getPaymentCode());
                    }
                    if (payOS != null) {
                        payment.setPaymentStatus(payOS.getData().getStatus().equals("PAID"));
                        paymentService.updatePayment(payment);
                    }
                }
            }
        }
    }
}
