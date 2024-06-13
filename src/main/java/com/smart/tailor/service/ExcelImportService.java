package com.smart.tailor.service;

import com.smart.tailor.utils.request.BrandMaterialRequest;
import com.smart.tailor.utils.request.ExpertTailoringRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface ExcelImportService {
    boolean isValidExcelFile(MultipartFile file);

    List<BrandMaterialRequest> getBrandMaterialDataFromExcel(InputStream inputStream, String brandName);

    List<ExpertTailoringRequest> getExpertTailoringDataFromExcel(InputStream inputStream);
}
