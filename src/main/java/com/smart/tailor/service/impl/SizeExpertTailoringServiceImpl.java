package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.SizeExpertTailoring;
import com.smart.tailor.entities.SizeExpertTailoringKey;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemAlreadyExistException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.SizeExpertTailoringMapper;
import com.smart.tailor.repository.SizeExpertTailoringRepository;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.request.SizeExpertTailoringRequest;
import com.smart.tailor.utils.response.ExpertTailoringResponse;
import com.smart.tailor.utils.response.SizeExpertTailoringResponse;
import com.smart.tailor.utils.response.SizeResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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

    @Override
    public void createSizeExpertTailoringByExcelFile(MultipartFile file) {

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
