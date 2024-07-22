package com.smart.tailor.service.impl;

import com.smart.tailor.entities.PayOSData;
import com.smart.tailor.repository.PayOSDataRepository;
import com.smart.tailor.service.PayOSDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PayOSDataServiceImpl implements PayOSDataService {
    private final PayOSDataRepository payOSDataRepository;

    @Transactional
    @Override
    public void save(PayOSData payOSData) {
        payOSDataRepository.save(payOSData);
    }

    @Override
    public Optional<PayOSData> findByOrderCode(Integer id) {
        return payOSDataRepository.findByOrderCode(id);
    }
}
