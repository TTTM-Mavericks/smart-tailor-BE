package com.smart.tailor.service;

import com.smart.tailor.entities.ExpertTailoringMaterial;
import com.smart.tailor.utils.request.ExpertTailoringMaterialListRequest;
import com.smart.tailor.utils.response.ExpertTailoringMaterialResponse;
import com.smart.tailor.utils.response.MaterialResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpertTailoringMaterialService {
    void createExpertTailoringMaterial(ExpertTailoringMaterialListRequest expertTailoringMaterialListRequest);

    void changeStatusExpertTailoringMaterial(UUID expertTailoring, UUID materialID);

    Optional<ExpertTailoringMaterial> findByExpertTailoringExpertTailoringIDAndMaterialMaterialID(UUID expertTailoringID, UUID materialID);

    List<ExpertTailoringMaterialResponse> findAllExpertTailoringMaterial();

    List<ExpertTailoringMaterialResponse> findAllActiveExpertTailoringMaterialByExpertTailoringID(UUID expertTailoringID);

    List<ExpertTailoringMaterialResponse> findAllActiveExpertTailoringMaterialByExpertTailoringName(String expertTailoringName);

    void generateSampleExpertTailoringMaterial(HttpServletResponse response) throws IOException;

    void createExpertTailoringMaterialByExcelFile(MultipartFile file);
}