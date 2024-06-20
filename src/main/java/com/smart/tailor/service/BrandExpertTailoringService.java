package com.smart.tailor.service;

import java.util.UUID;

public interface BrandExpertTailoringService {
    Boolean addExpertTailoringForBrand(UUID brandID, UUID expertTailoring) throws Exception;
}
