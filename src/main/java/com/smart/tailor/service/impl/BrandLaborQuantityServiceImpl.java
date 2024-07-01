package com.smart.tailor.service.impl;

import com.smart.tailor.mapper.BrandLaborQuantityMapper;
import com.smart.tailor.repository.BrandExpertTailoringRepository;
import com.smart.tailor.service.BrandLaborQuantityService;
import com.smart.tailor.service.LaborQuantityService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandLaborQuantityServiceImpl implements BrandLaborQuantityService {
    private final BrandExpertTailoringRepository brandExpertTailoringRepository;
    private final LaborQuantityService laborQuantityService;
    private final Logger logger = LoggerFactory.getLogger(BrandLaborQuantityServiceImpl.class);
}
