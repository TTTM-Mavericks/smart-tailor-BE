package com.smart.tailor.service.impl;

import com.smart.tailor.entities.Report;
import com.smart.tailor.entities.ReportImage;
import com.smart.tailor.enums.RoleType;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.ReportMapper;
import com.smart.tailor.repository.ReportRepository;
import com.smart.tailor.service.OrderService;
import com.smart.tailor.service.ReportImageService;
import com.smart.tailor.service.ReportService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.utils.request.ReportRequest;
import com.smart.tailor.utils.response.OrderResponse;
import com.smart.tailor.utils.response.ReportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

        var order = orderService.getOrderById(reportRequest.getOrderID())
                .orElseThrow(() -> new ItemNotFoundException("Can not find Order with OrderID: " + reportRequest.getOrderID()));

        if(user.getRoles().getRoleName().equals(RoleType.CUSTOMER.name())){
            if(!order.getOrderType().equals("PARENT_ORDER")){
                throw new BadRequestException("Customer can only report ParentOrder");
            }
        }

        if(user.getRoles().getRoleName().equals(RoleType.BRAND.name())){
            if(!order.getOrderType().equals("SUB_ORDER")){
                throw new BadRequestException("Brand can only report SubOrder");
            }
        }

        if(user.getRoles().getRoleName().equals(RoleType.EMPLOYEE.name())){
            if(!order.getOrderType().equals("SUB_ORDER") || !order.getOrderType().equals("PARENT_ORDER")){
                throw new BadRequestException("Employee can report ParentOrder and SubOrder");
            }
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
        return reportRepository
                .findAll()
                .stream()
                .filter(report -> report.getUser().getUserID().equals(userID))
                .map(reportMapper::mapperToReportResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportResponse> getAllReportByBrandID(String brandID) throws Exception {
        return reportRepository
                .findAll()
                .stream()
                .filter(report -> report.getUser().getUserID().equals(brandID))
                .map(reportMapper::mapperToReportResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportResponse> getAllReportByParentOrderID(String parentOrderID) {
        List<ReportResponse> reportResponseList = new ArrayList<>(getAllReportByOrderID(parentOrderID));

        List<OrderResponse> subOrderList = orderService.getSubOrderByParentID(parentOrderID);

        subOrderList
                .stream()
                .map(OrderResponse::getOrderID)
                .map(this::getAllReportByOrderID)
                .forEach(reportResponseList::addAll);

        return reportResponseList;
    }
}
