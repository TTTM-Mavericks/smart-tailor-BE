package com.smart.tailor.service;

import com.smart.tailor.utils.request.SizeExpertTailoringRequest;
import com.smart.tailor.utils.response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface ExcelImportService {
    boolean isValidExcelFile(MultipartFile file);

    APIResponse getBrandMaterialDataFromExcel(InputStream inputStream, String brandName);

    APIResponse getCategoryMaterialDataFromExcel(InputStream inputStream);

    APIResponse getExpertTailoringDataFromExcel(InputStream inputStream);
}
