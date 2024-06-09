package com.smart.tailor.service;

import com.smart.tailor.utils.response.BrandMaterialResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface ExcelExportService {
    void exportBrandMaterialData(List<BrandMaterialResponse> brandMaterialResponses, HttpServletResponse response) throws IOException;
}
