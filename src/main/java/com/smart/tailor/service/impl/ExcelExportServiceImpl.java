package com.smart.tailor.service.impl;


import com.smart.tailor.service.ExcelExportService;
import com.smart.tailor.utils.response.BrandMaterialResponse;
import com.smart.tailor.utils.response.ExpertTailoringResponse;
import com.smart.tailor.utils.response.MaterialResponse;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.smart.tailor.constant.FormatConstant.PERCENTAGE_FLUCTUATION_WITHIN_LIMIT_RANGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelExportServiceImpl implements ExcelExportService {
    private void createCell(Row row, int columnIndex, Object value, CellStyle style, XSSFSheet sheet){
        Cell cell = row.createCell(columnIndex);
        if(value instanceof Integer){
            cell.setCellValue((Integer) value);
        } else if(value instanceof Double){
            cell.setCellValue((Double) value);
        } else if(value instanceof Float){
            cell.setCellValue((Float) value);
        } else if(value instanceof Long){
            cell.setCellValue((Long) value);
        } else if(value instanceof Boolean){
            cell.setCellValue((Boolean) value);
        }else if(value instanceof LocalDateTime){
            LocalDateTime localDateTime = (LocalDateTime) value;
            Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            cell.setCellValue(date);
        }else if(value instanceof Date){
            cell.setCellValue((Date) value);
        }else if(value instanceof LocalDate){
            LocalDate localDate = (LocalDate) value;
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            cell.setCellValue(date);
        }else if(value instanceof Calendar){
            cell.setCellValue((Calendar) value);
        }else if(value instanceof String){
            cell.setCellValue((String) value);
        }else {
            cell.setCellValue("");
        }
        cell.setCellStyle(style);
        sheet.autoSizeColumn(columnIndex);
    }

//    @Override
//    public void exportBrandMaterialData(List<BrandMaterialResponse> brandMaterialResponses, HttpServletResponse response) throws IOException {
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        XSSFSheet sheet = workbook.createSheet("Brand Material List");
//
//        // Create Title Row of Excel Sheet
//        Row row = sheet.createRow(0);
//        CellStyle style = workbook.createCellStyle();
//        XSSFFont font = workbook.createFont();
//        font.setBold(true);
//        font.setFontHeight(20);
//        style.setFont(font);
//        style.setAlignment(HorizontalAlignment.CENTER);
//        createCell(row, 0, "Brand Material List", style, sheet);
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
//        font.setFontHeightInPoints((short) 10);
//
//        // Create Header of Excel Sheet
//        row = sheet.createRow(1);
//        font.setBold(true);
//        font.setFontHeight(16);
//        style.setFont(font);
//        createCell(row, 0, "Category Name", style, sheet);
//        createCell(row, 1, "Material Name", style, sheet);
//        createCell(row, 2, "Brand Name", style, sheet);
//        createCell(row, 3, "Unit", style, sheet);
//        createCell(row, 4, "Price", style, sheet);
//        createCell(row, 5, "Create Date", style, sheet);
//        createCell(row, 6, "Last Modified Date", style, sheet);
//
//        // Write Data from DB to Excel Sheet
//        int rowIndex = 2;
//        CellStyle styleData = workbook.createCellStyle();
//        XSSFFont fontData = workbook.createFont();
//        fontData.setBold(false);
//        fontData.setFontHeight(14);
//        styleData.setFont(fontData);
//        styleData.setAlignment(HorizontalAlignment.CENTER);
//
//        // Format CreateDate and LastModifiedDate
//        CellStyle dateTimeCellStyle = workbook.createCellStyle();
//        CreationHelper createHelper = workbook.getCreationHelper();
//        dateTimeCellStyle.setFont(fontData);
//        dateTimeCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss.00"));
//        dateTimeCellStyle.setAlignment(HorizontalAlignment.CENTER);
//
//        for(var brandMaterial : brandMaterialResponses){
//            Row rowSheet = sheet.createRow(rowIndex++);
//            int countIndex = 0;
//            createCell(rowSheet, countIndex++, brandMaterial.getCategoryName(), styleData, sheet);
//            createCell(rowSheet, countIndex++, brandMaterial.getMaterialName(), styleData, sheet);
//            createCell(rowSheet, countIndex++, brandMaterial.getBrandName(), styleData, sheet);
//            createCell(rowSheet, countIndex++, brandMaterial.getUnit(), styleData, sheet);
//            createCell(rowSheet, countIndex++, brandMaterial.getPrice(), styleData, sheet);
//            createCell(rowSheet, countIndex++, brandMaterial.getCreateDate(), dateTimeCellStyle, sheet);
//            createCell(rowSheet, countIndex++, brandMaterial.getLastModifiedDate(), dateTimeCellStyle, sheet);
//        }
//
//        // Export Data to Excel
//        ServletOutputStream outputStream = response.getOutputStream();
//        workbook.write(outputStream);
//        workbook.close();
//        outputStream.close();
//    }

    @Override
    public void exportExpertTailoringData(List<ExpertTailoringResponse> expertTailoringResponses, HttpServletResponse response) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Expert Tailoring List");

        // Create Title Row of Excel Sheet
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        createCell(row, 0, "Expert Tailoring List", style, sheet);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
        font.setFontHeightInPoints((short) 10);

        // Create Header of Excel Sheet
        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "Expert_Tailoring_Name", style, sheet);
        createCell(row, 1, "Size_Image_Url", style, sheet);
        createCell(row, 2, "Create_Date", style, sheet);
        createCell(row, 3, "Last_Modified_Date", style, sheet);

        // Write Data from DB to Excel Sheet
        int rowIndex = 2;
        CellStyle styleData = workbook.createCellStyle();
        XSSFFont fontData = workbook.createFont();
        fontData.setBold(false);
        fontData.setFontHeight(14);
        styleData.setFont(fontData);
        styleData.setAlignment(HorizontalAlignment.CENTER);

        // Format CreateDate and LastModifiedDate
        CellStyle dateTimeCellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateTimeCellStyle.setFont(fontData);
        dateTimeCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss.00"));
        dateTimeCellStyle.setAlignment(HorizontalAlignment.CENTER);

        for(var expertTailoring : expertTailoringResponses){
            Row rowSheet = sheet.createRow(rowIndex++);
            int countIndex = 0;
            createCell(rowSheet, countIndex++, expertTailoring.getExpertTailoringName(), styleData, sheet);
            createCell(rowSheet, countIndex++, expertTailoring.getSizeImageUrl(), styleData, sheet);
            createCell(rowSheet, countIndex++, expertTailoring.getCreateDate(), dateTimeCellStyle, sheet);
            createCell(rowSheet, countIndex++, expertTailoring.getLastModifiedDate(), dateTimeCellStyle, sheet);
        }

        // Export Data to Excel
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    @Override
    public void exportCategoryMaterialForBrand(List<MaterialResponse> materialResponses, HttpServletResponse response) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Brand Material");

        // Create Title Row of Excel Sheet
        Row row = sheet.createRow(0);
        CellStyle titleStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        titleStyle.setFont(font);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        createCell(row, 0, "Category and Material List", titleStyle, sheet);
        CellRangeAddress rangeAddress = new CellRangeAddress(0, 0, 0, 5);
        sheet.addMergedRegion(rangeAddress);
        RegionUtil.setBorderTop(BorderStyle.MEDIUM, rangeAddress, sheet);
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, rangeAddress, sheet);
        RegionUtil.setBorderLeft(BorderStyle.MEDIUM, rangeAddress, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, rangeAddress, sheet);
        font.setFontHeightInPoints((short) 10);

        // Create Header of Excel Sheet
        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        titleStyle.setFont(font);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.cloneStyleFrom(titleStyle);
        headerStyle.setBorderTop(BorderStyle.MEDIUM);
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerStyle.setBorderLeft(BorderStyle.MEDIUM);
        headerStyle.setBorderRight(BorderStyle.MEDIUM);

        createCell(row, 0, "Category_Name", headerStyle, sheet);
        createCell(row, 1, "Material_Name", headerStyle, sheet);
        createCell(row, 2, "HS_Code", headerStyle, sheet);
        createCell(row, 3, "Unit", headerStyle, sheet);
        createCell(row, 4, "Base_Price", headerStyle, sheet);
        createCell(row, 5, "Brand_Price", headerStyle, sheet);

        // Write Data from DB to Excel Sheet
        int rowIndex = 2;
        XSSFFont fontData = workbook.createFont();
        fontData.setBold(false);
        fontData.setFontHeight(14);

        CellStyle lockedData = workbook.createCellStyle();
        lockedData.setLocked(true);
        lockedData.setFont(fontData);
        lockedData.setAlignment(HorizontalAlignment.CENTER);
        lockedData.setBorderTop(BorderStyle.MEDIUM);
        lockedData.setBorderBottom(BorderStyle.MEDIUM);
        lockedData.setBorderLeft(BorderStyle.MEDIUM);
        lockedData.setBorderRight(BorderStyle.MEDIUM);

        CellStyle styleData = workbook.createCellStyle();
        styleData.setLocked(false);
        styleData.setFont(fontData);
        styleData.setAlignment(HorizontalAlignment.CENTER);
        styleData.setBorderTop(BorderStyle.MEDIUM);
        styleData.setBorderBottom(BorderStyle.MEDIUM);
        styleData.setBorderLeft(BorderStyle.MEDIUM);
        styleData.setBorderRight(BorderStyle.MEDIUM);

        for(var materialResponse : materialResponses){
            Row rowSheet = sheet.createRow(rowIndex++);
            int countIndex = 0;
            createCell(rowSheet, countIndex++, materialResponse.getCategoryName(), lockedData, sheet);
            createCell(rowSheet, countIndex++, materialResponse.getMaterialName(), lockedData, sheet);
            createCell(rowSheet, countIndex++, materialResponse.getHsCode(), lockedData, sheet);
            createCell(rowSheet, countIndex++, materialResponse.getUnit(), lockedData, sheet);
            createCell(rowSheet, countIndex++, materialResponse.getBasePrice(), lockedData, sheet);
            createCell(rowSheet, countIndex++, null, styleData, sheet);
        }
        // Auto Size Column to fit content
        for(int i = 0; i < 6; ++i){
            sheet.autoSizeColumn(i);
        }
        // Set Password to Unlock Columns and Rows
        sheet.protectSheet("Aa@123456");

        // Apply Border to Sheet
        rangeAddress = new CellRangeAddress(0, materialResponses.size(),  0, 5);

        // Apply data validation for Brand Price column from rowIndex = 2 to the last row
        int lastRow = sheet.getLastRowNum();

        String brandPriceFormula = "AND(F3>=1, E3>=F3*" + (1 - PERCENTAGE_FLUCTUATION_WITHIN_LIMIT_RANGE) + ", E3<=F3*" + (1 + PERCENTAGE_FLUCTUATION_WITHIN_LIMIT_RANGE) + ")";
        DataValidationHelper validationHelper = sheet.getDataValidationHelper();

        // Create a CellRangeAddressList for Brand Price column
        CellRangeAddressList brandPriceAddressList = new CellRangeAddressList(2, lastRow, 5, 5);

        // Create a custom data validation constraint
        DataValidationConstraint brandPriceConstraint = validationHelper.createCustomConstraint(brandPriceFormula);

        // Create data validation for Brand Price column
        DataValidation brandPriceValidation = validationHelper.createValidation(brandPriceConstraint, brandPriceAddressList);

        // Show error box if the data validation fails
        brandPriceValidation.setShowErrorBox(true);
        brandPriceValidation.createErrorBox("Invalid Input", "Brand Price must be between " + (100 - PERCENTAGE_FLUCTUATION_WITHIN_LIMIT_RANGE * 100) + "% and " + (100 + PERCENTAGE_FLUCTUATION_WITHIN_LIMIT_RANGE * 100) + "% of Base Price.");

        // Add data validation to the sheet
        sheet.addValidationData(brandPriceValidation);

        // Export Data to Excel
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    @Override
    public void exportSampleExpertTailoring(HttpServletResponse response) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Expert Tailoring");

        // Create Title Row of Excel Sheet
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setLocked(true);
        createCell(row, 0, "Expert Tailoring List", style, sheet);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
        font.setFontHeightInPoints((short) 10);

        // Create Header of Excel Sheet
        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        createCell(row, 0, "Expert_Tailoring_Name", style, sheet);
        createCell(row, 1, "Size_Image_Url", style, sheet);

        int rowIndex = 2;
        CellStyle styleData = workbook.createCellStyle();
        XSSFFont fontData = workbook.createFont();
        fontData.setBold(false);
        fontData.setFontHeight(14);
        styleData.setFont(fontData);
        styleData.setLocked(false);
        styleData.setAlignment(HorizontalAlignment.CENTER);

        // Set Password to Unlock Columns and Rows
        sheet.protectSheet("Aa@123456");

        // Unlocked For Specific Cells
        for(int i = 2; i <= 200; ++i){
            row = sheet.createRow(i);
            Cell cellA = row.createCell(0);
            Cell cellB = row.createCell(1);
            cellA.setCellStyle(styleData);
            cellB.setCellStyle(styleData);
        }

        // Create Data Validation for String
        DataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheet);

        // Apply Constraint to Cell 0 Which is Expert Tailoring Name
        DataValidationConstraint constraint = dataValidationHelper.createCustomConstraint("ISTEXT(A3)");
        CellRangeAddressList expertTailoringNameRange = new CellRangeAddressList(2, 200, 0,0);
        DataValidation expertTailoringNameValidation = dataValidationHelper.createValidation(constraint, expertTailoringNameRange);
        expertTailoringNameValidation.setShowErrorBox(true);
        expertTailoringNameValidation.createErrorBox("Invalid Input", "Expert Tailoring Name must be Type String");
        sheet.addValidationData(expertTailoringNameValidation);

        // Apply Constraint to Cell 0 Which is Expert Tailoring Name
        constraint = dataValidationHelper.createCustomConstraint("ISTEXT(B3)");
        CellRangeAddressList expertTailoringUrlRange = new CellRangeAddressList(2, 200, 1,1);
        DataValidation expertTailoringUrlValidation = dataValidationHelper.createValidation(constraint, expertTailoringUrlRange);
        expertTailoringUrlValidation.setShowErrorBox(true);
        expertTailoringUrlValidation.createErrorBox("Invalid Input", "Expert Tailoring Url must be Type String");
        sheet.addValidationData(expertTailoringUrlValidation);

        // Set Width for Specific Column
        sheet.setColumnWidth(0, 30 * 256);
        sheet.setColumnWidth(1, 160 * 256);

        // Export Data to Excel
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    @Override
    public void exportSampleCategoryMaterial(HttpServletResponse response) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Category and Material");

        // Create Title Row of Excel Sheet
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        style.setFont(font);
        style.setLocked(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        createCell(row, 0, "Category and Material", style, sheet);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
        font.setFontHeightInPoints((short) 10);

        // Create Header of Excel Sheet
        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "Category_Name", style, sheet);
        createCell(row, 1, "Material_Name", style, sheet);
        createCell(row, 2, "HS_Code", style, sheet);
        createCell(row, 3, "Unit", style, sheet);
        createCell(row, 4, "Base_Price", style, sheet);

        int rowIndex = 2;
        XSSFFont fontData = workbook.createFont();
        fontData.setBold(false);
        fontData.setFontHeight(14);
        CellStyle styleData = workbook.createCellStyle();
        styleData.setLocked(false);
        styleData.setFont(fontData);
        styleData.setAlignment(HorizontalAlignment.CENTER);
        // Set Password to Unlock Columns and Rows
        sheet.protectSheet("Aa@123456");

        // Unlocked For Specific Cells
        for(int i = 2; i <= 200; ++i){
            row = sheet.createRow(i);
            Cell cellA = row.createCell(0);
            Cell cellB = row.createCell(1);
            Cell cellC = row.createCell(2);
            Cell cellD = row.createCell(3);
            Cell cellE = row.createCell(4);
            cellA.setCellStyle(styleData);
            cellB.setCellStyle(styleData);
            cellC.setCellStyle(styleData);
            cellD.setCellStyle(styleData);
            cellE.setCellStyle(styleData);
        }

        // Create Data Validation for String
        DataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheet);

        // Apply Constraint to Cell 0 <=> CategoryName
        DataValidationConstraint constraint = dataValidationHelper.createCustomConstraint("ISTEXT(A3)");
        CellRangeAddressList categoryNameRange = new CellRangeAddressList(2, 200, 0,0);
        DataValidation categoryNameValidation = dataValidationHelper.createValidation(constraint, categoryNameRange);
        categoryNameValidation.setShowErrorBox(true);
        categoryNameValidation.createErrorBox("Invalid Input", "Category Name must be Type String");
        sheet.addValidationData(categoryNameValidation);

        // Apply Constraint to Cell 1 <=> MaterialName
        constraint = dataValidationHelper.createCustomConstraint("ISTEXT(B3)");
        CellRangeAddressList materialNameRange = new CellRangeAddressList(2, 200, 1, 1);
        DataValidation materialNameValidation = dataValidationHelper.createValidation(constraint, materialNameRange);
        materialNameValidation.setShowErrorBox(true);
        materialNameValidation.createErrorBox("Invalid Input", "Material Name must be Type String");
        sheet.addValidationData(materialNameValidation);

        // Apply Constraint to Cell 2 <=> HSCode
        constraint = dataValidationHelper.createCustomConstraint("AND(ISNUMBER(C3), C3 >= 0)");
        CellRangeAddressList hsCodeRange = new CellRangeAddressList(2, 200, 2,2);
        DataValidation hsCodeValidation = dataValidationHelper.createValidation(constraint, hsCodeRange);
        hsCodeValidation.setShowErrorBox(true);
        hsCodeValidation.createErrorBox("Invalid Input", "HS Code must be Type Number And Non-Negative Number");
        sheet.addValidationData(hsCodeValidation);

        // Apply Constraint to Cell 3 <=> Unit
        constraint = dataValidationHelper.createCustomConstraint("ISTEXT(D3)");
        CellRangeAddressList unitRange = new CellRangeAddressList(2, 200, 3, 3);
        DataValidation unitValidation = dataValidationHelper.createValidation(constraint, unitRange);
        unitValidation.setShowErrorBox(true);
        unitValidation.createErrorBox("Invalid Input", "Material Name must be Type String");
        sheet.addValidationData(unitValidation);

        // Apply Constraint to Cell 4 <=> BasePrice
        constraint = dataValidationHelper.createCustomConstraint("AND(ISNUMBER(E3), E3 >= 0)");
        CellRangeAddressList basePriceRange = new CellRangeAddressList(2, 200, 4,4);
        DataValidation basePriceValidation = dataValidationHelper.createValidation(constraint, basePriceRange);
        basePriceValidation.setShowErrorBox(true);
        basePriceValidation.createErrorBox("Invalid Input", "Base Price must be Type Number And Non-Negative Number");
        sheet.addValidationData(basePriceValidation);

        // Set Width for Specific Column
        sheet.setColumnWidth(0, 25 * 256);
        sheet.setColumnWidth(1, 55 * 256);
        sheet.setColumnWidth(2, 18 * 256);
        sheet.setColumnWidth(3, 18 * 256);
        sheet.setColumnWidth(4, 18 * 256);


        // Export Data to Excel
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
