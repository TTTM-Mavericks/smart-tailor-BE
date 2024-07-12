package com.smart.tailor.service;

import com.smart.tailor.utils.request.*;
import com.smart.tailor.utils.response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface ExcelImportService {
    boolean isValidExcelFile(MultipartFile file);

    List<BrandMaterialRequest> getBrandMaterialDataFromExcel(InputStream inputStream, String brandName);

    List<MaterialRequest> getCategoryMaterialDataFromExcel(InputStream inputStream);

    List<ExpertTailoringRequest> getExpertTailoringDataFromExcel(InputStream inputStream);

    List<SizeExpertTailoringRequest> getSizeExpertTailoringRequestFromExcel(InputStream inputStream);

    List<ExpertTailoringMaterialListRequest> getExpertTailoringMaterialDataFromExcel(InputStream inputStream);
}
