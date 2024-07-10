package com.smart.tailor.service;

import com.smart.tailor.utils.request.SizeExpertTailoringRequest;
import com.smart.tailor.utils.response.SizeExpertTailoringResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface SizeExpertTailoringService {
    void createSizeExpertTailoring(SizeExpertTailoringRequest sizeExpertTailoringRequest);

    List<SizeExpertTailoringResponse> findAllSizeExpertTailoring();

    List<SizeExpertTailoringResponse> findAllSizeExpertTailoringID(UUID expectTailoringID);

    void updateSizeExpertTailoring(SizeExpertTailoringRequest sizeExpertTailoringRequest);

    void createSizeExpertTailoringByExcelFile(MultipartFile file);

    void generateSampleSizeExpertTailoringByExcelFile(HttpServletResponse response) throws IOException;
}
