package com.smart.tailor.service;

import com.smart.tailor.utils.response.ExpertTailoringResponse;
import com.smart.tailor.utils.response.MaterialResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface ExcelExportService {
//    void exportBrandMaterialData(List<BrandMaterialResponse> brandMaterialResponses, HttpServletResponse response) throws IOException;

    void exportExpertTailoringData(List<ExpertTailoringResponse> expertTailoringResponse, HttpServletResponse response) throws IOException;

    void exportCategoryMaterialForBrand(List<MaterialResponse> materialResponses, HttpServletResponse response) throws IOException;

    void exportSampleExpertTailoring(HttpServletResponse response) throws IOException;

    void exportSampleCategoryMaterial(HttpServletResponse response) throws IOException;
}
