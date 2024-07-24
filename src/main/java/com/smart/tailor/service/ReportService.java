package com.smart.tailor.service;

import com.smart.tailor.utils.request.ReportRequest;
import com.smart.tailor.utils.response.ReportResponse;

import java.util.List;
import java.util.UUID;

public interface ReportService {
    void createReport(ReportRequest reportRequest) throws Exception;

    List<ReportResponse> getAllReport();

    List<ReportResponse> getAllReportByOrderID(UUID orderID);
}
