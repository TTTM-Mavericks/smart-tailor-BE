package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.ExpertTailoringMaterial;
import com.smart.tailor.entities.ExpertTailoringMaterialKey;
import com.smart.tailor.exception.*;
import com.smart.tailor.mapper.ExpertTailoringMaterialMapper;
import com.smart.tailor.repository.ExpertTailoringMaterialRepository;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.request.ExpertTailoringMaterialListRequest;
import com.smart.tailor.utils.request.ExpertTailoringMaterialRequest;
import com.smart.tailor.utils.request.MaterialRequest;
import com.smart.tailor.utils.response.ErrorData;
import com.smart.tailor.utils.response.ExpertTailoringMaterialResponse;
import com.smart.tailor.utils.response.MaterialResponse;
import jakarta.persistence.Tuple;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpertTailoringMaterialServiceImpl implements ExpertTailoringMaterialService {
    private final ExpertTailoringMaterialRepository expertTailoringMaterialRepository;
    private final ExpertTailoringService expertTailoringService;
    private final MaterialService materialService;
    private final ExpertTailoringMaterialMapper expertTailoringMaterialMapper;
    private final ExcelExportService excelExportService;
    private final ExcelImportService excelImportService;
    private final Logger logger = LoggerFactory.getLogger(ExpertTailoringMaterialServiceImpl.class);

    @Transactional
    @Override
    public void createExpertTailoringMaterial(ExpertTailoringMaterialListRequest expertTailoringMaterialListRequest) {
        String categoryName = expertTailoringMaterialListRequest.getCategoryName();
        String materialName = expertTailoringMaterialListRequest.getMaterialName();

        var material = materialService.findByMaterialNameAndCategory_CategoryName(
                        materialName, categoryName)
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_MATERIAL));

        List<Object> duplicateExpertTailoringMaterials = new ArrayList<>();

        for(String expertTailoringName : expertTailoringMaterialListRequest.getExpertTailoringNames()){
            var expertTailoring = expertTailoringService.getExpertTailoringByExpertTailoringName(expertTailoringName)
                    .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING));

            var expertTailoringMaterialExisted = findByExpertTailoringExpertTailoringIDAndMaterialMaterialID(
                    expertTailoring.getExpertTailoringID(), material.getMaterialID());

            if(expertTailoringMaterialExisted.isPresent()){
                duplicateExpertTailoringMaterials.add(
                        ExpertTailoringMaterialRequest
                                .builder()
                                .materialName(materialName)
                                .categoryName(categoryName)
                                .expertTailoringName(expertTailoringName)
                                .build()
                );
                continue;
            }
            if (!duplicateExpertTailoringMaterials.isEmpty()) continue;

            ExpertTailoringMaterialKey expertTailoringMaterialKey = ExpertTailoringMaterialKey
                    .builder()
                    .expertTailoringID(expertTailoring.getExpertTailoringID())
                    .materialID(material.getMaterialID())
                    .build();

            ExpertTailoringMaterial expertTailoringMaterial = ExpertTailoringMaterial
                    .builder()
                    .expertTailoringMaterialKey(expertTailoringMaterialKey)
                    .expertTailoring(expertTailoring)
                    .material(material)
                    .status(true)
                    .build();

            expertTailoringMaterialRepository.save(expertTailoringMaterial);
        }

        if(!duplicateExpertTailoringMaterials.isEmpty()){
            throw new DuplicateDataException(MessageConstant.EXPERT_TAILORING_MATERIAL_IS_EXISTED, duplicateExpertTailoringMaterials);
        }
    }

    @Override
    public Optional<ExpertTailoringMaterial> findByExpertTailoringExpertTailoringIDAndMaterialMaterialID(UUID expertTailoringID, UUID materialID) {
        return expertTailoringMaterialRepository.findByExpertTailoringExpertTailoringIDAndMaterialMaterialID(expertTailoringID, materialID);
    }

    @Transactional
    @Override
    public void changeStatusExpertTailoringMaterial(UUID expertTailoringID, UUID materialID) {
        var material = materialService.findMaterialByID(materialID)
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_MATERIAL));

        var expertTailoring = expertTailoringService.findExpertTailoringByID(expertTailoringID)
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING));

        var expertTailoringMaterialExisted = findByExpertTailoringExpertTailoringIDAndMaterialMaterialID(
                expertTailoring.getExpertTailoringID(), material.getMaterialID())
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING_MATERIAL));

        expertTailoringMaterialExisted.setStatus(!expertTailoringMaterialExisted.getStatus());
        expertTailoringMaterialRepository.save(expertTailoringMaterialExisted);
    }

    @Override
    public List<ExpertTailoringMaterialResponse> findAllExpertTailoringMaterial() {
        return expertTailoringMaterialRepository
                .findAll()
                .stream()
                .map(expertTailoringMaterialMapper::mapperToExpertTailoringMaterialResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExpertTailoringMaterialResponse> findAllActiveExpertTailoringMaterialByExpertTailoringID(UUID expertTailoringID) {
        return expertTailoringMaterialRepository
                .findAll()
                .stream()
                .filter(expertTailoringMaterial ->
                        expertTailoringMaterial.getExpertTailoringMaterialKey().getExpertTailoringID().toString().equals(expertTailoringID.toString()) &&
                        expertTailoringMaterial.getStatus())
                .map(expertTailoringMaterialMapper::mapperToExpertTailoringMaterialResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExpertTailoringMaterialResponse> findAllActiveExpertTailoringMaterialByExpertTailoringName(String expertTailoringName) {
        return expertTailoringMaterialRepository
                .findAll()
                .stream()
                .filter(expertTailoringMaterial ->
                        expertTailoringMaterial.getExpertTailoring().getExpertTailoringName().equalsIgnoreCase(expertTailoringName) &&
                        expertTailoringMaterial.getStatus())
                .map(expertTailoringMaterialMapper::mapperToExpertTailoringMaterialResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void generateSampleExpertTailoringMaterial(HttpServletResponse response) throws IOException {
        var materialResponse = materialService.findAllActiveMaterials();
        excelExportService.exportSampleExpertTailoringMaterial(response, materialResponse);
    }

    @Transactional
    @Override
    public void createExpertTailoringMaterialByExcelFile(MultipartFile file) {
        if (!excelImportService.isValidExcelFile(file)) {
            throw new ExcelFileInvalidFormatException(MessageConstant.INVALID_EXCEL_FILE_FORMAT);
        }
        try {
            var excelData = excelImportService.getExpertTailoringMaterialDataFromExcel(file.getInputStream());

            if (excelData.isEmpty()) {
                throw new BadRequestException("Category and Material Excel File Has Empty Data");
            }

            Set<ExpertTailoringMaterialListRequest> excelNames = new HashSet<>();
            List<ExpertTailoringMaterialListRequest> uniqueExcelData = new ArrayList<>();
            List<Object> duplicateExcelData = new ArrayList<>();

            for (ExpertTailoringMaterialListRequest request : excelData) {
                if (!excelNames.add(request)) {
                    duplicateExcelData.add(request);
                } else {
                    uniqueExcelData.add(request);
                }
            }

            if (!duplicateExcelData.isEmpty()) {
                throw new ExcelFileDuplicateDataException(MessageConstant.DUPLICATE_EXPERT_TAILORING_MATERIAL_IN_EXCEL_FILE, duplicateExcelData);
            }

            List<Object> invalidData = new ArrayList<>();
            for (ExpertTailoringMaterialListRequest materialRequest : uniqueExcelData) {
                try {
                    createExpertTailoringMaterial(materialRequest);
                } catch (ItemNotFoundException ex) {
                    String errorMessage = ex.getMessage() != null ? ex.getMessage() : MessageConstant.CAN_NOT_FIND_ANY_MATERIAL;
                    logger.error("Error creating Expert Tailoring Material: Item not found - {}", errorMessage, ex);
                    invalidData.add(new ErrorData(materialRequest, errorMessage));
                } catch (DuplicateDataException ex) {
                    String errorMessage = ex.getMessage() != null ? ex.getMessage() : MessageConstant.EXPERT_TAILORING_MATERIAL_IS_EXISTED;
                    logger.error("Error creating Expert Tailoring Material: Already exists - {}", errorMessage, ex);
                    invalidData.add(new ErrorData(ex.getErrors(), errorMessage));
                } catch (Exception ex) {
                    logger.error("Error creating Expert Tailoring Material - {}", ex.getMessage());
                    invalidData.add(new ErrorData(materialRequest, ex.getMessage()));
                }
            }

            if (!invalidData.isEmpty()) {
                throw new ExcelFileInvalidDataTypeException("Some Data could not be processed correctly", invalidData);
            }
        } catch (IOException ex) {
            logger.error("Error processing excel file", ex);
            throw new ExcelFileInvalidFormatException(MessageConstant.INVALID_EXCEL_FILE_FORMAT);
        }
    }
}
