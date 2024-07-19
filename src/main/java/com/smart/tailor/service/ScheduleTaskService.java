package com.smart.tailor.service;

public interface ScheduleTaskService {
    void deleteUserWithEmailUnverifiedSchedule();

    void checkValidOrderAfterExpirationTimeOrder() throws Exception;
}
