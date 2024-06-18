package com.smart.tailor.service;

import com.smart.tailor.entities.ExpertTailoring;
import com.smart.tailor.utils.request.ExpertTailoringRequest;
import com.smart.tailor.utils.request.MaterialRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.BrandMaterialResponse;
import com.smart.tailor.utils.response.ExpertTailoringResponse;
import com.smart.tailor.utils.response.MaterialResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ExpertTailoringService {
    APIResponse createExpertTailoring(ExpertTailoringRequest expertTailoringRequest);

    ExpertTailoringResponse mapperToExpertTailoringResponse(ExpertTailoring expertTailoring);

    List<ExpertTailoringResponse> getAllExpertTailoring();

    ExpertTailoringResponse getByExpertTailoringName(String expertTailoringName);

    APIResponse createExpertTailoringByExcelFile(MultipartFile file);

    List<ExpertTailoringResponse> getAllExpertTailoringByExportExcelData(HttpServletResponse response) throws IOException;

    void generateSampleExpertTailoringByExportExcel(HttpServletResponse response) throws IOException;

    ExpertTailoringResponse findByExpertTailoringID(UUID expertTailoringID);

    APIResponse updateExpertTailoring(UUID expertTailoringID, ExpertTailoringRequest expertTailoringRequest);

    APIResponse updateStatusExpertTailoring(UUID expertTailoringID);
}
