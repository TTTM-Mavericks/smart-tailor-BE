package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.exception.ExcelFileErrorReadingException;
import com.smart.tailor.exception.ExcelFileNotSupportException;
import com.smart.tailor.exception.ExcelFileInvalidDataTypeException;
import com.smart.tailor.service.ExcelImportService;
import com.smart.tailor.utils.request.*;
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
import java.util.*;

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
    public List<ExpertTailoringRequest> getExpertTailoringDataFromExcel(InputStream inputStream) {
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
                return expertTailoringRequests;
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

    @Override
    public List<SizeExpertTailoringRequest> getSizeExpertTailoringRequestFromExcel(InputStream inputStream) {
        List<SizeExpertTailoringRequest> sizeExpertTailoringRequests = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
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
                        cellErrorResponses.add(
                                CellErrorResponse
                                        .builder()
                                        .rowIndex(rowIndex + 1)
                                        .cellIndex(cellIndex + 1)
                                        .cellName(getCellNameForSizeExpertTailoring(cellIndex))
                                        .data("")
                                        .message(MessageConstant.DATA_IS_EMPTY)
                                        .build()
                        );
                    } else {
                        switch (cellIndex) {
                            case 0:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    sizeExpertTailoringRequest.setExpertTailoringName(cell.getStringCellValue());
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    cellErrorResponses.add(
                                            CellErrorResponse
                                                    .builder()
                                                    .rowIndex(rowIndex + 1)
                                                    .cellIndex(cellIndex + 1)
                                                    .cellName(getCellNameForSizeExpertTailoring(cellIndex))
                                                    .data(cell.toString())
                                                    .message(MessageConstant.INVALID_DATA_TYPE_COLUMN_NEED_TYPE_STRING)
                                                    .build()
                                    );
                                }
                                break;
                            case 1:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    sizeExpertTailoringRequest.setSizeName(cell.getStringCellValue());
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    cellErrorResponses.add(
                                            CellErrorResponse
                                                    .builder()
                                                    .rowIndex(rowIndex + 1)
                                                    .cellIndex(cellIndex + 1)
                                                    .cellName(getCellNameForSizeExpertTailoring(cellIndex))
                                                    .data(cell.toString())
                                                    .message(MessageConstant.INVALID_DATA_TYPE_COLUMN_NEED_TYPE_STRING)
                                                    .build()
                                    );
                                }
                                break;
                            case 2:
                                isValid = false;
                                doubleValue = -1;
                                message = MessageConstant.INVALID_DATA_TYPE_COLUMN_NEED_TYPE_NUMERIC;
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
                                        message = MessageConstant.INVALID_NEGATIVE_NUMBER_NEED_POSITIVE_NUMBER;
                                    }
                                    inValidData = true;
                                    rowDataValid = false;
                                    cellErrorResponses.add(
                                            CellErrorResponse
                                                    .builder()
                                                    .rowIndex(rowIndex + 1)
                                                    .cellIndex(cellIndex + 1)
                                                    .cellName(getCellNameForSizeExpertTailoring(cellIndex))
                                                    .message(message)
                                                    .data(cell.toString())
                                                    .build()
                                    );
                                }
                                break;
                            case 3:
                                isValid = false;
                                doubleValue = -1;
                                message = MessageConstant.INVALID_DATA_TYPE_COLUMN_NEED_TYPE_NUMERIC;
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
                                        message = MessageConstant.INVALID_NEGATIVE_NUMBER_NEED_POSITIVE_NUMBER;
                                    }
                                    inValidData = true;
                                    rowDataValid = false;
                                    cellErrorResponses.add(
                                            CellErrorResponse
                                                    .builder()
                                                    .rowIndex(rowIndex + 1)
                                                    .cellIndex(cellIndex + 1)
                                                    .cellName(getCellNameForSizeExpertTailoring(cellIndex))
                                                    .message(message)
                                                    .data(cell.toString())
                                                    .build()
                                    );
                                }
                                break;
                            case 4:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    sizeExpertTailoringRequest.setUnit(cell.getStringCellValue());
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    cellErrorResponses.add(
                                            CellErrorResponse
                                                    .builder()
                                                    .rowIndex(rowIndex + 1)
                                                    .cellIndex(cellIndex + 1)
                                                    .cellName(getCellNameForSizeExpertTailoring(cellIndex))
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
                    sizeExpertTailoringRequests.add(sizeExpertTailoringRequest);
                }

                rowIndex++;
            }

            if (inValidData) {
                throw new ExcelFileInvalidDataTypeException(MessageConstant.INVALID_DATA_TYPE, cellErrorResponses);
            } else {
               return sizeExpertTailoringRequests;
            }
        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", e.getMessage());
            throw new ExcelFileErrorReadingException(MessageConstant.ERROR_READING_EXCEL_FILE);
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
    public List<ExpertTailoringMaterialListRequest> getExpertTailoringMaterialDataFromExcel(InputStream inputStream) {
        List<ExpertTailoringMaterialListRequest> expertTailoringMaterialListRequests = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("Expert Tailoring Material");

            if (sheet == null) {
                throw new ExcelFileNotSupportException(MessageConstant.WRONG_TYPE_OF_EXPERT_TAILORING_MATERIAL_EXCEL_FILE);
            }
            logger.info("Inside getExpertTailoringMaterialDataFromExcel Method");

            boolean inValidData = false;
            List<Object> cellErrorResponses = new ArrayList<>();

            int rowIndex = 2;
            while (rowIndex <= sheet.getLastRowNum()) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isRowCompletelyEmptyForExpertTailoringMaterial(row)) {
                    rowIndex++;
                    continue;
                }

                ExpertTailoringMaterialListRequest expertTailoringMaterialListRequest = new ExpertTailoringMaterialListRequest();
                boolean rowDataValid = true;
                boolean isValid = false;
                String message = "";
                double doubleValue = -1;
                for (int cellIndex = 0; cellIndex < 3; cellIndex++) {
                    Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (cell == null || cell.getCellType() == CellType.BLANK) {
                        inValidData = true;
                        rowDataValid = false;
                        cellErrorResponses.add(
                                CellErrorResponse
                                        .builder()
                                        .rowIndex(rowIndex + 1)
                                        .cellIndex(cellIndex + 1)
                                        .cellName(getCellNameForExpertTailoringMaterial(cellIndex))
                                        .data("")
                                        .message(MessageConstant.DATA_IS_EMPTY)
                                        .build()
                        );
                    } else {
                        switch (cellIndex) {
                            case 0:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    expertTailoringMaterialListRequest.setCategoryName(cell.getStringCellValue());
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    cellErrorResponses.add(
                                            CellErrorResponse
                                                    .builder()
                                                    .rowIndex(rowIndex + 1)
                                                    .cellIndex(cellIndex + 1)
                                                    .cellName(getCellNameForExpertTailoringMaterial(cellIndex))
                                                    .data(cell.toString())
                                                    .message(MessageConstant.INVALID_DATA_TYPE_COLUMN_NEED_TYPE_STRING)
                                                    .build()
                                    );
                                }
                                break;
                            case 1:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    expertTailoringMaterialListRequest.setMaterialName(cell.getStringCellValue());
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    cellErrorResponses.add(
                                            CellErrorResponse
                                                    .builder()
                                                    .rowIndex(rowIndex + 1)
                                                    .cellIndex(cellIndex + 1)
                                                    .cellName(getCellNameForExpertTailoringMaterial(cellIndex))
                                                    .data(cell.toString())
                                                    .message(MessageConstant.INVALID_DATA_TYPE_COLUMN_NEED_TYPE_STRING)
                                                    .build()
                                    );
                                }
                                break;
                            case 2:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    String[] expertTailoringNames = cell.getStringCellValue().split(",");
                                    List<String> trimmedNames = Arrays.stream(expertTailoringNames)
                                            .map(String::trim)
                                            .toList();
                                    expertTailoringMaterialListRequest.setExpertTailoringNames(trimmedNames);
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    cellErrorResponses.add(
                                            CellErrorResponse
                                                    .builder()
                                                    .rowIndex(rowIndex + 1)
                                                    .cellIndex(cellIndex + 1)
                                                    .cellName(getCellNameForExpertTailoringMaterial(cellIndex))
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
                    expertTailoringMaterialListRequests.add(expertTailoringMaterialListRequest);
                }

                rowIndex++;
            }

            if (inValidData) {
                throw new ExcelFileInvalidDataTypeException(MessageConstant.INVALID_DATA_TYPE, cellErrorResponses);
            } else {
                return expertTailoringMaterialListRequests;
            }
        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", e.getMessage());
            throw new ExcelFileErrorReadingException(MessageConstant.ERROR_READING_EXCEL_FILE);
        }
    }

    private boolean isRowCompletelyEmptyForExpertTailoringMaterial(Row row) {
        for (int cellIndex = 0; cellIndex < 5; cellIndex++) {
            Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private String getCellNameForExpertTailoringMaterial(int cellIndex) {
        switch (cellIndex) {
            case 0: return "Category_Name";
            case 1: return "Material_Name";
            case 2: return "Expert_Tailoring_Name";
            default: return "Unknown";
        }
    }
}
