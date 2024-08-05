package com.smart.tailor.service.impl;

import com.smart.tailor.entities.Report;
import com.smart.tailor.entities.ReportImage;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.ReportMapper;
import com.smart.tailor.repository.ReportRepository;
import com.smart.tailor.service.OrderService;
import com.smart.tailor.service.ReportImageService;
import com.smart.tailor.service.ReportService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.utils.request.ReportRequest;
import com.smart.tailor.utils.response.ReportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
    private final ReportImageService reportImageService;
    private final OrderService orderService;
    private final ReportMapper reportMapper;
    private final UserService userService;

    @Transactional
    @Override
    public void createReport(ReportRequest reportRequest) throws Exception {
        var user = userService.getUserByUserID(reportRequest.getUserID())
                .orElseThrow(() -> new ItemNotFoundException("Can not find User with UserID: " + reportRequest.getUserID()));

        String orderID = reportRequest.getOrderID();
        var order = orderService.getOrderById(orderID)
                .orElseThrow(() -> new ItemNotFoundException("Can not find Order with OrderID: " + orderID));

        var isValidOrderByCustomerID = orderService.getOrderByUserID(user.getUserID())
                .stream()
                .filter(orderCustomResponse -> orderCustomResponse.getOrderID().equals(orderID))
                .findFirst();

        var isValidOrderByBrandID = orderService.getOrderByBrandID(user.getUserID())
                .stream()
                .filter(orderCustomResponse -> orderCustomResponse.getOrderID().equals(orderID))
                .findFirst();

        var isValidOrderForEmployee = order
                .getEmployee()
                .getEmployeeID()
                .equals(orderID);


        if(isValidOrderByCustomerID.isEmpty() && isValidOrderByBrandID.isEmpty() && !isValidOrderForEmployee){
            throw new ItemNotFoundException("Can not find Order with UserID: " + user.getUserID() + " to Report");
        }

        var saveReport = reportRepository.save(
                Report
                        .builder()
                        .typeOfReport(reportRequest.getTypeOfReport())
                        .order(order)
                        .user(user)
                        .content(reportRequest.getContent())
                        .reportStatus(true)
                        .build()
        );

        List<ReportImage> reportImageList = reportImageService.createReportImage(saveReport, reportRequest.getReportImageList());

        saveReport.setReportImageList(reportImageList);

        reportRepository.save(saveReport);
    }

    @Override
    public List<ReportResponse> getAllReport() {
        return reportRepository
                .findAll()
                .stream()
                .map(reportMapper::mapperToReportResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportResponse> getAllReportByOrderID(String orderID) {
        return reportRepository
                .findAll()
                .stream()
                .filter(report -> report.getOrder().getOrderID().toString().equals(orderID.toString()))
                .map(reportMapper::mapperToReportResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportResponse> getAllReportByUserID(String userID) throws Exception {
        return orderService
                .getOrderByUserID(userID)
                .stream()
                .flatMap(orderCustomResponse -> getAllReportByOrderID(orderCustomResponse.getOrderID()).stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportResponse> getAllReportByBrandID(String brandID) throws Exception {
        return orderService
                .getOrderByBrandID(brandID)
                .stream()
                .flatMap(orderCustomResponse -> getAllReportByOrderID(orderCustomResponse.getOrderID()).stream())
                .collect(Collectors.toList());
    }
}
