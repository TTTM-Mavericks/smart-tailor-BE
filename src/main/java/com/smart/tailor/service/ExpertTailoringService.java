package com.smart.tailor.service;

import com.smart.tailor.config.CustomExeption;
import com.smart.tailor.entities.ExpertTailoring;
import com.smart.tailor.utils.request.ExpertTailoringRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.ExpertTailoringResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpertTailoringService {
    Optional<ExpertTailoring> getExpertTailoringByID(UUID expectID) throws CustomExeption;

    APIResponse createExpertTailoring(ExpertTailoringRequest expertTailoringRequest);

    ExpertTailoringResponse mapperToExpertTailoringResponse(ExpertTailoring expertTailoring);

    List<ExpertTailoringResponse> getAllExpertTailoring();

    ExpertTailoringResponse getByExpertTailoringName(String expertTailoringName);

    APIResponse createExpertTailoringByExcelFile(MultipartFile file);

    List<ExpertTailoringResponse> getAllExpertTailoringByExportExcelData(HttpServletResponse response) throws IOException;
}
