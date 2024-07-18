package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.SizeExpertTailoring;
import com.smart.tailor.entities.SizeExpertTailoringKey;
import com.smart.tailor.exception.*;
import com.smart.tailor.mapper.SizeExpertTailoringMapper;
import com.smart.tailor.repository.SizeExpertTailoringRepository;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.request.SizeExpertTailoringRequest;
import com.smart.tailor.utils.response.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
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
                .orElseThrow(() -> new ItemNotFoundException("Can not find Size with SizeName:" + sizeExpertTailoringRequest.getSizeName()));

        var expertTailoring = expertTailoringService.getExpertTailoringByExpertTailoringName(sizeExpertTailoringRequest.getExpertTailoringName())
                .orElseThrow(() -> new ItemNotFoundException("Can not find Expert Tailoring with ExpertTailoringName: " + sizeExpertTailoringRequest.getExpertTailoringName()));

        var sizeExpertTailoringExisted = sizeExpertTailoringRepository.existsByExpertTailoringExpertTailoringNameAndSizeSizeNameAndMinFabricAndMaxFabricAndUnit(
                sizeExpertTailoringRequest.getExpertTailoringName(),
                sizeExpertTailoringRequest.getSizeName(),
                sizeExpertTailoringRequest.getMinFabric(),
                sizeExpertTailoringRequest.getMaxFabric(),
                sizeExpertTailoringRequest.getUnit()
        );

        if (sizeExpertTailoringExisted) {
            throw new ItemAlreadyExistException("Size Expert Tailoring is existed");
        }

        if (sizeExpertTailoringRequest.getMinFabric() > sizeExpertTailoringRequest.getMaxFabric()) {
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

    @Override
    public List<SizeExpertTailoringResponse> findAllSizeExpertTailoringID(UUID expectTailoringID) {
        return sizeExpertTailoringRepository
                .findAll()
                .stream()
                .filter(sizeExpertTailoring -> {
                    return sizeExpertTailoring.getExpertTailoring().getExpertTailoringID().equals(expectTailoringID);
                })
                .map(sizeExpertTailoringMapper::mapperToSizeExpertTailoringResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateSizeExpertTailoring(SizeExpertTailoringRequest sizeExpertTailoringRequest) {
        var size = sizeService.findBySizeName(sizeExpertTailoringRequest.getSizeName())
                .orElseThrow(() -> new ItemNotFoundException("Can not find Size with SizeName:" + sizeExpertTailoringRequest.getSizeName()));

        var expertTailoring = expertTailoringService.getExpertTailoringByExpertTailoringName(sizeExpertTailoringRequest.getExpertTailoringName())
                .orElseThrow(() -> new ItemNotFoundException("Can not find Expert Tailoring with ExpertTailoringName: " + sizeExpertTailoringRequest.getExpertTailoringName()));

        var sizeExpertTailoringExisted = sizeExpertTailoringRepository.existsByExpertTailoringExpertTailoringNameAndSizeSizeNameAndMinFabricAndMaxFabricAndUnit(
                sizeExpertTailoringRequest.getExpertTailoringName(),
                sizeExpertTailoringRequest.getSizeName(),
                sizeExpertTailoringRequest.getMinFabric(),
                sizeExpertTailoringRequest.getMaxFabric(),
                sizeExpertTailoringRequest.getUnit()
        );

        if (sizeExpertTailoringExisted) {
            throw new ItemAlreadyExistException("Size Expert Tailoring is existed");
        }

        if (sizeExpertTailoringRequest.getMinFabric() > sizeExpertTailoringRequest.getMaxFabric()) {
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
        if (!excelImportService.isValidExcelFile(file)) {
            throw new ExcelFileInvalidFormatException(MessageConstant.INVALID_EXCEL_FILE_FORMAT);
        }
        try {
            List<Pair<Integer, SizeExpertTailoringRequest>> sizeExpertTailoringRequests = new ArrayList<>();
            List<ErrorDetail> errorFields = new ArrayList<>();
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = workbook.getSheet("Size Expert Tailoring");

            if (sheet == null) {
                throw new ExcelFileNotSupportException(MessageConstant.WRONG_TYPE_OF_CATEGORY_AND_MATERIAL_EXCEL_FILE);
            }
            logger.info("Inside getSizeExpertTailoringRequestFromExcel Method");

            boolean inValidData = false;
            List<Object> cellErrorResponses = new ArrayList<>();

            int rowIndex = 2;
            while (rowIndex <= sheet.getLastRowNum()) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isRowCompletelyEmptyForSizeExpertTailoring(row)) {
                    rowIndex++;
                    continue;
                }

                List<String> errors = new ArrayList<>();
                SizeExpertTailoringRequest sizeExpertTailoringRequest = new SizeExpertTailoringRequest();
                boolean rowDataValid = true;
                boolean isValid = false;
                String message = "";
                double doubleValue = -1;
                for (int cellIndex = 0; cellIndex < 5; cellIndex++) {
                    Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (cell == null || cell.getCellType() == CellType.BLANK) {
                        inValidData = true;
                        rowDataValid = false;
                        errors.add(getCellNameForSizeExpertTailoring(cellIndex) + " at row Index " + (rowIndex + 1) + " is empty!");
                    } else {
                        switch (cellIndex) {
                            case 0:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    sizeExpertTailoringRequest.setExpertTailoringName(cell.getStringCellValue());
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    errors.add(getCellNameForSizeExpertTailoring(cellIndex) + " at row Index " + (rowIndex + 1) + " Require Data Type String!");
                                }
                                break;
                            case 1:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    sizeExpertTailoringRequest.setSizeName(cell.getStringCellValue());
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    errors.add(getCellNameForSizeExpertTailoring(cellIndex) + " at row Index " + (rowIndex + 1) + " Require Data Type String!");
                                }
                                break;
                            case 2:
                                isValid = false;
                                doubleValue = -1;
                                message = " Require Data Type Numeric!";
                                switch (cell.getCellType()) {
                                    case NUMERIC:
                                        doubleValue = (double) cell.getNumericCellValue();
                                        isValid = true;
                                        break;
                                    case STRING:
                                        try {
                                            doubleValue = Double.parseDouble(cell.getStringCellValue());
                                            isValid = true;
                                        } catch (NumberFormatException e) {
                                            isValid = false;
                                            System.out.println(e.getMessage());
                                        }
                                        break;
                                }
                                if(isValid && doubleValue >= 0){
                                    sizeExpertTailoringRequest.setMinFabric(doubleValue);
                                }else{
                                    if(isValid && doubleValue < 0){
                                        message = " Require Positive Numeric!";
                                    }
                                    inValidData = true;
                                    rowDataValid = false;
                                    errors.add(getCellNameForSizeExpertTailoring(cellIndex) + " at row Index " + (rowIndex + 1) + message);
                                }
                                break;
                            case 3:
                                isValid = false;
                                doubleValue = -1;
                                message = " Require Data Type Numeric!";
                                switch (cell.getCellType()) {
                                    case NUMERIC:
                                        doubleValue = (double) cell.getNumericCellValue();
                                        isValid = true;
                                        break;
                                    case STRING:
                                        try {
                                            doubleValue = Double.parseDouble(cell.getStringCellValue());
                                            isValid = true;
                                        } catch (NumberFormatException e) {
                                            isValid = false;
                                            System.out.println(e.getMessage());
                                        }
                                        break;
                                }
                                if(isValid && doubleValue >= 0){
                                    sizeExpertTailoringRequest.setMaxFabric(doubleValue);
                                }else{
                                    if(isValid && doubleValue < 0){
                                        message = " Require Positive Numeric!";
                                    }
                                    inValidData = true;
                                    rowDataValid = false;
                                    errors.add(getCellNameForSizeExpertTailoring(cellIndex) + " at row Index " + (rowIndex + 1) + message);
                                }
                                break;
                            case 4:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    sizeExpertTailoringRequest.setUnit(cell.getStringCellValue());
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    errors.add(getCellNameForSizeExpertTailoring(cellIndex) + " at row Index " + (rowIndex + 1) + " Require Data Type String!");
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }

                if (rowDataValid) {
                    sizeExpertTailoringRequests.add(Pair.of(rowIndex + 1, sizeExpertTailoringRequest));
                } else {
                    errorFields.add(new ErrorDetail(errors));
                }
                rowIndex++;
            }

            if (sizeExpertTailoringRequests.isEmpty()) {
                throw new BadRequestException("Size Expert Tailoring Excel File Has Empty Data");
            }

            Set<SizeExpertTailoringRequest> duplicateExcelData = new HashSet<>();

            for (var pairSizeExpertTailoringRequest : sizeExpertTailoringRequests) {
                var indexSizeExpertTailoringRequest = pairSizeExpertTailoringRequest.getFirst();
                var sizeExpertTailoringRequest = pairSizeExpertTailoringRequest.getSecond();
                List<String> errors = new ArrayList<>();

                var size = sizeService.findBySizeName(sizeExpertTailoringRequest.getSizeName());
                if(size.isEmpty()){
                    errors.add("Size_Name at row Index: " + indexSizeExpertTailoringRequest + " Not Found!");
                }


                var expertTailoring = expertTailoringService.getExpertTailoringByExpertTailoringName(sizeExpertTailoringRequest.getExpertTailoringName());
                if(expertTailoring.isEmpty()){
                    errors.add("Expert_Tailoring_Name at row Index: " + indexSizeExpertTailoringRequest + " Not Found!");
                }

                if(!duplicateExcelData.add(sizeExpertTailoringRequest)){
                    errors.add("Duplicate Size Expert Tailoring Request Data at row Index: " + indexSizeExpertTailoringRequest + " in Excel File");
                }

                var sizeExpertTailoringExisted = sizeExpertTailoringRepository.existsByExpertTailoringExpertTailoringNameAndSizeSizeNameAndMinFabricAndMaxFabricAndUnit(
                        sizeExpertTailoringRequest.getExpertTailoringName(),
                        sizeExpertTailoringRequest.getSizeName(),
                        sizeExpertTailoringRequest.getMinFabric(),
                        sizeExpertTailoringRequest.getMaxFabric(),
                        sizeExpertTailoringRequest.getUnit()
                );

                if (sizeExpertTailoringExisted) {
                    errors.add("Size Expert Tailoring is Existed");
                }

                if (sizeExpertTailoringRequest.getMinFabric() > sizeExpertTailoringRequest.getMaxFabric()) {
                    errors.add("Min Fabric can not greater than Max Fabric");
                }

                if (errors.size() > 0){
                    errorFields.add(new ErrorDetail(sizeExpertTailoringRequest, errors));
                } else {
                    SizeExpertTailoringKey sizeExpertTailoringKey = SizeExpertTailoringKey
                            .builder()
                            .expertTailoringID(expertTailoring.get().getExpertTailoringID())
                            .sizeID(size.get().getSizeID())
                            .build();

                    sizeExpertTailoringRepository.save(
                            SizeExpertTailoring
                                    .builder()
                                    .sizeExpertTailoringKey(sizeExpertTailoringKey)
                                    .minFabric(sizeExpertTailoringRequest.getMinFabric())
                                    .maxFabric(sizeExpertTailoringRequest.getMaxFabric())
                                    .size(size.get())
                                    .expertTailoring(expertTailoring.get())
                                    .unit(sizeExpertTailoringRequest.getUnit())
                                    .status(true)
                                    .build()
                    );
                }
            }

            if (!errorFields.isEmpty()) {
                throw new ExcelFileInvalidDataTypeException("Some Data could not be processed correctly", errorFields);
            }

        } catch (IOException ex) {
            logger.error("Error processing excel file", ex);
            throw new ExcelFileInvalidFormatException(MessageConstant.INVALID_EXCEL_FILE_FORMAT);
        }
    }

    private boolean isRowCompletelyEmptyForSizeExpertTailoring(Row row) {
        for (int cellIndex = 0; cellIndex < 5; cellIndex++) {
            Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private String getCellNameForSizeExpertTailoring(int cellIndex) {
        switch (cellIndex) {
            case 0: return "Expert_Tailoring_Name";
            case 1: return "Size_Name";
            case 2: return "Min_Fabric";
            case 3: return "Max_Fabric";
            case 4: return "Unit";
            default: return "Unknown";
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
