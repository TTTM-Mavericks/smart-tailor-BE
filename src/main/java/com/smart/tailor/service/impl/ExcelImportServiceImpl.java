package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.ExcelImportService;
import com.smart.tailor.utils.request.BrandMaterialRequest;
import com.smart.tailor.utils.request.ExpertTailoringRequest;
import com.smart.tailor.utils.request.MaterialRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.CellErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
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
    public List<BrandMaterialRequest> getBrandMaterialDataFromExcel(InputStream inputStream, String brandName) {
        List<BrandMaterialRequest> brandMaterialRequests = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("Brand Material");
            if(sheet == null){
                return null;
            }
            logger.info("Inside getBrandMaterialDataFromExcel Method");
            int rowIndex = 0;
            for(Row row : sheet){
                if(rowIndex == 0){
                    ++rowIndex;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                BrandMaterialRequest brandMaterialRequest = new BrandMaterialRequest();
                while(cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    switch (cellIndex){
                        case 0 :
                            brandMaterialRequest.setCategoryName(cell.getStringCellValue());
                            break;
                        case 1 :
                            brandMaterialRequest.setMaterialName(cell.getStringCellValue());
                            break;
                        case 2 :
                            brandMaterialRequest.setHsCode(cell.getNumericCellValue());
                            break;
                        case 3 :
                            brandMaterialRequest.setUnit(cell.getStringCellValue());
                            break;
                        case 4 :
                            brandMaterialRequest.setPrice(cell.getNumericCellValue());
                            break;
                        default :
                            break;
                    }
                    ++cellIndex;
                }
                brandMaterialRequest.setBrandName(brandName);
                brandMaterialRequests.add(brandMaterialRequest);
            }
        } catch (IOException e) {
            logger.error("{}", e.getMessage());
            return null;
        }
        return brandMaterialRequests;
    }

    @Override
    public APIResponse getCategoryMaterialDataFromExcel(InputStream inputStream) {
        List<MaterialRequest> materialRequests = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("Category and Material");

            if(sheet == null){
                return APIResponse
                        .builder()
                        .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                        .message(MessageConstant.WRONG_TYPE_OF_CATEGORY_AND_MATERIAL_EXCEL_FILE)
                        .data(null)
                        .build();
            }
            logger.info("Inside getCategoryMaterialDataFromExcel Method");

            boolean inValidData = false;
            List<CellErrorResponse> cellErrorResponses = new ArrayList<>();
            int rowIndex = 0;
            for(Row row : sheet){
                if(++rowIndex == 1){
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                MaterialRequest materialRequest = new MaterialRequest();
                while(cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    switch (cellIndex){
                        case 0 :
                            if(cell.getCellType() == CellType.STRING){
                                materialRequest.setCategoryName(cell.getStringCellValue());
                            } else {
                                inValidData = true;
                                cellErrorResponses.add(
                                        CellErrorResponse
                                                .builder()
                                                .rowIndex(rowIndex)
                                                .cellIndex(cellIndex)
                                                .cellName("Category Name")
                                                .data(cell.toString())
                                                .message(MessageConstant.DATA_IS_EMPTY)
                                                .build()
                                );
                            }
                            break;
                        case 1 :
                            if(cell.getCellType() == CellType.STRING){
                                materialRequest.setMaterialName(cell.getStringCellValue());
                            } else {
                                inValidData = true;
                                cellErrorResponses.add(
                                        CellErrorResponse
                                                .builder()
                                                .rowIndex(rowIndex)
                                                .cellIndex(cellIndex)
                                                .cellName("Material Name")
                                                .data(cell.toString())
                                                .message(MessageConstant.DATA_IS_EMPTY)
                                                .build()
                                );
                            }
                            break;
                        case 2 :
                            if(cell.getCellType() == CellType.NUMERIC && cell.getNumericCellValue() >= 0){
                                materialRequest.setHsCode(cell.getNumericCellValue());
                            } else {
                                inValidData = true;
                                String message = MessageConstant.INVALID_DATA_TYPE;
                                if(cell.getCellType() == CellType.BLANK) {
                                    message = MessageConstant.DATA_IS_EMPTY;
                                } else if (cell.getCellType() == CellType.NUMERIC && cell.getNumericCellValue() < 0){
                                    message = MessageConstant.INVALID_NEGATIVE_NUMBER_NEED_POSITIVE_NUMBER;
                                }
                                cellErrorResponses.add(
                                        CellErrorResponse
                                                .builder()
                                                .rowIndex(rowIndex)
                                                .cellIndex(cellIndex)
                                                .cellName("HS Code")
                                                .data(cell.toString())
                                                .message(message)
                                                .build()
                                );
                            }
                            break;
                        case 3 :
                            if(cell.getCellType() == CellType.STRING){
                                materialRequest.setUnit(cell.getStringCellValue());
                            } else {
                                inValidData = true;
                                cellErrorResponses.add(
                                        CellErrorResponse
                                                .builder()
                                                .rowIndex(rowIndex)
                                                .cellIndex(cellIndex)
                                                .cellName("Unit")
                                                .data(cell.toString())
                                                .message(MessageConstant.DATA_IS_EMPTY)
                                                .build()
                                );
                            }
                            break;
                        case 4 :
                            if(cell.getCellType() == CellType.NUMERIC && cell.getNumericCellValue() >= 0){
                                materialRequest.setBasePrice(cell.getNumericCellValue());
                            } else {
                                inValidData = true;
                                String message = MessageConstant.INVALID_DATA_TYPE;
                                if(cell.getCellType() == CellType.BLANK) {
                                    message = MessageConstant.DATA_IS_EMPTY;
                                } else if (cell.getCellType() == CellType.NUMERIC && cell.getNumericCellValue() < 0){
                                    message = MessageConstant.INVALID_NEGATIVE_NUMBER_NEED_POSITIVE_NUMBER;
                                }
                                cellErrorResponses.add(
                                        CellErrorResponse
                                                .builder()
                                                .rowIndex(rowIndex)
                                                .cellIndex(cellIndex)
                                                .cellName("Base Price")
                                                .data(cell.toString())
                                                .message(message)
                                                .build()
                                );
                            }
                            break;
                        default :
                            break;
                    }
                    ++cellIndex;
                }
                materialRequests.add(materialRequest);
            }
            if(inValidData){
                return APIResponse
                        .builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(MessageConstant.INVALID_DATA_TYPE)
                        .data(cellErrorResponses)
                        .build();
            }else {
                return APIResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message(MessageConstant.GET_DATA_FROM_EXCEL_SUCCESS)
                        .data(materialRequests)
                        .build();
            }
        } catch (IOException e) {
            logger.error("{}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<ExpertTailoringRequest> getExpertTailoringDataFromExcel(InputStream inputStream) {
        List<ExpertTailoringRequest> expertTailoringRequests = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("Expert Tailoring");
            if(sheet == null){
                return null;
            }
            logger.info("Inside getExpertTailoringDataFromExcel Method");
            int rowIndex = 0;
            for(Row row : sheet){
                if(rowIndex == 0){
                    ++rowIndex;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                ExpertTailoringRequest expertTailoringRequest  = new ExpertTailoringRequest();
                while(cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    switch (cellIndex){
                        case 0 :
                            expertTailoringRequest.setExpertTailoringName(cell.getStringCellValue());
                            break;
                        case 1 :
                            expertTailoringRequest.setSizeImageUrl(cell.getStringCellValue());
                            break;
                        default :
                            break;
                    }
                    ++cellIndex;
                }
                expertTailoringRequests.add(expertTailoringRequest);
            }
        } catch (IOException e) {
            logger.error("{}", e.getMessage());
            return null;
        }
        return expertTailoringRequests;
    }
}
