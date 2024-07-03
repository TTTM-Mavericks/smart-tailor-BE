package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.SizeExpertTailoring;
import com.smart.tailor.entities.SizeExpertTailoringKey;
import com.smart.tailor.exception.*;
import com.smart.tailor.mapper.SizeExpertTailoringMapper;
import com.smart.tailor.repository.SizeExpertTailoringRepository;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.request.ExpertTailoringRequest;
import com.smart.tailor.utils.request.SizeExpertTailoringRequest;
import com.smart.tailor.utils.response.*;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SizeExpertTailoringServiceImpl implements SizeExpertTailoringService {
    private final SizeExpertTailoringRepository sizeExpertTailoringRepository;
    private final SizeService sizeService;
    private final ExpertTailoringService expertTailoringService;
    private final SizeExpertTailoringMapper sizeExpertTailoringMapper;
    private final ExcelImportService excelImportService;
    private final ExcelExportService excelExportService;
    private final Logger logger = LoggerFactory.getLogger(SizeExpertTailoringServiceImpl.class);

    @Transactional
    @Override
    public void createSizeExpertTailoring(SizeExpertTailoringRequest sizeExpertTailoringRequest) {
        var size = sizeService.findBySizeName(sizeExpertTailoringRequest.getSizeName())
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_SIZE));

        var expertTailoring = expertTailoringService.getExpertTailoringByExpertTailoringName(sizeExpertTailoringRequest.getExpertTailoringName())
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING));

        var sizeExpertTailoringExisted = sizeExpertTailoringRepository.findSizeExpertTailoringBySizeIDAndExpertTailoringID(size.getSizeID(), expertTailoring.getExpertTailoringID());
        if(sizeExpertTailoringExisted != null){
            throw new ItemAlreadyExistException(MessageConstant.SIZE_EXPERT_TAILORING_IS_EXISTED);
        }

        if(sizeExpertTailoringRequest.getMinFabric() > sizeExpertTailoringRequest.getMaxFabric()){
            throw new BadRequestException("Min Fabric can not greater than Max Fabric");
        }

        SizeExpertTailoringKey sizeExpertTailoringKey = SizeExpertTailoringKey
                .builder()
                .expertTailoringID(expertTailoring.getExpertTailoringID())
                .sizeID(size.getSizeID())
                .build();

        sizeExpertTailoringRepository.save(
                SizeExpertTailoring
                        .builder()
                        .sizeExpertTailoringKey(sizeExpertTailoringKey)
                        .minFabric(sizeExpertTailoringRequest.getMinFabric())
                        .maxFabric(sizeExpertTailoringRequest.getMaxFabric())
                        .size(size)
                        .expertTailoring(expertTailoring)
                        .unit(sizeExpertTailoringRequest.getUnit())
                        .status(true)
                        .build()
        );
    }

    @Override
    public List<SizeExpertTailoringResponse> findAllSizeExpertTailoring() {
        return sizeExpertTailoringRepository
                .findAll()
                .stream()
                .map(sizeExpertTailoringMapper::mapperToSizeExpertTailoringResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateSizeExpertTailoring(SizeExpertTailoringRequest sizeExpertTailoringRequest) {
        var size = sizeService.findBySizeName(sizeExpertTailoringRequest.getSizeName())
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_SIZE));

        var expertTailoring = expertTailoringService.getExpertTailoringByExpertTailoringName(sizeExpertTailoringRequest.getExpertTailoringName())
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING));

        var sizeExpertTailoringExisted = sizeExpertTailoringRepository.findSizeExpertTailoringBySizeIDAndExpertTailoringID(size.getSizeID(), expertTailoring.getExpertTailoringID());
        if(sizeExpertTailoringExisted == null){
            throw new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_SIZE_EXPERT_TAILORING);
        }

        if(sizeExpertTailoringRequest.getMinFabric() > sizeExpertTailoringRequest.getMaxFabric()){
            throw new BadRequestException("Min Fabric can not greater than Max Fabric");
        }

        SizeExpertTailoringKey sizeExpertTailoringKey = SizeExpertTailoringKey
                .builder()
                .expertTailoringID(expertTailoring.getExpertTailoringID())
                .sizeID(size.getSizeID())
                .build();

        sizeExpertTailoringRepository.save(
                SizeExpertTailoring
                        .builder()
                        .sizeExpertTailoringKey(sizeExpertTailoringKey)
                        .minFabric(sizeExpertTailoringRequest.getMinFabric())
                        .maxFabric(sizeExpertTailoringRequest.getMaxFabric())
                        .size(size)
                        .expertTailoring(expertTailoring)
                        .unit(sizeExpertTailoringRequest.getUnit())
                        .status(true)
                        .build()
        );
    }

    @Transactional
    @Override
    public void createSizeExpertTailoringByExcelFile(MultipartFile file) {
        if (!excelImportService.isValidExcelFile(file)) {
            throw new ExcelFileInvalidFormatException(MessageConstant.INVALID_EXCEL_FILE_FORMAT);
        }
        try {
            var excelData = excelImportService.getSizeExpertTailoringRequestFromExcel(file.getInputStream());

            if(excelData.isEmpty()){
                throw new BadRequestException("Size Expert Tailoring Excel File Has Empty Data");
            }

            Set<SizeExpertTailoringRequest> excelNames = new HashSet<>();
            List<SizeExpertTailoringRequest> uniqueExcelData = new ArrayList<>();
            List<Object> duplicateExcelData = new ArrayList<>();

            for(SizeExpertTailoringRequest request : excelData){
                if(!excelNames.add(request)){
                    duplicateExcelData.add(request);
                } else {
                    uniqueExcelData.add(request);
                }
            }

            if (!duplicateExcelData.isEmpty()) {
                throw new ExcelFileDuplicateDataException(MessageConstant.DUPLICATE_EXPERT_TAILORING_IN_EXCEL_FILE, duplicateExcelData);
            }

            List<Object> invalidData = new ArrayList<>();

            for (SizeExpertTailoringRequest expertTailoringRequest : uniqueExcelData) {
                try {
                    createSizeExpertTailoring(expertTailoringRequest);
                } catch (ItemNotFoundException ex) {
                    String errorMessage = ex.getMessage() != null ? ex.getMessage() : MessageConstant.CAN_NOT_FIND_ANY_SIZE;
                    logger.error("Error creating SizeExpertTailoring: Item not found - {}", errorMessage, ex);
                    invalidData.add(new ErrorData(expertTailoringRequest, errorMessage));
                } catch (ItemAlreadyExistException ex) {
                    String errorMessage = ex.getMessage() != null ? ex.getMessage() : MessageConstant.SIZE_EXPERT_TAILORING_IS_EXISTED;
                    logger.error("Error creating SizeExpertTailoring: Already exists - {}", errorMessage, ex);
                    invalidData.add(new ErrorData(expertTailoringRequest, errorMessage));
                } catch (BadRequestException ex) {
                    String errorMessage = ex.getMessage() != null ? ex.getMessage() : "Min Fabric can not greater than Max Fabric";
                    logger.error("Error creating SizeExpertTailoring: Bad request - {}", errorMessage, ex);
                    invalidData.add(new ErrorData(expertTailoringRequest, errorMessage));
                } catch (Exception ex) {
                    logger.error("Error creating SizeExpertTailoring - {}", ex.getMessage());
                    invalidData.add(new ErrorData(expertTailoringRequest, ex.getMessage()));
                }
            }

            if(!invalidData.isEmpty()){
                throw new ExcelFileInvalidDataTypeException("Some Data could not be processed correctly", invalidData);
            }

        } catch (IOException ex) {
            logger.error("Error processing excel file", ex);
            throw new ExcelFileInvalidFormatException(MessageConstant.INVALID_EXCEL_FILE_FORMAT);
        }
    }

    @Override
    public void generateSampleSizeExpertTailoringByExcelFile(HttpServletResponse response) throws IOException {
        String[] expertTailoringNames = expertTailoringService
                        .getAllExpertTailoring()
                        .stream()
                        .map(ExpertTailoringResponse::getExpertTailoringName)
                        .toList()
                        .toArray(String[]::new);

        String[] sizeNames = sizeService
                        .findAllSizeResponse()
                        .stream()
                        .map(SizeResponse::getSizeName)
                        .toList()
                        .toArray(String[]::new);

        excelExportService.exportSampleSizeExpertTailoring(response, expertTailoringNames, sizeNames);
    }
}
