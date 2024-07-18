package com.smart.tailor.service.impl;

import com.smart.tailor.entities.User;
import com.smart.tailor.enums.OrderStatus;
import com.smart.tailor.service.OrderService;
import com.smart.tailor.service.ScheduleTaskService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.utils.response.OrderResponse;
import com.smart.tailor.utils.response.UserResponse;
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

    @Scheduled(cron = "0 0 * * * *") // second - minute - hour - dayOfMonth - month - dayOfWeek   // Run every hour
    @Override
    public void deleteUserWithEmailUnverifiedSchedule() {
        logger.info("Scheduled task is running at {}", LocalDateTime.now());
        var listUnverifiedUser = userService.findAllUnverifiedUser();
        for(User user : listUnverifiedUser){
            LocalDateTime userCreateDateTime = user.getCreateDate();
            // Config System Properties about Time Expire For Each UnVerified User
            // Using Default Expire Time
            LocalDateTime userExpiredDateTime = userCreateDateTime.plusHours(12);
            LocalDateTime currentDateTime = LocalDateTime.now();
            if(currentDateTime.isAfter(userExpiredDateTime)){
                logger.info("Delete User {}", user);
                userService.deleteUnverifiedUser(user.getUserID());
            }
        }
    }

    @Scheduled(cron = "0 * * * * *") // Run every minute
    @Override
    public void checkValidOrderAfterExpirationTimeOrder() {
        logger.info("Inside Method checkValidOrderAfterExpirationTimeOrder");
        var orders = orderService.getAllParentOrderWithUnVerifyStatus();

        for(OrderResponse orderResponse : orders){
            var checkOrderExpireTime = orderService.isOrderExpireTime(orderResponse.getOrderID());
            if(checkOrderExpireTime){
                if(orderService.isOrderCompletelyPicked(orderResponse.getOrderID())){
                    logger.info("Change Status Start Order");
                } else {
                    logger.info("Change Status Delete Order");
                }
            }
        }
    }
}
