package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.ExpertTailoringMaterial;
import com.smart.tailor.entities.ExpertTailoringMaterialKey;
import com.smart.tailor.exception.DuplicateDataException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.ExpertTailoringMaterialMapper;
import com.smart.tailor.repository.ExpertTailoringMaterialRepository;
import com.smart.tailor.service.ExpertTailoringMaterialService;
import com.smart.tailor.service.ExpertTailoringService;
import com.smart.tailor.service.MaterialService;
import com.smart.tailor.utils.request.ExpertTailoringMaterialListRequest;
import com.smart.tailor.utils.request.ExpertTailoringMaterialRequest;
import com.smart.tailor.utils.response.ExpertTailoringMaterialResponse;
import com.smart.tailor.utils.response.MaterialResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpertTailoringMaterialServiceImpl implements ExpertTailoringMaterialService {
    private final ExpertTailoringMaterialRepository expertTailoringMaterialRepository;
    private final ExpertTailoringService expertTailoringService;
    private final MaterialService materialService;
    private final ExpertTailoringMaterialMapper expertTailoringMaterialMapper;
    private final Logger logger = LoggerFactory.getLogger(ExpertTailoringMaterialServiceImpl.class);

    @Transactional
    @Override
    public void createExpertTailoringMaterial(ExpertTailoringMaterialListRequest expertTailoringMaterialListRequest) {
        String categoryName = expertTailoringMaterialListRequest.getCategoryName();
        String materialName = expertTailoringMaterialListRequest.getMaterialName();

        var material = materialService.findByMaterialNameAndCategory_CategoryName(
                        materialName, categoryName)
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_MATERIAL + " : " + materialName));

        List<Object> duplicateExpertTailoringMaterials = new ArrayList<>();

        for(String expertTailoringName : expertTailoringMaterialListRequest.getExpertTailoringNames()){
            var expertTailoring = expertTailoringService.getExpertTailoringByExpertTailoringName(expertTailoringName)
                    .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING + " : " + expertTailoringName));

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


}
