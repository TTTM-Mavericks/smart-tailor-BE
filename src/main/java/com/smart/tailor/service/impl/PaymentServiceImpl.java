package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Payment;
import com.smart.tailor.enums.PaymentMethod;
import com.smart.tailor.enums.PaymentType;
import com.smart.tailor.mapper.PaymentMapper;
import com.smart.tailor.repository.OrderRepository;
import com.smart.tailor.repository.PaymentRepository;
import com.smart.tailor.service.PayOSService;
import com.smart.tailor.service.PaymentService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.utils.request.PayOSItem;
import com.smart.tailor.utils.request.PayOSRequest;
import com.smart.tailor.utils.request.PaymentRequest;
import com.smart.tailor.utils.response.PaymentResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PayOSService payOSService;
    private final UserService userService;
    private final PaymentMapper paymentMapper;
    private final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Value("${SERVER_URL}")
    private String clientURL;

    @Override
    public PaymentResponse createPayOSPayment(PaymentRequest paymentRequest) throws Exception {
        try {
            /**
             * TODO
             * Validate payment request
             */
            UUID paymentSenderID = paymentRequest.getPaymentSenderID();
            String paymentSenderName = paymentRequest.getPaymentSenderName();
            String paymentSenderBankCode = paymentRequest.getPaymentSenderBankCode();
            String paymentSenderBankNumber = paymentRequest.getPaymentSenderBankNumber();

            UUID paymentRecipientID = paymentRequest.getPaymentRecipientID();
            String paymentRecipientName = paymentRequest.getPaymentRecipientName();
            String paymentRecipientBankCode = paymentRequest.getPaymentRecipientBankCode();
            String paymentRecipientBankNumber = paymentRequest.getPaymentRecipientBankNumber();

            Integer paymentAmount = paymentRequest.getPaymentAmount();
            PaymentMethod paymentMethod = paymentRequest.getPaymentMethod();
            Boolean paymentStatus = false;
            PaymentType paymentType = paymentRequest.getPaymentType();

            if (paymentType.equals(PaymentType.CUSTOMER_UPGRADE) || paymentType.equals(PaymentType.BRAND_REGISTRATION)) {
                var checkSender = userService.getUserByUserID(paymentSenderID);
                if (checkSender.isEmpty()) {
                    throw new Exception(MessageConstant.USER_IS_NOT_FOUND + " with ID: " + paymentSenderID);
                }
                var sender = checkSender.get();

                var checkRecipient = userService.getUserByUserID(paymentRecipientID);
                if (checkRecipient.isEmpty()) {
                    throw new Exception(MessageConstant.USER_IS_NOT_FOUND + " with ID: " + paymentRecipientID);
                }
                var recipient = checkRecipient.get();

                String senderEmail = sender.getEmail();
                String senderPhone = sender.getPhoneNumber();
                String senderAddress = "";
                if (sender.getRoles().getRoleName().equals("BRAND")) {
                    var brand = sender.getBrand();
                    senderAddress = brand.getProvince() + " " + brand.getDistrict() + " " + brand.getWard() + " " + brand.getAddress();
                } else if (sender.getRoles().getRoleName().equals("CUSTOMER")) {
                    var customer = sender.getCustomer();
                    senderAddress = customer.getProvince() + " " + customer.getDistrict() + " " + customer.getWard() + " " + customer.getAddress();
                }
                var storedPayment = paymentRepository.save(
                        Payment.builder()
                                .paymentSender(null)
                                .paymentSenderName("")
                                .paymentSenderBankCode("")
                                .paymentSenderBankNumber("")

                                .paymentRecipient(null)
                                .paymentRecipientName("")
                                .paymentRecipientBankCode("")
                                .paymentRecipientBankNumber("")

                                .paymentMethod(paymentMethod)
                                .paymentAmount(paymentAmount)
                                .paymentStatus(paymentStatus)
                                .paymentType(paymentType)
//                                .order(paymentRequest.getOrder())
                                .order(null)
                                .paymentCode(null)
                                .build()
                );
                return paymentMapper.mapperToPaymentResponse(storedPayment);
            } else {

                /**
                 * TODO
                 * Load item from paymentRequest to itemList
                 */
                List<PayOSItem> itemList = paymentRequest.getItemList();
                String cancelUrl = "";
                String returnUrl = "";
                String description = "";
                var orderID = paymentRequest.getOrderID();

                if (paymentType.equals(PaymentType.DEPOSIT)) {
                    description = "DEPOSIT ORDER";
                } else if (paymentType.equals(PaymentType.STAGE_1)) {
                    description = "STAGE 1";
                } else {
                    description = "STAGE 2";
                }

                var creationPayOS = payOSService.createPaymentLink(
                        PayOSRequest
                                .builder()
                                .amount(10000)
                                .description(description)
                                .buyerName("")
                                .buyerEmail("")
                                .buyerPhone("")
                                .buyerAddress("")
                                .returnUrl(clientURL + "/order_detail/" + orderID)
                                .build()
                );

                logger.info("CREATE PayOS SUCCESSFULLY!");

                if (creationPayOS == null) {
                    throw new Exception("Create PayOS Fail!");
                }
                Integer orderCode = creationPayOS.getData().getOrderCode();

                var order = orderRepository.findById(orderID)
                        .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderID));

                var storedPayment = paymentRepository.save(
                        Payment.builder()
                                .paymentSender(null)
                                .paymentSenderName("")
                                .paymentSenderBankCode("")
                                .paymentSenderBankNumber("")

                                .paymentRecipient(null)
                                .paymentRecipientName("")
                                .paymentRecipientBankCode("")
                                .paymentRecipientBankNumber("")

                                .paymentMethod(paymentMethod)
                                .paymentAmount(paymentAmount)
                                .paymentStatus(paymentStatus)
                                .paymentType(paymentType)
                                .order(order)  // Use the retrieved order here
                                .paymentCode(orderCode)
                                .build()
                );

                return paymentMapper.mapperToPaymentResponse(storedPayment);
            }
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public PaymentResponse getPaymentByID(UUID paymentID) throws Exception {
        try {
            if (paymentID == null) {
                throw new Exception(MessageConstant.MISSING_ARGUMENT);
            }
            var checkPayment = paymentRepository.findByPaymentID(paymentID);
            if (checkPayment.isPresent()) {
                var payment = checkPayment.get();
                return paymentMapper.mapperToPaymentResponse(payment);
            }
            return null;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<Payment> findAllByOrderID(UUID orderID) {
        return paymentRepository.findAllByOrderID(orderID);
    }

    @Override
    public Payment updatePayment(Payment payment) {
        return paymentRepository.save(payment);
    }
}
