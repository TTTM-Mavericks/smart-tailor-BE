package com.smart.tailor.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.entities.PayOSData;
import com.smart.tailor.mapper.PayOSDataMapper;
import com.smart.tailor.repository.PayOSDataRepository;
import com.smart.tailor.service.PayOSService;
import com.smart.tailor.utils.request.PayOSItem;
import com.smart.tailor.utils.request.PayOSRequest;
import com.smart.tailor.utils.response.PayOSResponse;
import com.smart.tailor.utils.response.PayOSResponseData;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class PayOSServiceImpl implements PayOSService {
    @Value("${PAYOS_CREATE_PAYMENT_LINK_URL}")
    private String createPaymentLinkUrl;
    @Value("${PAYOS_CLIENT_ID}")
    private String clientId;
    @Value("${PAYOS_API_KEY}")
    private String apiKey;
    @Value("${PAYOS_CHECKSUM_KEY}")
    private String checksumKey;
    private final Logger logger = LoggerFactory.getLogger(PayOSServiceImpl.class);
    private final PayOSDataRepository payOSRepository;
    private final PayOSDataMapper payOSDataMapper;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public PayOSResponse createPaymentLink(PayOSRequest paymentRequest) throws Exception {
        try {
            List<PayOSItem> items = paymentRequest.getItems();
            String cancelUrl = "";
            String returnUrl = "";

            Integer amount = paymentRequest.getAmount();

            logger.error("AMOUNT {}", amount);
//            if (items == null || items.size() <= 0) {
//                throw new Exception(MessageConstant.MISSING_ARGUMENT);
//            }
//            for (PayOSItem item : items) {
//                if (item == null) {
//                    throw new Exception(MessageConstant.MISSING_ARGUMENT);
//                }
//                if (item.getName() == null || item.getName().trim().isEmpty() || item.getName().trim().isBlank()) {
//                    throw new Exception(MessageConstant.MISSING_ARGUMENT);
//                }
//                String name = item.getName();
//
//                if (item.getQuantity() == null || item.getQuantity() <= 0) {
//                    item.setQuantity(1);
//                }
//                Integer quantity = item.getQuantity();
//
//                if (item.getPrice() == null || item.getPrice() < 0) {
//                    item.setPrice(0);
//                }
//                Integer price = item.getPrice();
//                amount += price * quantity;
//            }

            String currentTimeString = String.valueOf(LocalDateTime.now());
            Integer orderCode = Integer.parseInt(currentTimeString.substring(currentTimeString.length() - 6));
            logger.info("ORDER CODE: {}", orderCode);
//            cancelUrl = "https://be.mavericks-tttm.studio/payment-cancel";
            cancelUrl = paymentRequest.getCancelUrl();
//            returnUrl = "https://be.mavericks-tttm.studio/payment-infor?payos=1&item=" + items.get(0).getName().trim().toUpperCase();
            returnUrl = paymentRequest.getReturnUrl();
            String status = "PENDING";

            paymentRequest.setOrderCode(orderCode);
            paymentRequest.setAmount(amount);
            paymentRequest.setReturnUrl(returnUrl);
            paymentRequest.setCancelUrl(cancelUrl);
            paymentRequest.setDescription("PAYOS DESC");

            String bodyToSignature = createSignatureOfPaymentRequest(paymentRequest, checksumKey);
            paymentRequest.setSignature(bodyToSignature);

            // Tạo header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-client-id", clientId);
            headers.set("x-api-key", apiKey);
            // Gửi yêu cầu POST

            logger.error("BEFORE POST TO PAYOS");

            WebClient client = WebClient.create();
            Mono<String> response = client.post()
                    .uri(createPaymentLinkUrl)
                    .headers(httpHeaders -> httpHeaders.putAll(headers))
                    .body(BodyInserters.fromValue(paymentRequest))
                    .retrieve()
                    .bodyToMono(String.class);
            String responseBody = response.block();
            JsonNode res = objectMapper.readTree(responseBody);
//            logger.info("AFTER POST TO PAYOS: {}", res);
            System.out.println(res);
            if (!Objects.equals(res.get("code").asText(), "00")) {
                throw new Exception("Fail");
            }
            String bin = res.get("data").get("bin").asText();
            String accountNumber = res.get("data").get("accountNumber").asText();
            String accountName = res.get("data").get("accountName").asText();
            String description = "PayOS Description";
//            String description = res.get("data").get("description").asText();
            String currency = res.get("data").get("currency").asText();
            String paymentLinkId = res.get("data").get("paymentLinkId").asText();
            status = res.get("data").get("status").asText();
            String checkoutUrl = res.get("data").get("checkoutUrl").asText();
            String qrCode = res.get("data").get("qrCode").asText();

            //Kiểm tra dữ liệu có đúng không
            String paymentLinkResSignature = createSignatureFromObj(res.get("data"), checksumKey);
//    System.out.println("RES: " + res);
//    System.out.println(paymentLinkResSignature);
//            logger.info("Line Code 132 {}", paymentLinkResSignature);
            if (!paymentLinkResSignature.equals(res.get("signature").asText())) {
//      orderRepository.deleteOrderByOrderID(newestOrder.getId());
                throw new Exception("Signature is not compatible");
            }
            PayOSResponseData responseData = PayOSResponseData.builder()
                    .accountNumber(accountNumber)
                    .bin(bin)
                    .accountName(accountName)
                    .amount(amount)
                    .description(description)
                    .orderCode(orderCode)
                    .currency(currency)
                    .paymentLinkId(paymentLinkId)
                    .status(status)
                    .checkoutUrl(checkoutUrl)
                    .qrCode(qrCode)
                    .build();

            PayOSData payOSData = PayOSData.builder()
                    .accountNumber(accountNumber)
                    .bin(bin)
                    .accountName(accountName)
                    .amount(amount)
                    .description(description)
                    .orderCode(orderCode)
                    .currency(currency)
                    .paymentLinkId(paymentLinkId)
                    .status(status)
                    .checkoutUrl(checkoutUrl)
                    .qrCode(qrCode)
                    .build();
            payOSRepository.save(payOSData);

//            logger.info("Line Code 149 {}", responseData);
            PayOSResponse paymentResponse = PayOSResponse
                    .builder()
                    .code(res.get("code").asText())
                    .desc("Success - Thành công")
                    .data(responseData)
                    .signature(paymentRequest.getSignature())
                    .build();
            return paymentResponse;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public PayOSResponse getPaymentInfo(Integer paymentID) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", clientId);
        headers.set("x-api-key", apiKey);
        // Gửi yêu cầu POST
        WebClient client = WebClient.create();
        Mono<String> response = client.get()
                .uri(createPaymentLinkUrl + "/" + paymentID)
                .headers(httpHeaders -> httpHeaders.putAll(headers))
                .retrieve()
                .bodyToMono(String.class);
        String responseBody = response.block();
        JsonNode res = objectMapper.readTree(responseBody);
        System.out.println(paymentID);
        System.out.println(res);
        if (!Objects.equals(res.get("code").asText(), "00")) {
            logger.error("GET PAYOS RESPONSE FAIL!");
            return null;
        }

        String code = res.get("code").asText();
        String desc = res.get("desc").asText();
        String id = res.get("data").get("id").asText();
        Integer orderCode = Integer.parseInt(res.get("data").get("orderCode").asText());
        Integer amount = Integer.parseInt(res.get("data").get("amount").asText());
        Integer amountPaid = Integer.parseInt(res.get("data").get("amountPaid").asText());
        Integer amountRemaining = Integer.parseInt(res.get("data").get("amountRemaining").asText());
        String status = res.get("data").get("status").asText();
        String createdAt = res.get("data").get("createdAt").asText();
//        List<Transactions> transactions = new ArrayList<>();
//        JsonNode jsonArrayNode = res.get("data").get("transactions");
//        for (JsonNode jsonNode : jsonArrayNode) {
//            Transactions transactionsObject = objectMapper.convertValue(jsonNode, Transactions.class);
//            transactions.add(transactionsObject);
//        }
        String canceledAt = res.get("data").get("canceledAt").asText();
        String cancellationReason = res.get("data").get("cancellationReason").asText();
        String signature = res.get("signature").asText();
//
//        PayOSResponseData data = PayOSResponseData
//                .builder()
//                .bin()
//                .orderCode(orderCode)
//                .amount(amount)
//                .amountPaid(amountPaid)
//                .amountRemaining(amountRemaining)
//                .status(status)
//                .createdAt(createdAt)
//                .transactions(transactions)
//                .canceledAt(canceledAt)
//                .cancellationReason(cancellationReason)
//                .build();
//
//        Payment paymentObject = paymentRepository.getByPaymentID(orderCode);
//
//        PaymentInformation paymentInformation = PaymentInformation
//                .builder()
//                .code(code)
//                .desc(desc)
//                .data(data)
//                .signature(signature)
//                .checkoutUrl(paymentObject.getCheckoutUrl())
//                .qrCode(paymentObject.getQrCode())
//                .build();
//        return paymentInformation;
        System.out.println("CODE IS: " + orderCode);
        var payOSData = payOSRepository.findById(Integer.valueOf(orderCode));
        if (payOSData.isEmpty()) {
            System.out.println("CAN NOT FIND BY CODE: "+ orderCode);
            return null;
        }
        PayOSResponse payOSResponse = PayOSResponse
                .builder()
                .code(code)
                .data(
                        payOSDataMapper.mapToPayOSResponseData(payOSData.get())
                )
                .signature(signature)
                .desc(desc)
                .build();
        System.out.println(payOSResponse);
        logger.info("PAYOS RESPONSE {}", payOSResponse);
        return payOSResponse;
    }


    private static String convertObjToQueryStr(JsonNode object) {
        StringBuilder stringBuilder = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();

        object.fields().forEachRemaining(entry -> {
            String key = entry.getKey();
            JsonNode value = entry.getValue();
            String valueAsString = value.isTextual() ? value.asText() : value.toString();

            if (!stringBuilder.isEmpty()) {
                stringBuilder.append('&');
            }
            stringBuilder.append(key).append('=').append(valueAsString);
        });

        return stringBuilder.toString();
    }

    private static JsonNode sortObjDataByKey(JsonNode object) {
        if (!object.isObject()) {
            return object;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode orderedObject = objectMapper.createObjectNode();

        Iterator<Entry<String, JsonNode>> fieldsIterator = object.fields();
        TreeMap<String, JsonNode> sortedMap = new TreeMap<>();

        while (fieldsIterator.hasNext()) {
            Entry<String, JsonNode> field = fieldsIterator.next();
            sortedMap.put(field.getKey(), field.getValue());
        }

        sortedMap.forEach(orderedObject::set);

        return orderedObject;
    }

    private static String generateHmacSHA256(String dataStr, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256Hmac.init(secretKey);
        byte[] hmacBytes = sha256Hmac.doFinal(dataStr.getBytes(StandardCharsets.UTF_8));

        // Chuyển byte array sang chuỗi hex
        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte b : hmacBytes) {
            hexStringBuilder.append(String.format("%02x", b));
        }
        return hexStringBuilder.toString();
    }

    public static String createSignatureFromObj(JsonNode data, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        JsonNode sortedDataByKey = sortObjDataByKey(data);
        String dataQueryStr = convertObjToQueryStr(sortedDataByKey);
        return generateHmacSHA256(dataQueryStr, key);
    }

    public static String createSignatureOfPaymentRequest(PayOSRequest data, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        int amount = data.getAmount();
        String cancelUrl = data.getCancelUrl();
        String description = data.getDescription();
        int orderCode = data.getOrderCode();
        String returnUrl = data.getReturnUrl();
        String dataStr = "amount=" + amount + "&cancelUrl=" + cancelUrl + "&description=" + description
                + "&orderCode=" + orderCode + "&returnUrl=" + returnUrl;
        // Sử dụng HMAC-SHA-256 để tính toán chữ ký
        return generateHmacSHA256(dataStr, key);
    }
}