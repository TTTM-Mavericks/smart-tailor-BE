package com.smart.tailor.service.impl;

import com.smart.tailor.service.ExcelImportService;
import com.smart.tailor.utils.request.BrandMaterialRequest;
import com.smart.tailor.utils.request.ExpertTailoringRequest;
import com.smart.tailor.utils.request.MaterialRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public List<MaterialRequest> getCategoryMaterialDataFromExcel(InputStream inputStream) {
        List<MaterialRequest> materialRequests = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("Category and Material");
            if(sheet == null){
                return null;
            }
            logger.info("Inside getCategoryMaterialDataFromExcel Method");
            int rowIndex = 0;
            for(Row row : sheet){
                if(rowIndex == 0){
                    ++rowIndex;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                MaterialRequest materialRequest = new MaterialRequest();
                while(cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    switch (cellIndex){
                        case 0 :
                            materialRequest.setCategoryName(cell.getStringCellValue());
                            break;
                        case 1 :
                            materialRequest.setMaterialName(cell.getStringCellValue());
                            break;
                        case 2 :
                            materialRequest.setHsCode(cell.getNumericCellValue());
                            break;
                        default :
                            break;
                    }
                    ++cellIndex;
                }
                materialRequests.add(materialRequest);
            }
        } catch (IOException e) {
            logger.error("{}", e.getMessage());
            return null;
        }
        return materialRequests;
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
