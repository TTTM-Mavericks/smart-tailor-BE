package com.smart.tailor.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.tailor.service.GHTKShippingService;
import com.smart.tailor.utils.request.OrderShippingRequest;
import com.smart.tailor.utils.response.FeeResponse;
import com.smart.tailor.utils.response.OrderShippingResponse;
import com.smart.tailor.utils.response.VietQRResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class GHTKShippingServiceImpl implements GHTKShippingService {
    @Value("${GHTK_SHIPPING_API_URL}")
    private String shippingApiUrl;

    @Value("${GHTK_SHPPING_API_TOKEN_KEY}")
    private String shippingApiTokenKey;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final Logger logger = LoggerFactory.getLogger(GHTKShippingServiceImpl.class);

    @Override
    public OrderShippingResponse createShippingOrder(OrderShippingRequest orderShippingRequest) {

        return null;
    }

    @Override
    public FeeResponse calculateShippingFee(OrderShippingRequest orderShippingRequest) {
        try {
            shippingApiUrl += "/fee";

            // Build the URI with parameters
            shippingApiUrl = UriComponentsBuilder
                    .fromHttpUrl(shippingApiUrl)
                    .queryParam("pick_address", orderShippingRequest.getOrder().getPick_address())
                    .queryParam("pick_province", orderShippingRequest.getOrder().getProvince())
                    .queryParam("pick_district", orderShippingRequest.getOrder().getPick_district())
                    .queryParam("pick_ward", orderShippingRequest.getOrder().getPick_ward())
                    .queryParam("address", orderShippingRequest.getOrder().getAddress())
                    .queryParam("province", orderShippingRequest.getOrder().getProvince())
                    .queryParam("district", orderShippingRequest.getOrder().getDistrict())
                    .queryParam("ward", orderShippingRequest.getOrder().getWard())
                    .queryParam("weight", orderShippingRequest.getOrder().getWeight() * 1000)
                    .build()
                    .toUriString();

            logger.info("Shipping API Token {}", shippingApiTokenKey);
            logger.info("Shipping API Url {}", shippingApiUrl);

            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Token", shippingApiTokenKey);

            WebClient client = WebClient.create();
            Mono<String> response = client
                    .get()
                    .uri(shippingApiUrl)
                    .headers(httpHeaders -> httpHeaders.putAll(headers))
                    .retrieve()
                    .bodyToMono(String.class)
//                    .timeout(Duration.ofSeconds(10))
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))) // Retry up to 3 times with exponential backoff
                    .onErrorResume(error -> {
                        logger.error("Error after retries: {}", error.getMessage());
                        return Mono.empty();
                    });

            String responseBody = response.block();
            if (responseBody == null) {
                throw new Exception("Failed to get response from the service");
            }

            JsonNode res = objectMapper.readTree(responseBody);

            logger.info("Response Body From Request URL {}", responseBody);
            if (!Objects.equals(res.get("success").asText(), "true")) {
                throw new Exception("Fail");
            }

            return FeeResponse.builder()
                    .success(Boolean.parseBoolean(res.get("success").asText()))
                    .fee(Integer.parseInt(res.get("fee").get("fee").asText()))
                    .message(res.get("message").asText())
                    .build();

        } catch (Exception ex) {
            logger.error("Exception occurred: {}", ex.getMessage());
            return null;
        }
    }

}
