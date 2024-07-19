package com.smart.tailor.service.impl;

import com.smart.tailor.entities.User;
import com.smart.tailor.enums.OrderStatus;
import com.smart.tailor.enums.PaymentType;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.request.OrderStatusUpdateRequest;
import com.smart.tailor.utils.request.PaymentRequest;
import com.smart.tailor.utils.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class ScheduleTaskServiceImpl implements ScheduleTaskService {
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(ScheduleTaskServiceImpl.class);
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final DesignDetailService detailService;

    @Scheduled(cron = "0 0 * * * *") // second - minute - hour - dayOfMonth - month - dayOfWeek   // Run every hour
    @Override
    public void deleteUserWithEmailUnverifiedSchedule() {
        logger.info("Scheduled task is running at {}", LocalDateTime.now());
        var listUnverifiedUser = userService.findAllUnverifiedUser();
        for (User user : listUnverifiedUser) {
            LocalDateTime userCreateDateTime = user.getCreateDate();
            // Config System Properties about Time Expire For Each UnVerified User
            // Using Default Expire Time
            LocalDateTime userExpiredDateTime = userCreateDateTime.plusHours(12);
            LocalDateTime currentDateTime = LocalDateTime.now();
            if (currentDateTime.isAfter(userExpiredDateTime)) {
                logger.info("Delete User {}", user);
                userService.deleteUnverifiedUser(user.getUserID());
            }
        }
    }

    @Scheduled(cron = "0 * * * * *") // Run every minute
    @Override
    public void checkValidOrderAfterExpirationTimeOrder() throws Exception {
        logger.info("Inside Method checkValidOrderAfterExpirationTimeOrder");
        var orders = orderService.getAllParentOrder();
        for (OrderResponse orderResponse : orders) {
            var checkOrderExpireTime = orderService.isOrderExpireTime(orderResponse.getOrderID());
            if (checkOrderExpireTime && orderResponse.getOrderStatus().equals(OrderStatus.PENDING)) {
                var status = orderService.isOrderCompletelyPicked(orderResponse.getOrderID());
                var order = orderService.getOrderById(orderResponse.getOrderID()).get();
                if (status) {
                    var detail = detailService.findAllByOrderID(orderResponse.getOrderID());
                    var design = detail.getDesign();
                    var sender = design.getUser();
                    var recipient = userService.getUserByEmail("hoanganhduy1122@gmail.com");
                    /**
                     * UPDATE STATUS TO DEPOSIT
                     */
                    logger.info("Change Status Start Order");
                    orderService.changeOrderStatus(
                            OrderStatusUpdateRequest
                                    .builder()
                                    .orderID(order.getOrderID())
                                    .status(OrderStatus.DEPOSIT)
                                    .build()
                    );
//                    List<Payment> paymentList = order.getPaymentList();\
//                    var checkDeposit = false;
//                    if (!paymentList.isEmpty()) {
//                        for (Payment payment : paymentList) {
//                            if (payment.getPaymentType().equals(PaymentType.DEPOSIT) && payment.getPaymentStatus()) {
//                                checkDeposit = true;
//                                break;
//                            }
//                        }
//                    }
//                    logger.error("CHECK DEPOSIT {}", checkDeposit);
//                    if (!checkDeposit) {
                    var payOSResponse = paymentService.createPayOSPayment(
                            PaymentRequest
                                    .builder()
                                    .order(order)

                                    .paymentSenderID(sender.getUserID())
                                    .paymentSenderName(sender.getFullName())
                                    .paymentSenderBankCode("")
                                    .paymentSenderBankNumber("")

                                    .paymentRecipientID(recipient.getUserID())
                                    .paymentRecipientName(recipient.getFullName())
                                    .paymentRecipientBankCode("")
                                    .paymentRecipientBankNumber("")

                                    .paymentType(PaymentType.DEPOSIT)
                                    .paymentAmount(order.getTotalPrice())
                                    .itemList(null)
                                    .build()
                    );
                } else {
                    logger.info("Change Status Delete Order");
                    orderService.changeOrderStatus(
                            OrderStatusUpdateRequest
                                    .builder()
                                    .orderID(order.getOrderID())
                                    .status(OrderStatus.CANCEL)
                                    .build()
                    );
                }
            }
        }
    }
}
