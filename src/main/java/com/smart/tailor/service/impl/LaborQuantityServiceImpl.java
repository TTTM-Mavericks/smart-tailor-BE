package com.smart.tailor.service.impl;

import com.smart.tailor.repository.LaborQuantityRepository;
import com.smart.tailor.service.LaborQuantityService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LaborQuantityServiceImpl implements LaborQuantityService {
    private final LaborQuantityRepository laborQuantityRepository;
    private final Logger logger = LoggerFactory.getLogger(LaborQuantityServiceImpl.class);
}
