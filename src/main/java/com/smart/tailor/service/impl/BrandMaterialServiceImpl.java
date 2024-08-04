package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Brand;
import com.smart.tailor.entities.BrandMaterial;
import com.smart.tailor.entities.BrandMaterialKey;
import com.smart.tailor.exception.*;
import com.smart.tailor.mapper.BrandMaterialMapper;
import com.smart.tailor.repository.BrandMaterialRepository;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.request.BrandMaterialRequest;
import com.smart.tailor.utils.request.MaterialRequest;
import com.smart.tailor.utils.response.BrandMaterialResponse;
import com.smart.tailor.utils.response.ErrorDetail;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class BrandMaterialServiceImpl implements BrandMaterialService {
    private final MaterialService materialService;
    private final BrandService brandService;
    private final BrandMaterialRepository brandMaterialRepository;
    private final BrandMaterialMapper brandMaterialMapper;
    private final ExcelImportService excelImportService;
    private final SystemPropertiesService systemPropertiesService;
    private final Logger logger = LoggerFactory.getLogger(BrandMaterialServiceImpl.class);

    @Override
    @Transactional
    public void createBrandMaterial(BrandMaterialRequest brandMaterialRequest) {
        // Check If Brand ID is Existed
        var brand = brandService.findBrandById(UUID.fromString(brandMaterialRequest.getBrandID()))
                .orElseThrow(() -> new ItemNotFoundException("Can not find Brand with BrandID: " + brandMaterialRequest.getBrandID()));

        // Check if Category and Material is Existed or not
        var material = materialService.findByMaterialNameAndCategory_CategoryName(brandMaterialRequest.getMaterialName(), brandMaterialRequest.getCategoryName())
                .orElseThrow(() -> new ItemNotFoundException("Can not find Material with MaterialName: " + brandMaterialRequest.getMaterialName()));

        // Check Whether BrandMaterial is Existed or not
        // If Existed ==> Fail to Store Brand Material because Each Brand only enter one MaterialName belong to one CategoryName
        var brandMaterialExisted = brandMaterialRepository.findBrandMaterialByCategoryNameAndMaterialNameAndBrandID(brandMaterialRequest.getCategoryName(), brandMaterialRequest.getMaterialName(), UUID.fromString(brandMaterialRequest.getBrandID()));
        if (brandMaterialExisted != null) {
            throw new ItemAlreadyExistException(MessageConstant.BRAND_MATERIAL_IS_EXISTED);
        }

        int basePrice = brandMaterialRequest.getBasePrice();
        int brandPrice = brandMaterialRequest.getBrandPrice();
        var priceVariationPercentageForMaterial = Double.parseDouble(systemPropertiesService.getByName("PRICE_VARIATION_PERCENTAGE_FOR_MATERIAL").getPropertyValue());

        double lowerBound = basePrice * (1 - priceVariationPercentageForMaterial);
        double upperBound = basePrice * (1 + priceVariationPercentageForMaterial);

        int roundedLowerBound = (int) Math.ceil(lowerBound);
        int roundedUpperBound = (int) Math.ceil(upperBound);

        if (brandPrice < roundedLowerBound || brandPrice > roundedUpperBound) {
            throw new BadRequestException("Brand Price must be between " + roundedLowerBound + " and " + roundedUpperBound);
        }

        // When All condition pass, store data to BrandMaterial
        BrandMaterialKey brandMaterialKey = BrandMaterialKey
                .builder()
                .brandID(brand.getBrandID())
                .materialID(material.getMaterialID())
                .build();


        brandMaterialRepository.save(
                BrandMaterial
                        .builder()
                        .brandMaterialKey(brandMaterialKey)
                        .material(material)
                        .brand(brand)
                        .brandPrice(brandPrice)
                        .build()
        );
    }

    @Override
    public List<BrandMaterialResponse> getAllBrandMaterial() {
        return brandMaterialRepository
                .findAll()
                .stream()
                .map(brandMaterialMapper::mapperToBrandMaterialResponse)
                .toList();
    }

    @Override
    public List<BrandMaterialResponse> getAllBrandMaterialByBrandID(UUID brandID) {
        Optional<Brand> brand = brandService.findBrandById(brandID);
        if (brand.isEmpty()) return null;
        return brandMaterialRepository
                .findAll()
                .stream()
                .filter(brandMaterial -> (brandMaterial.getBrand().getBrandID().toString().equalsIgnoreCase(brandID.toString())))
                .map(brandMaterialMapper::mapperToBrandMaterialResponse)
                .toList();
    }

    @Override
    public void createBrandMaterialByImportExcelData(MultipartFile file, UUID brandID) {
        if (!excelImportService.isValidExcelFile(file)) {
            throw new ExcelFileInvalidFormatException(MessageConstant.INVALID_EXCEL_FILE_FORMAT);
        }
        try {
            List<Pair<Integer, BrandMaterialRequest>> brandMaterialRequests = new ArrayList<>();
            Set<BrandMaterialRequest> duplicateExcelData = new HashSet<>();
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = workbook.getSheet("Brand Material");

            if (sheet == null) {
                throw new ExcelFileNotSupportException(MessageConstant.WRONG_TYPE_OF_BRAND_MATERIAL_EXCEL_FILE);
            }

            logger.info("Inside createBrandMaterialByImportExcelData Method");
            boolean inValidData = false;

            List<ErrorDetail> errorFields = new ArrayList<>();
            int rowIndex = 2;
            while (rowIndex <= sheet.getLastRowNum()) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isRowCompletelyEmptyForBrandMaterial(row)) {
                    rowIndex++;
                    continue;
                }

                List<String> errors = new ArrayList<>();
                BrandMaterialRequest brandMaterialRequest = new BrandMaterialRequest();
                boolean rowDataValid = true;
                boolean brandPriceIsEmpty = false;
                boolean isValid = false;
                String message = "";
                for (int cellIndex = 0; cellIndex < 6; cellIndex++) {
                    Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (cell == null || cell.getCellType() == CellType.BLANK) {
                        if (cellIndex != 5) {
                            inValidData = true;
                            rowDataValid = false;
                            errors.add(getCellNameForBrandMaterial(cellIndex) + " at row Index " + (rowIndex + 1) + " is empty");
                        } else {
                            brandPriceIsEmpty = true;
                        }
                    } else {
                        switch (cellIndex) {
                            case 0:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    var categoryName = cell.getStringCellValue();
                                    if (categoryName.length() > 50) {
                                        errors.add(getCellNameForBrandMaterial(cellIndex) + " at row Index " + (rowIndex + 1) + " must not exceed 50 characters");
                                        inValidData = true;
                                        rowDataValid = false;
                                    } else {
                                        brandMaterialRequest.setCategoryName(categoryName);
                                    }
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    errors.add(getCellNameForBrandMaterial(cellIndex) + " at row Index " + (rowIndex + 1) + " must be data type string");
                                }
                                break;
                            case 1:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    var materialName = cell.getStringCellValue();
                                    if (materialName.length() > 50) {
                                        errors.add(getCellNameForBrandMaterial(cellIndex) + " at row Index " + (rowIndex + 1) + " must not exceed 50 characters");
                                        inValidData = true;
                                        rowDataValid = false;
                                    } else {
                                        brandMaterialRequest.setMaterialName(materialName);
                                    }
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    errors.add(getCellNameForBrandMaterial(cellIndex) + " at row Index " + (rowIndex + 1) + " must be data type string");
                                }
                                break;
                            case 2:
                                isValid = false;
                                long longValue = -1;
                                message = " require data type numeric";
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
                                if (isValid && longValue >= 0) {
                                    brandMaterialRequest.setHsCode(longValue);
                                } else {
                                    if (isValid && longValue < 0) {
                                        message = " require positive numeric";
                                    }
                                    inValidData = true;
                                    rowDataValid = false;
                                    errors.add(getCellNameForBrandMaterial(cellIndex) + " at row Index " + (rowIndex + 1) + message);
                                }
                                break;
                            case 3:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    var unit = cell.getStringCellValue();
                                    if (unit.length() > 50) {
                                        errors.add(getCellNameForBrandMaterial(cellIndex) + " at row Index " + (rowIndex + 1) + " must not exceed 50 characters");
                                        inValidData = true;
                                        rowDataValid = false;
                                    } else {
                                        brandMaterialRequest.setUnit(unit);
                                    }
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    errors.add(getCellNameForBrandMaterial(cellIndex) + " at row Index " + (rowIndex + 1) + " require data type string");
                                }
                                break;
                            case 4:
                                isValid = false;
                                Integer basePrice = -1;
                                message = " require data type numeric";
                                switch (cell.getCellType()) {
                                    case NUMERIC:
                                        basePrice = (int) cell.getNumericCellValue();
                                        isValid = true;
                                        break;
                                    case STRING:
                                        try {
                                            basePrice = Integer.parseInt(cell.getStringCellValue());
                                            isValid = true;
                                        } catch (NumberFormatException e) {
                                            isValid = false;
                                            System.out.println(e.getMessage());
                                        }
                                        break;
                                }
                                if (isValid && basePrice >= 0) {
                                    brandMaterialRequest.setBasePrice(basePrice);
                                } else {
                                    if (isValid && basePrice < 0) {
                                        message = " require positive numeric";
                                    }
                                    inValidData = true;
                                    rowDataValid = false;
                                    errors.add(getCellNameForBrandMaterial(cellIndex) + " at row Index " + (rowIndex + 1) + message);
                                }
                                break;
                            case 5:
                                isValid = false;
                                Integer brandPrice = -1;
                                boolean isEmpty = false;
                                message = " require data type numeric";
                                switch (cell.getCellType()) {
                                    case NUMERIC:
                                        brandPrice = (int) cell.getNumericCellValue();
                                        isValid = true;
                                        break;
                                    case STRING:
                                        String cellValue = cell.getStringCellValue().trim();
                                        if (cellValue.isEmpty()) {
                                            isEmpty = true;
                                            break;
                                        }
                                        try {
                                            brandPrice = Integer.parseInt(cellValue);
                                            isValid = true;
                                        } catch (NumberFormatException e) {
                                            isValid = false;
                                            System.out.println(e.getMessage());
                                        }
                                        break;
                                }
                                if (isEmpty) break;
                                if (isValid && brandPrice >= 0) {
                                    brandMaterialRequest.setBrandPrice(brandPrice);
                                } else {
                                    if (isValid && brandPrice < 0) {
                                        message = " require positive numeric";
                                    }
                                    inValidData = true;
                                    rowDataValid = false;
                                    errors.add(getCellNameForBrandMaterial(cellIndex) + " at row Index " + (rowIndex + 1) + message);
                                }
                                break;

                        }
                    }
                }
                if (!duplicateExcelData.add(brandMaterialRequest)) {
                    errors.add("Duplicate Brand Material Request Data at row Index " + (rowIndex + 1) + " in excel file");
                }

                var brand = brandService.findBrandById(brandID).orElse(null);
                if (brand == null) {
                    errors.add("Can not find Brand with BrandID " + brandID);
                }

                var material = materialService.findByMaterialNameAndCategory_CategoryName(brandMaterialRequest.getMaterialName(),
                        brandMaterialRequest.getCategoryName());

                var existedFullMaterial = materialService.isExistedMaterial(
                        MaterialRequest
                                .builder()
                                .materialName(brandMaterialRequest.getMaterialName())
                                .categoryName(brandMaterialRequest.getCategoryName())
                                .hsCode(brandMaterialRequest.getHsCode())
                                .unit(brandMaterialRequest.getUnit())
                                .basePrice(brandMaterialRequest.getBasePrice())
                                .build()
                );

                if (!existedFullMaterial || material.isEmpty()) {
                    errors.add("Material_Name at row Index " + (rowIndex + 1) + " not found");
                }

                if (rowDataValid && !brandPriceIsEmpty) {
                    var brandMaterialExisted = brandMaterialRepository.findBrandMaterialByCategoryNameAndMaterialNameAndBrandID(brandMaterialRequest.getCategoryName(),
                            brandMaterialRequest.getMaterialName(), brandID);

                    if (brandMaterialExisted != null && brandMaterialExisted.getBrandPrice().equals(brandMaterialRequest.getBrandPrice())) {
                        errors.add("Brand Material is existed at row Index " + (rowIndex + 1));
                    }

                    int basePrice = brandMaterialRequest.getBasePrice();
                    int brandPrice = brandMaterialRequest.getBrandPrice();
                    var priceVariationPercentageForMaterial = Double.parseDouble(systemPropertiesService.getByName("PRICE_VARIATION_PERCENTAGE_FOR_MATERIAL").getPropertyValue());

                    double lowerBound = basePrice * (1 - priceVariationPercentageForMaterial);
                    double upperBound = basePrice * (1 + priceVariationPercentageForMaterial);

                    int roundedLowerBound = (int) Math.ceil(lowerBound);
                    int roundedUpperBound = (int) Math.ceil(upperBound);

                    if (brandPrice < roundedLowerBound || brandPrice > roundedUpperBound) {
                        errors.add("Brand Price must be between " + roundedLowerBound + " and " + roundedUpperBound);
                    }

                    if (errors.isEmpty()) {
                        BrandMaterialKey brandMaterialKey = BrandMaterialKey
                                .builder()
                                .brandID(brand.getBrandID())
                                .materialID(material.get().getMaterialID())
                                .build();


                        brandMaterialRepository.save(
                                BrandMaterial
                                        .builder()
                                        .brandMaterialKey(brandMaterialKey)
                                        .material(material.get())
                                        .brand(brand)
                                        .brandPrice(brandPrice)
                                        .build()
                        );
                        brandMaterialRequests.add(Pair.of(rowIndex + 1, brandMaterialRequest));
                    }
                }

                if (!errors.isEmpty()) {
                    errorFields.add(new ErrorDetail(errors));
                }
                rowIndex++;
            }

            if (brandMaterialRequests.isEmpty() && errorFields.isEmpty()) {
                throw new BadRequestException("Brand Material Request excel file has empty data");
            }

            if (!errorFields.isEmpty()) {
                throw new ExcelFileInvalidDataTypeException("The Brand Material Excel File have " + brandMaterialRequests.size() + " create Success and " + errorFields.size() + " create Failure", errorFields);
            }

        } catch (IOException ex) {
            logger.error("Error processing excel file", ex);
            throw new ExcelFileInvalidFormatException(MessageConstant.INVALID_EXCEL_FILE_FORMAT);
        }
    }

    private boolean isRowCompletelyEmptyForBrandMaterial(Row row) {
        for (int cellIndex = 0; cellIndex < 6; cellIndex++) {
            Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private String getCellNameForBrandMaterial(int cellIndex) {
        switch (cellIndex) {
            case 0:
                return "Category_Name";
            case 1:
                return "Material_Name";
            case 2:
                return "HS_Code";
            case 3:
                return "Unit";
            case 4:
                return "Base_Price";
            case 5:
                return "Brand_Price";
            default:
                return "Unknown_Data";
        }
    }

    @Override
    public void updateBrandMaterial(BrandMaterialRequest brandMaterialRequest) {
        // Check If Brand Name is Existed
        var brand = brandService.findBrandById(UUID.fromString(brandMaterialRequest.getBrandID()))
                .orElseThrow(() -> new ItemNotFoundException("Can not find Brand with BrandID: " + brandMaterialRequest.getBrandID()));

        // Check if Category and Material is Existed or not
        var materialResponse = materialService.findByMaterialNameAndCategoryName(brandMaterialRequest.getMaterialName().toLowerCase(), brandMaterialRequest.getCategoryName().toLowerCase());
        if (materialResponse == null) {
            throw new ItemNotFoundException("Can not find Material with MaterialName: " + brandMaterialRequest.getMaterialName());
        }

        // Check Whether BrandMaterial is Existed or not
        // If Existed ==> Fail to Store Brand Material because Each Brand only enter one MaterialName belong to one CategoryName
        var brandMaterialExisted = brandMaterialRepository.findBrandMaterialByCategoryNameAndMaterialNameAndBrandID(brandMaterialRequest.getCategoryName(), brandMaterialRequest.getMaterialName(), UUID.fromString(brandMaterialRequest.getBrandID()));
        if (brandMaterialExisted == null) {
            throw new ItemAlreadyExistException(MessageConstant.BRAND_MATERIAL_IS_EXISTED);
        }

        int basePrice = brandMaterialRequest.getBasePrice();
        int brandPrice = brandMaterialRequest.getBrandPrice();
        var priceVariationPercentageForMaterial = Double.parseDouble(systemPropertiesService.getByName("PRICE_VARIATION_PERCENTAGE_FOR_MATERIAL").getPropertyValue());

        double lowerBound = basePrice * (1 - priceVariationPercentageForMaterial);
        double upperBound = basePrice * (1 + priceVariationPercentageForMaterial);

        int roundedLowerBound = (int) Math.ceil(lowerBound);
        int roundedUpperBound = (int) Math.ceil(upperBound);

        if (brandPrice < roundedLowerBound || brandPrice > roundedUpperBound) {
            throw new BadRequestException("Brand Price must be between " + roundedLowerBound + " and " + roundedUpperBound);
        }

        brandMaterialRepository.updateBrandMaterial(brandPrice, brandMaterialExisted.getBrandMaterialKey().getBrandID(), brandMaterialExisted.getBrandMaterialKey().getMaterialID());
    }

    @Override
    public Integer getMinPriceByMaterialID(UUID materialID) {
        var value = brandMaterialRepository.getMinPriceByMaterialID(materialID);
        if (value == null) {
            return 0;
        }
        return value;
    }

    @Override
    public Integer getMaxPriceByMaterialID(UUID materialID) {
        var value = brandMaterialRepository.getMaxPriceByMaterialID(materialID);
        if (value == null) {
            return 0;
        }
        return value;
    }

    @Override
    public Optional<BrandMaterial> getPriceByID(BrandMaterialKey key) {
        return brandMaterialRepository.findById(key);
    }

    @Override
    public Integer getBrandPriceByBrandIDAndMaterialID(UUID brandID, UUID materialID) {
        return brandMaterialRepository.getBrandPriceByBrandIDAndMaterialID(brandID, materialID);
    }
}
