package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.ExpertTailoring;
import com.smart.tailor.mapper.ExpertTailoringMapper;
import com.smart.tailor.repository.ExpertTailoringRepository;
import com.smart.tailor.service.ExcelExportService;
import com.smart.tailor.service.ExcelImportService;
import com.smart.tailor.service.ExpertTailoringService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.ExpertTailoringRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.ExpertTailoringResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    public APIResponse createExpertTailoring(ExpertTailoringRequest expertTailoringRequest) {
        if(
                !Utilities.isStringNotNullOrEmpty(expertTailoringRequest.getExpertTailoringName()) ||
                !Utilities.isStringNotNullOrEmpty(expertTailoringRequest.getSizeImageUrl())
        ) {
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.MISSING_ARGUMENT)
                    .build();
        }

        var expertTailoringExisted = getByExpertTailoringName(expertTailoringRequest.getExpertTailoringName());
        if(expertTailoringExisted != null){
            return APIResponse
                    .builder()
                    .status(HttpStatus.CONFLICT.value())
                    .message(MessageConstant.EXPERT_TAILORING_IS_EXISTED)
                    .build();
        }

        ExpertTailoring expertTailoring = expertTailoringRepository.save(
                ExpertTailoring
                        .builder()
                        .expertTailoringName(expertTailoringRequest.getExpertTailoringName())
                        .sizeImageUrl(expertTailoringRequest.getSizeImageUrl())
                        .build()
        );
        return APIResponse
                .builder()
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
        if(expertTailroingOptional.isPresent()){
            return mapperToExpertTailoringResponse(expertTailroingOptional.get());
        }
        return null;
    }

    @Override
    public APIResponse createExpertTailoringByExcelFile(MultipartFile file) {
        List<ExpertTailoringRequest> invalidData = new ArrayList<>();
        if (excelImportService.isValidExcelFile(file)) {
            try {
                var expertTailoringRequests = excelImportService.getExpertTailoringDataFromExcel(file.getInputStream());
                for(var expertTailoringRequest : expertTailoringRequests){
                    logger.info("Inside list Brand Material Request {}", expertTailoringRequest);
                    var response = createExpertTailoring(expertTailoringRequest);
                    if(response.getStatus() != HttpStatus.OK.value()){
                        invalidData.add(expertTailoringRequest);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                return APIResponse
                        .builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(MessageConstant.INVALID_EXCEL_FILE_FORMAT)
                        .data(null)
                        .build();
            }
        }
        if(invalidData.isEmpty()){
            return APIResponse
                    .builder()
                    .status(HttpStatus.OK.value())
                    .message(MessageConstant.ADD_NEW_EXPERT_TAILORING_BY_EXCEL_FILE_SUCCESSFULLY)
                    .data(null)
                    .build();
        }
        else{
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.ADD_NEW_EXPERT_TAILORING_BY_EXCEL_FILE_FAIL)
                    .data("Error Data : " + invalidData)
                    .build();
        }
    }

    @Override
    public List<ExpertTailoringResponse> getAllExpertTailoringByExportExcelData(HttpServletResponse response) throws IOException {
        var expertTailoringResponse = getAllExpertTailoring();
        excelExportService.exportExpertTailoringData(expertTailoringResponse, response);
        return expertTailoringResponse;
    }
}
