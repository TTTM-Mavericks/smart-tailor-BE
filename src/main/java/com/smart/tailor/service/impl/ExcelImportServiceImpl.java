package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.exception.ExcelFileErrorReadingException;
import com.smart.tailor.exception.ExcelFileNotSupportException;
import com.smart.tailor.exception.ExcelFileInvalidDataTypeException;
import com.smart.tailor.service.ExcelImportService;
import com.smart.tailor.utils.request.BrandMaterialRequest;
import com.smart.tailor.utils.request.ExpertTailoringRequest;
import com.smart.tailor.utils.request.MaterialRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.CellErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelImportServiceImpl implements ExcelImportService {
    private final Logger logger = LoggerFactory.getLogger(ExcelImportServiceImpl.class);

    private final String EXCEL_FORMAT = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @Override
    public boolean isValidExcelFile(MultipartFile file) {
        return Objects.equals(file.getContentType(), EXCEL_FORMAT);
    }

    @Override
    public APIResponse getBrandMaterialDataFromExcel(InputStream inputStream, String brandName) {
        List<BrandMaterialRequest> brandMaterialRequests = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("Brand Material");

            if (sheet == null) {
                throw new ExcelFileNotSupportException(MessageConstant.WRONG_TYPE_OF_BRAND_MATERIAL_EXCEL_FILE);
            }

            logger.info("Inside getBrandMaterialDataFromExcel Method");
            boolean inValidData = false;
            List<Object> cellErrorResponses = new ArrayList<>();
            int rowIndex = 2;
            while(rowIndex <= sheet.getLastRowNum()){
                Row row = sheet.getRow(rowIndex);
                if(row == null || isRowCompletelyEmptyForBrandMaterial(row)){
                    rowIndex++;
                    continue;
                }

                BrandMaterialRequest brandMaterialRequest = new BrandMaterialRequest();
                boolean rowDataValid = true;
                boolean brandPriceIsEmpty = false;
                boolean isValid = false;
                double numericValue = -1;
                String message = "";
                for(int cellIndex = 0; cellIndex < 6; cellIndex++){
                    Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(cell == null || cell.getCellType() == CellType.BLANK){
                        if(cellIndex != 5) {
                            inValidData = true;
                            rowDataValid = false;
                            cellErrorResponses.add(
                                    CellErrorResponse
                                            .builder()
                                            .rowIndex(rowIndex + 1)
                                            .cellIndex(cellIndex + 1)
                                            .cellName(getCellNameForBrandMaterial(cellIndex))
                                            .message(MessageConstant.DATA_IS_EMPTY)
                                            .data(null)
                                            .build()
                            );
                        } else{
                            brandPriceIsEmpty = true;
                        }
                    }
                    else{
                        switch (cellIndex){
                            case 0:
                                if(cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()){
                                    brandMaterialRequest.setCategoryName(cell.getStringCellValue());
                                } else{
                                    inValidData = true;
                                    rowDataValid = false;
                                    cellErrorResponses.add(
                                            CellErrorResponse
                                                    .builder()
                                                    .rowIndex(rowIndex + 1)
                                                    .cellIndex(cellIndex + 1)
                                                    .cellName(getCellNameForBrandMaterial(cellIndex))
                                                    .message(MessageConstant.INVALID_DATA_TYPE_COLUMN_NEED_TYPE_STRING)
                                                    .data(cell.toString())
                                                    .build()
                                    );
                                }
                                break;
                            case 1:
                                if(cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()){
                                    brandMaterialRequest.setMaterialName(cell.getStringCellValue());
                                } else{
                                    inValidData = true;
                                    rowDataValid = false;
                                    cellErrorResponses.add(
                                            CellErrorResponse
                                                    .builder()
                                                    .rowIndex(rowIndex + 1)
                                                    .cellIndex(cellIndex + 1)
                                                    .cellName(getCellNameForBrandMaterial(cellIndex))
                                                    .message(MessageConstant.INVALID_DATA_TYPE_COLUMN_NEED_TYPE_STRING)
                                                    .data(cell.toString())
                                                    .build()
                                    );
                                }
                                break;
                            case 2:
                                isValid = false;
                                long longValue = -1;
                                message = MessageConstant.INVALID_DATA_TYPE_COLUMN_NEED_TYPE_NUMERIC;
                                switch (cell.getCellType()) {
                                    case NUMERIC:
                                        longValue = (long) cell.getNumericCellValue();
                                        isValid = true;
                                        break;
                                    case STRING:
                                        try {
                                            longValue = Long.parseLong(cell.getStringCellValue());
                                            isValid = true;
                                        } catch (NumberFormatException e) {
                                            isValid = false;
                                            System.out.println(e.getMessage());
                                        }
                                        break;
                                }
                                if(isValid && longValue >= 0){
                                    brandMaterialRequest.setHsCode(longValue);
                                }else{
                                    if(isValid && longValue < 0){
                                        message = MessageConstant.INVALID_NEGATIVE_NUMBER_NEED_POSITIVE_NUMBER;
                                    }
                                    inValidData = true;
                                    rowDataValid = false;
                                    cellErrorResponses.add(
                                            CellErrorResponse
                                                    .builder()
                                                    .rowIndex(rowIndex + 1)
                                                    .cellIndex(cellIndex + 1)
                                                    .cellName(getCellNameForBrandMaterial(cellIndex))
                                                    .message(message)
                                                    .data(cell.toString())
                                                    .build()
                                    );
                                }
                                break;
                            case 3:
                                if(cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()){
                                    brandMaterialRequest.setUnit(cell.getStringCellValue());
                                }else{
                                    inValidData = true;
                                    rowDataValid = false;
                                    cellErrorResponses.add(
                                            CellErrorResponse
                                                    .builder()
                                                    .rowIndex(rowIndex + 1)
                                                    .cellIndex(cellIndex + 1)
                                                    .cellName(getCellNameForBrandMaterial(cellIndex))
                                                    .message(MessageConstant.INVALID_DATA_TYPE_COLUMN_NEED_TYPE_STRING)
                                                    .data(cell.toString())
                                                    .build()
                                    );
                                }
                                break;
                            case 4:
                                isValid = false;
                                numericValue = -1;
                                message = MessageConstant.INVALID_DATA_TYPE_COLUMN_NEED_TYPE_NUMERIC;
                                switch (cell.getCellType()) {
                                    case NUMERIC:
                                        numericValue = cell.getNumericCellValue();
                                        isValid = true;
                                        break;
                                    case STRING:
                                        try {
                                            numericValue = Double.parseDouble(cell.getStringCellValue());
                                            isValid = true;
                                        } catch (NumberFormatException e) {
                                            isValid = false;
                                            System.out.println(e.getMessage());
                                        }
                                        break;
                                }
                                if(isValid && numericValue >= 0){
                                    brandMaterialRequest.setBasePrice(numericValue);
                                }else{
                                    if(isValid && numericValue < 0){
                                        message = MessageConstant.INVALID_NEGATIVE_NUMBER_NEED_POSITIVE_NUMBER;
                                    }
                                    inValidData = true;
                                    rowDataValid = false;
                                    cellErrorResponses.add(
                                            CellErrorResponse
                                                    .builder()
                                                    .rowIndex(rowIndex + 1)
                                                    .cellIndex(cellIndex + 1)
                                                    .cellName(getCellNameForBrandMaterial(cellIndex))
                                                    .message(message)
                                                    .data(cell.toString())
                                                    .build()
                                    );
                                }
                                break;
                            case 5:
                                isValid = false;
                                numericValue = -1;
                                message = MessageConstant.INVALID_DATA_TYPE_COLUMN_NEED_TYPE_NUMERIC;
                                switch (cell.getCellType()) {
                                    case NUMERIC:
                                        numericValue = cell.getNumericCellValue();
                                        isValid = true;
                                        break;
                                    case STRING:
                                        try {
                                            numericValue = Double.parseDouble(cell.getStringCellValue());
                                            isValid = true;
                                        } catch (NumberFormatException e) {
                                            isValid = false;
                                            System.out.println(e.getMessage());
                                        }
                                        break;
                                }
                                 if(isValid && numericValue >= 0){
                                    brandMaterialRequest.setBrandPrice(numericValue);
                                }else{
                                    if(isValid && numericValue < 0){
                                        message = MessageConstant.INVALID_NEGATIVE_NUMBER_NEED_POSITIVE_NUMBER;
                                    }
                                    inValidData = true;
                                    rowDataValid = false;
                                    cellErrorResponses.add(
                                            CellErrorResponse
                                                    .builder()
                                                    .rowIndex(rowIndex + 1)
                                                    .cellIndex(cellIndex + 1)
                                                    .cellName(getCellNameForBrandMaterial(cellIndex))
                                                    .message(message)
                                                    .data(cell.toString())
                                                    .build()
                                    );
                                }
                                break;
                        }
                    }
                }
                if(rowDataValid && !brandPriceIsEmpty){
                    brandMaterialRequest.setBrandName(brandName);
                    brandMaterialRequests.add(brandMaterialRequest);
                }
                rowIndex++;
            }
            if (inValidData) {
                throw new ExcelFileInvalidDataTypeException(MessageConstant.INVALID_DATA_TYPE, cellErrorResponses);
            } else {
                return APIResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message(MessageConstant.GET_DATA_FROM_EXCEL_SUCCESS)
                        .data(brandMaterialRequests)
                        .build();
            }
        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", e.getMessage());
            throw new ExcelFileErrorReadingException(MessageConstant.ERROR_READING_EXCEL_FILE);
        }
    }

    private boolean isRowCompletelyEmptyForBrandMaterial(Row row){
        for(int cellIndex = 0; cellIndex < 6; cellIndex++){
            Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if(cell != null && cell.getCellType() != CellType.BLANK){
                return false;
            }
        }
        return true;
    }

    private String getCellNameForBrandMaterial(int cellIndex){
        switch (cellIndex){
            case 0: return "Category_Name";
            case 1: return "Material_Name";
            case 2: return "HS_Code";
            case 3: return "Unit";
            case 4: return "Base_Price";
            case 5: return "Brand_Price";
            default: return "Unknown_Data";
        }
    }

    @Override
    public APIResponse getCategoryMaterialDataFromExcel(InputStream inputStream) {
        List<MaterialRequest> materialRequests = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("Category and Material");

            if (sheet == null) {
                throw new ExcelFileNotSupportException(MessageConstant.WRONG_TYPE_OF_CATEGORY_AND_MATERIAL_EXCEL_FILE);
            }
            logger.info("Inside getCategoryMaterialDataFromExcel Method");

            boolean inValidData = false;
            List<Object> cellErrorResponses = new ArrayList<>();

            int rowIndex = 2;
            while (rowIndex <= sheet.getLastRowNum()) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isRowCompletelyEmptyForCategoryMaterial(row)) {
                    rowIndex++;
                    continue;
                }

                MaterialRequest materialRequest = new MaterialRequest();
                boolean rowDataValid = true;
                boolean isValid = false;
                String message = "";
                double numericValue = -1;
                for (int cellIndex = 0; cellIndex < 5; cellIndex++) {
                    Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (cell == null || cell.getCellType() == CellType.BLANK) {
                        inValidData = true;
                        rowDataValid = false;
                        cellErrorResponses.add(
                                CellErrorResponse
                                        .builder()
                                        .rowIndex(rowIndex + 1)
                                        .cellIndex(cellIndex + 1)
                                        .cellName(getCellNameForCategoryMaterial(cellIndex))
                                        .data("")
                                        .message(MessageConstant.DATA_IS_EMPTY)
                                        .build()
                        );
                    } else {
                        switch (cellIndex) {
                            case 0:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    materialRequest.setCategoryName(cell.getStringCellValue());
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    cellErrorResponses.add(
                                            CellErrorResponse
                                                    .builder()
                                                    .rowIndex(rowIndex + 1)
                                                    .cellIndex(cellIndex + 1)
                                                    .cellName(getCellNameForCategoryMaterial(cellIndex))
                                                    .data(cell.toString())
                                                    .message(MessageConstant.INVALID_DATA_TYPE_COLUMN_NEED_TYPE_STRING)
                                                    .build()
                                    );
                                }
                                break;
                            case 1:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    materialRequest.setMaterialName(cell.getStringCellValue());
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    cellErrorResponses.add(
                                            CellErrorResponse
                                                    .builder()
                                                    .rowIndex(rowIndex + 1)
                                                    .cellIndex(cellIndex + 1)
                                                    .cellName(getCellNameForCategoryMaterial(cellIndex))
                                                    .data(cell.toString())
                                                    .message(MessageConstant.INVALID_DATA_TYPE_COLUMN_NEED_TYPE_STRING)
                                                    .build()
                                    );
                                }
                                break;
                            case 2:
                                 isValid = false;
                                 long longValue = -1;
                                 message = MessageConstant.INVALID_DATA_TYPE_COLUMN_NEED_TYPE_NUMERIC;
                                switch (cell.getCellType()) {
                                    case NUMERIC:
                                        longValue = (long) cell.getNumericCellValue();
                                        isValid = true;
                                        break;
                                    case STRING:
                                        try {
                                            longValue = Long.parseLong(cell.getStringCellValue());
                                            isValid = true;
                                        } catch (NumberFormatException e) {
                                            isValid = false;
                                            System.out.println(e.getMessage());
                                        }
                                        break;
                                }
                                if(isValid && longValue >= 0){
                                    materialRequest.setHsCode(longValue);
                                }else{
                                    if(isValid && longValue < 0){
                                        message = MessageConstant.INVALID_NEGATIVE_NUMBER_NEED_POSITIVE_NUMBER;
                                    }
                                    inValidData = true;
                                    rowDataValid = false;
                                    cellErrorResponses.add(
                                            CellErrorResponse
                                                    .builder()
                                                    .rowIndex(rowIndex + 1)
                                                    .cellIndex(cellIndex + 1)
                                                    .cellName(getCellNameForCategoryMaterial(cellIndex))
                                                    .message(message)
                                                    .data(cell.toString())
                                                    .build()
                                    );
                                }
                                break;
                            case 3:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    materialRequest.setUnit(cell.getStringCellValue());
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    cellErrorResponses.add(
                                            CellErrorResponse
                                                    .builder()
                                                    .rowIndex(rowIndex + 1)
                                                    .cellIndex(cellIndex + 1)
                                                    .cellName(getCellNameForCategoryMaterial(cellIndex))
                                                    .data(cell.toString())
                                                    .message(MessageConstant.INVALID_DATA_TYPE_COLUMN_NEED_TYPE_STRING)
                                                    .build()
                                    );
                                }
                                break;
                            case 4:
                                 isValid = false;
                                 numericValue = -1;
                                 message = MessageConstant.INVALID_DATA_TYPE_COLUMN_NEED_TYPE_NUMERIC;
                                switch (cell.getCellType()) {
                                    case NUMERIC:
                                        numericValue = cell.getNumericCellValue();
                                        isValid = true;
                                        break;
                                    case STRING:
                                        try {
                                            numericValue = Double.parseDouble(cell.getStringCellValue());
                                            isValid = true;
                                        } catch (NumberFormatException e) {
                                            isValid = false;
                                            System.out.println(e.getMessage());
                                        }
                                        break;
                                }
                                if(isValid && numericValue >= 0){
                                    materialRequest.setBasePrice(numericValue);
                                }else{
                                    if(isValid && numericValue < 0){
                                        message = MessageConstant.INVALID_NEGATIVE_NUMBER_NEED_POSITIVE_NUMBER;
                                    }
                                    inValidData = true;
                                    rowDataValid = false;
                                    cellErrorResponses.add(
                                            CellErrorResponse
                                                    .builder()
                                                    .rowIndex(rowIndex + 1)
                                                    .cellIndex(cellIndex + 1)
                                                    .cellName(getCellNameForCategoryMaterial(cellIndex))
                                                    .message(message)
                                                    .data(cell.toString())
                                                    .build()
                                    );
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }

                if (rowDataValid) {
                    materialRequests.add(materialRequest);
                }

                rowIndex++;
            }

            if (inValidData) {
                throw new ExcelFileInvalidDataTypeException(MessageConstant.INVALID_DATA_TYPE, cellErrorResponses);
            } else {
                return APIResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message(MessageConstant.GET_DATA_FROM_EXCEL_SUCCESS)
                        .data(materialRequests)
                        .build();
            }
        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", e.getMessage());
            throw new ExcelFileErrorReadingException(MessageConstant.ERROR_READING_EXCEL_FILE);
        }
    }

    private boolean isRowCompletelyEmptyForCategoryMaterial(Row row) {
        for (int cellIndex = 0; cellIndex < 5; cellIndex++) {
            Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private String getCellNameForCategoryMaterial(int cellIndex) {
        switch (cellIndex) {
            case 0: return "Category_Name";
            case 1: return "Material_Name";
            case 2: return "HS_Code";
            case 3: return "Unit";
            case 4: return "Base_Price";
            default: return "Unknown";
        }
    }

    @Override
    public APIResponse getExpertTailoringDataFromExcel(InputStream inputStream) {
        List<ExpertTailoringRequest> expertTailoringRequests = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("Expert Tailoring");
            if (sheet == null) {
                throw new ExcelFileNotSupportException(MessageConstant.WRONG_TYPE_OF_EXPERT_TAILORING_EXCEL_FILE);
            }

            logger.info("Inside getExpertTailoringDataFromExcel Method");

            boolean inValidData = false;
            List<Object> cellErrorResponses = new ArrayList<>();

            int rowIndex = 2;
            while (rowIndex <= sheet.getLastRowNum()) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isRowCompletelyEmptyForExpertTailoring(row)) {
                    rowIndex++;
                    continue;
                }

                ExpertTailoringRequest expertTailoringRequest = new ExpertTailoringRequest();
                boolean rowDataValid = true;
                for (int cellIndex = 0; cellIndex < 2; cellIndex++) {
                    Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (cell == null || cell.getCellType() == CellType.BLANK) {
                        inValidData = true;
                        rowDataValid = false;
                        cellErrorResponses.add(
                                CellErrorResponse
                                        .builder()
                                        .rowIndex(rowIndex + 1)
                                        .cellIndex(cellIndex + 1)
                                        .cellName(getCellNameForExpertTailoring(cellIndex))
                                        .data(null)
                                        .message(MessageConstant.DATA_IS_EMPTY)
                                        .build()
                        );
                    } else {
                        switch (cellIndex) {
                            case 0:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    expertTailoringRequest.setExpertTailoringName(cell.getStringCellValue());
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    cellErrorResponses.add(
                                            CellErrorResponse
                                                    .builder()
                                                    .rowIndex(rowIndex + 1)
                                                    .cellIndex(cellIndex + 1)
                                                    .cellName(getCellNameForExpertTailoring(cellIndex))
                                                    .data(cell.toString())
                                                    .message(MessageConstant.INVALID_DATA_TYPE_COLUMN_NEED_TYPE_STRING)
                                                    .build()
                                    );
                                }
                                break;
                            case 1:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    expertTailoringRequest.setSizeImageUrl(cell.getStringCellValue());
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    cellErrorResponses.add(
                                            CellErrorResponse
                                                    .builder()
                                                    .rowIndex(rowIndex + 1)
                                                    .cellIndex(cellIndex + 1)
                                                    .cellName(getCellNameForExpertTailoring(cellIndex))
                                                    .data(cell.toString())
                                                    .message(MessageConstant.INVALID_DATA_TYPE_COLUMN_NEED_TYPE_STRING)
                                                    .build()
                                    );
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }

                if (rowDataValid) {
                    expertTailoringRequests.add(expertTailoringRequest);
                }

                rowIndex++;
            }

            if (inValidData) {
                throw new ExcelFileInvalidDataTypeException(MessageConstant.INVALID_DATA_TYPE, cellErrorResponses);
            } else {
                return APIResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message(MessageConstant.GET_DATA_FROM_EXCEL_SUCCESS)
                        .data(expertTailoringRequests)
                        .build();
            }
        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", e.getMessage());
            throw new ExcelFileErrorReadingException(MessageConstant.ERROR_READING_EXCEL_FILE);
        }
    }

    private boolean isRowCompletelyEmptyForExpertTailoring(Row row) {
        for (int cellIndex = 0; cellIndex < 2; cellIndex++) {
            Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private String getCellNameForExpertTailoring(int cellIndex) {
        switch (cellIndex) {
            case 0: return "Expert_Tailoring_Name";
            case 1: return "Size_Image_URL";
            default: return "Unknown_Data";
        }
    }

}
