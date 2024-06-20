package com.smart.tailor.service.impl;

import com.smart.tailor.config.CustomExeption;
import com.smart.tailor.constant.ErrorConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Category;
import com.smart.tailor.entities.ExpertTailoring;
import com.smart.tailor.entities.Material;
import com.smart.tailor.mapper.ExpertTailoringMapper;
import com.smart.tailor.repository.ExpertTailoringRepository;
import com.smart.tailor.service.ExcelExportService;
import com.smart.tailor.service.ExcelImportService;
import com.smart.tailor.service.ExpertTailoringService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.ExpertTailoringRequest;
import com.smart.tailor.utils.request.MaterialRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.ExpertTailoringResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpertTailoringServiceImpl implements ExpertTailoringService {
    private final ExpertTailoringRepository expertTailoringRepository;
    private final Logger logger = LoggerFactory.getLogger(ExpertTailoringServiceImpl.class);
    private final ExpertTailoringMapper expertTailoringMapper;
    private final ExcelExportService excelExportService;
    private final ExcelImportService excelImportService;

    @Override
    public Optional<ExpertTailoring> getExpertTailoringByID(UUID expectID) throws CustomExeption {
        try {
            if (expectID == null) {
                throw new CustomExeption(ErrorConstant.MISSING_ARGUMENT);
            }
            var expectTailoring = expertTailoringRepository.findExpertTailoringByExpertTailoringID(expectID);
            if (expectTailoring.isEmpty()) {
                throw new CustomExeption(ErrorConstant.BAD_REQUEST);
            }
            return expectTailoring;
        } catch (Exception ex) {
            logger.error("ERROR IN EXPECT TAILORING SERVICE - GET EXPECT TAILORING BY ID: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    @Transactional
    public APIResponse createExpertTailoring(ExpertTailoringRequest expertTailoringRequest) {
        if (!Utilities.isStringNotNullOrEmpty(expertTailoringRequest.getExpertTailoringName()) ||
                !Utilities.isStringNotNullOrEmpty(expertTailoringRequest.getSizeImageUrl())) {
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.MISSING_ARGUMENT)
                    .build();
        }

        var expertTailoringExisted = getByExpertTailoringName(expertTailoringRequest.getExpertTailoringName());
        if (expertTailoringExisted != null) {
            return APIResponse
                    .builder()
                    .status(HttpStatus.CONFLICT.value())
                    .message(MessageConstant.EXPERT_TAILORING_IS_EXISTED)
                    .build();
        }

        ExpertTailoring expertTailoring = expertTailoringRepository.save(
                ExpertTailoring.builder()
                        .expertTailoringName(expertTailoringRequest.getExpertTailoringName())
                        .sizeImageUrl(expertTailoringRequest.getSizeImageUrl())
                        .status(true)
                        .build()
        );

        return APIResponse.builder()
                .status(HttpStatus.OK.value())
                .message(MessageConstant.ADD_NEW_EXPERT_TAILORING_SUCCESSFULLY)
                .data(mapperToExpertTailoringResponse(expertTailoring))
                .build();
    }


    @Override
    public ExpertTailoringResponse mapperToExpertTailoringResponse(ExpertTailoring expertTailoring) {
        return expertTailoringMapper.mapperToExpertTailoringResponse(expertTailoring);
    }

    @Override
    public List<ExpertTailoringResponse> getAllExpertTailoring() {
        return expertTailoringRepository
                .findAll()
                .stream()
                .map(this::mapperToExpertTailoringResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ExpertTailoringResponse getByExpertTailoringName(String expertTailoringName) {
        var expertTailroingOptional = expertTailoringRepository.findByExpertTailoringName(expertTailoringName);
        if (expertTailroingOptional.isPresent()) {
            return mapperToExpertTailoringResponse(expertTailroingOptional.get());
        }
        return null;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public APIResponse createExpertTailoringByExcelFile(MultipartFile file) {
        if (!excelImportService.isValidExcelFile(file)) {
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_EXCEL_FILE_FORMAT)
                    .data(null)
                    .build();
        }
        try {
            var apiResponse = excelImportService.getExpertTailoringDataFromExcel(file.getInputStream());

            if(apiResponse.getStatus() == HttpStatus.BAD_REQUEST.value() ||
                    apiResponse.getStatus() == HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()){
                return apiResponse;
            }

            var excelData = (List<ExpertTailoringRequest>) apiResponse.getData();

            if(excelData.isEmpty()){
                return APIResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message(MessageConstant.CATEGORY_AND_MATERIAL_EXCEL_FILE_HAS_EMPTY_DATA)
                        .data(null)
                        .build();
            }

            Set<ExpertTailoringRequest> excelNames = new HashSet<>();
            List<ExpertTailoringRequest> uniqueExcelData = new ArrayList<>();
            List<ExpertTailoringRequest> duplicateExcelData = new ArrayList<>();

            for(ExpertTailoringRequest request : excelData){
                if(!excelNames.add(request)){excelData.add(request);
                } else {
                    uniqueExcelData.add(request);
                }
            }

            if (!duplicateExcelData.isEmpty()) {
                return APIResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(MessageConstant.DUPLICATE_EXPERT_TAILORING_IN_EXCEL_FILE)
                        .data(duplicateExcelData)
                        .build();
            }

            List<ExpertTailoringRequest> validData = new ArrayList<>();
            List<ExpertTailoringRequest> invalidData = new ArrayList<>();
            for (ExpertTailoringRequest expertTailoringRequest : uniqueExcelData) {
                var saveExpertTailoringResponse = createExpertTailoring(expertTailoringRequest);
                validData.add(expertTailoringRequest);
                if (saveExpertTailoringResponse.getStatus() != HttpStatus.OK.value()) {
                    invalidData.add(expertTailoringRequest);
                }
            }

            if (invalidData.isEmpty()) {
                return APIResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message(MessageConstant.ADD_NEW_EXPERT_TAILORING_BY_EXCEL_FILE_SUCCESSFULLY)
                        .data(validData)
                        .build();
            } else {
                return APIResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(MessageConstant.DUPLICATE_EXPERT_TAILORING_DATA)
                        .data(invalidData)
                        .build();
            }
        } catch (IOException ex) {
            logger.error("Error processing excel file", ex);
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_EXCEL_FILE_FORMAT)
                    .data(null)
                    .build();
        }
    }

    @Override
    public List<ExpertTailoringResponse> getAllExpertTailoringByExportExcelData(HttpServletResponse response) throws IOException {
        var expertTailoringResponse = getAllExpertTailoring();
        excelExportService.exportExpertTailoringData(expertTailoringResponse, response);
        return expertTailoringResponse;
    }

    @Override
    public void generateSampleExpertTailoringByExportExcel(HttpServletResponse response) throws IOException {
        excelExportService.exportSampleExpertTailoring(response);
    }

    @Override
    public ExpertTailoringResponse findByExpertTailoringID(UUID expertTailoringID) {
        if(Utilities.isStringNotNullOrEmpty(expertTailoringID.toString())){
            var expertTailoring = expertTailoringRepository.findByExpertTailoringID(expertTailoringID);
            if(expertTailoring.isPresent()){
                return expertTailoringMapper.mapperToExpertTailoringResponse(expertTailoring.get());
            }
        }
        return null;
    }

    @Override
    public APIResponse updateExpertTailoring(UUID expertTailoringID, ExpertTailoringRequest expertTailoringRequest) {
        if(
                !Utilities.isStringNotNullOrEmpty(expertTailoringID.toString()) ||
                !Utilities.isStringNotNullOrEmpty(expertTailoringRequest.getExpertTailoringName()) ||
                !Utilities.isStringNotNullOrEmpty(expertTailoringRequest.getSizeImageUrl())
        ){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.DATA_IS_EMPTY)
                    .build();
        }

        var expertTailoring = expertTailoringRepository.findByExpertTailoringID(expertTailoringID);
        if(expertTailoring.isEmpty()){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING)
                    .build();
        }

        var checkExpertTailoringNameIsExisted = getByExpertTailoringName(expertTailoringRequest.getExpertTailoringName());
        if(checkExpertTailoringNameIsExisted != null){
            System.out.println(checkExpertTailoringNameIsExisted.getExpertTailoringID());
            System.out.println(expertTailoring.get().getExpertTailoringID());
            if(!checkExpertTailoringNameIsExisted.getExpertTailoringID().toString().equals(expertTailoring.get().getExpertTailoringID().toString())){
                return APIResponse
                        .builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(MessageConstant.EXPERT_TAILORING_NAME_IS_EXISTED)
                        .build();
            }
        }

        var updateExpertTailoring = expertTailoringRepository.save(
                ExpertTailoring
                        .builder()
                        .expertTailoringID(expertTailoringID)
                        .expertTailoringName(expertTailoringRequest.getExpertTailoringName())
                        .sizeImageUrl(expertTailoringRequest.getSizeImageUrl())
                        .status(expertTailoring.get().getStatus())
                        .build()
        );

        return APIResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(MessageConstant.UPDATE_MATERIAL_SUCCESSFULLY)
                .data(expertTailoringMapper.mapperToExpertTailoringResponse(updateExpertTailoring))
                .build();
    }

    @Override
    public APIResponse updateStatusExpertTailoring(UUID expertTailoringID) {
        if(!Utilities.isStringNotNullOrEmpty(expertTailoringID.toString())){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.DATA_IS_EMPTY)
                    .build();
        }

        var expertTailoring = expertTailoringRepository.findByExpertTailoringID(expertTailoringID);
        if(expertTailoring.isEmpty()){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING)
                    .build();
        }

        expertTailoring.get().setStatus(expertTailoring.get().getStatus() ? false : true);

        return APIResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(MessageConstant.UPDATE_EXPERT_TAILORING_SUCCESSFULLY)
                .data(expertTailoringMapper.mapperToExpertTailoringResponse(expertTailoringRepository.save(expertTailoring.get())))
                .build();
    }
}
