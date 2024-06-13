package com.smart.tailor.service.impl;

import com.smart.tailor.service.ExcelImportService;
import com.smart.tailor.utils.request.BrandMaterialRequest;
import com.smart.tailor.utils.request.ExpertTailoringRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
                            brandMaterialRequest.setUnit(cell.getStringCellValue());
                            break;
                        case 3 :
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
            throw new RuntimeException(e);
        }
        return brandMaterialRequests;
    }

    @Override
    public List<ExpertTailoringRequest> getExpertTailoringDataFromExcel(InputStream inputStream) {
        List<ExpertTailoringRequest> expertTailoringRequests = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("Expert Tailoring");
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
            throw new RuntimeException(e);
        }
        return expertTailoringRequests;
    }
}
