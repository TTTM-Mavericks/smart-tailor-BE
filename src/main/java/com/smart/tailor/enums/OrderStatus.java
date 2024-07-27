package com.smart.tailor.enums;

public enum OrderStatus {
    NOT_VERIFY, // ch xac thuc design
    PENDING,    // cho brand pick
    DEPOSIT,    // cho dat coc
    PROCESSING, // cho may
    CANCEL,     // huy
    COMPLETED,   // hoan thanh nhung chua giao
    DELIVERED,  // da giao

    /**
     * SUB ORDER
     */
    START_PRODUCING,
    FINISH_FIRST_STAGE,
    FINISG_SECOND_STAGE,

}
