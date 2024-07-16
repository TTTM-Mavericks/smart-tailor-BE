package com.smart.tailor.service;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Size;
import com.smart.tailor.exception.DuplicateDataException;
import com.smart.tailor.exception.ItemAlreadyExistException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.SizeMapper;
import com.smart.tailor.repository.SizeRepository;
import com.smart.tailor.utils.request.ListSizeRequest;
import com.smart.tailor.utils.request.SizeRequest;
import com.smart.tailor.utils.response.ErrorDetail;
import com.smart.tailor.utils.response.SizeResponse;
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
public class SizeServiceImpl implements SizeService{
    private final SizeRepository sizeRepository;
    private final SizeMapper sizeMapper;
    private final Logger logger = LoggerFactory.getLogger(SizeServiceImpl.class);

    @Override
    public void createSize(ListSizeRequest listSizeRequest) {
        List<Object> errorDetails = new ArrayList<>();
        for(SizeRequest sizeRequest : listSizeRequest.getSizeRequestList()){
            String errorMessage = null;
            var sizeExisted = sizeRepository.findBySizeName(sizeRequest.getSizeName().toUpperCase());
            if(sizeExisted.isPresent()){
                errorMessage = "Size is existed with Size Name: " + sizeRequest.getSizeName();
            }

            if(errorMessage != null){
                errorDetails.add(new ErrorDetail(sizeRequest, errorMessage));
            } else {
                sizeRepository.save(
                        Size
                                .builder()
                                .sizeName(sizeRequest.getSizeName().toUpperCase())
                                .status(true)
                                .build()
                );
            }
        }
        if(!errorDetails.isEmpty()){
            throw new DuplicateDataException(MessageConstant.SIZE_IS_EXISTED, errorDetails);
        }
    }

    @Override
    public List<SizeResponse> findAllSizeResponse() {
        return sizeRepository
                .findAll()
                .stream()
                .map(sizeMapper::mapperToSizeResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateSize(UUID sizeID, SizeRequest sizeRequest) {
        var currentSize = sizeRepository.findById(sizeID)
                .orElseThrow(() -> new ItemNotFoundException("Can not find any Size with SizeID: " + sizeID));

        var sizeExisted = sizeRepository.findBySizeName(sizeRequest.getSizeName().toUpperCase());
        if(sizeExisted.isPresent()){
            if(!sizeExisted.get().getSizeID().toString().equals(currentSize.getSizeID().toString())){
                throw new ItemAlreadyExistException("Size is existed with SizeName: " + sizeRequest.getSizeName());
            }
        }
        sizeRepository.save(
                Size
                        .builder()
                        .sizeID(currentSize.getSizeID())
                        .sizeName(sizeRequest.getSizeName().toUpperCase())
                        .status(currentSize.getStatus())
                        .build()
        );
    }

    @Override
    public Optional<Size> findBySizeName(String sizeName) {
        return sizeRepository.findBySizeName(sizeName);
    }

    @Override
    public Optional<Size> findByID(UUID sizeID) {
        return sizeRepository.findById(sizeID);
    }
}
