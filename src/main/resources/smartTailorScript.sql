use smart_tailor_be;

-- roles
insert into roles(role_id, create_date, last_modified_date, role_name)
values (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null, 'CUSTOMER'),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null, 'ADMIN'),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null, 'MANAGER'),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null, 'EMPLOYEE'),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null, 'ACCOUNTANT'),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null, 'BRAND');

-- system_properties
insert into system_properties(property_id, create_date, last_modified_date, property_detail, property_name,
                              property_type, property_status, property_unit, property_value)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null,
        'The period within which customers can cancel their order.', 'CANCELLATION_TIME', 'DURATION', true, 'MINUTES',
        '150'),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null,
        'The duration required to find a suitable option for the order.', 'MATCHING_TIME', 'DURATION', true, 'MINUTES',
        '120'),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null,
        'The cost associated with the brand, specified in a particular currency.', 'BRAND_COST', 'COST', true, 'VND',
        '');

-- expert_tailoring
INSERT INTO expert_tailoring(expert_tailoring_id, expert_tailoring_name, size_image_url, model_image_url, status,
                             create_date, last_modified_date)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), 'shirtModel', 'IMAGE URL',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1720536717/clothes/mplmusqeleocefrsqrzf.png', true,
        current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 'hoodieModel', 'IMAGE URL',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1720536716/clothes/kn5egywxx2qj4wwzwxts.png', true,
        current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 'longSkirtModel', 'IMAGE URL',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1720536717/clothes/qf9prnqsfwofv9khp3cr.png', true,
        current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 'skirtFullModel', 'IMAGE URL',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1720536716/clothes/wibukocpklimkobvv5sa.png', true,
        current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 'womenSkirtTopModel', 'IMAGE URL',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1720536716/clothes/bwzhszbvmtqckax4oomk.png', true,
        current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 'womenSkirtBottomModel', 'IMAGE URL',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1720536716/clothes/jfsmttronovmyw9xz2cg.png', true,
        current_timestamp, null);

-- category
INSERT INTO category (category_id, create_date, last_modified_date, category_name)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 'Fabric'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 'Thread'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 'Cotton'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 'Ink'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 'Tape'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 'Label'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 'Button'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 'Bag'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 'Accessory'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 'Hang Tag'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 'Zipper');

INSERT INTO material (material_id, create_date, last_modified_date, base_price, hs_code, material_name, status, unit,
                      category_id)
VALUES
-- Fabric Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 15.0, 5407101000, 'Cotton Fabric', 1, 'meter',
 (SELECT category_id FROM category WHERE category_name = 'Fabric')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 20.0, 5407101001, 'Silk Fabric', 1, 'meter',
 (SELECT category_id FROM category WHERE category_name = 'Fabric')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 10.0, 5407101002, 'Linen Fabric', 1, 'meter',
 (SELECT category_id FROM category WHERE category_name = 'Fabric')),-- Thread Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 3.5, 5508100000, 'Polyester Thread', 1, 'spool',
 (SELECT category_id FROM category WHERE category_name = 'Thread')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 4.0, 5508100001, 'Nylon Thread', 1, 'spool',
 (SELECT category_id FROM category WHERE category_name = 'Thread')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 5.0, 5508100002, 'Cotton Thread', 1, 'spool',
 (SELECT category_id FROM category WHERE category_name = 'Thread')),

-- Cotton Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 12.0, 5201000000, 'Organic Cotton', 1, 'kilogram',
 (SELECT category_id FROM category WHERE category_name = 'Cotton')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 10.0, 5201000001, 'Recycled Cotton', 1, 'kilogram',
 (SELECT category_id FROM category WHERE category_name = 'Cotton')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 8.0, 5201000002, 'Combed Cotton', 1, 'kilogram',
 (SELECT category_id FROM category WHERE category_name = 'Cotton')),

-- Ink Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 25.0, 3215190000, 'Textile Ink', 1, 'liter',
 (SELECT category_id FROM category WHERE category_name = 'Ink')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 20.0, 3215190001, 'Silk Screen Ink', 1, 'liter',
 (SELECT category_id FROM category WHERE category_name = 'Ink')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 30.0, 3215190002, 'Dye Sublimation Ink', 1, 'liter',
 (SELECT category_id FROM category WHERE category_name = 'Ink')),

-- Tape Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 1.2, 4823908500, 'Masking Tape', 1, 'roll',
 (SELECT category_id FROM category WHERE category_name = 'Tape')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 1.5, 4823908501, 'Double-Sided Tape', 1, 'roll',
 (SELECT category_id FROM category WHERE category_name = 'Tape')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 1.0, 4823908502, 'Packing Tape', 1, 'roll',
 (SELECT category_id FROM category WHERE category_name = 'Tape')),

-- Label Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 0.05, 4821102000, 'Woven Label', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Label')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 0.07, 4821102001, 'Printed Label', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Label')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 0.09, 4821102002, 'Heat Transfer Label', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Label')),

-- Button Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 0.1, 9606210000, 'Plastic Button', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Button')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 0.2, 9606210001, 'Metal Button', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Button')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 0.3, 9606210002, 'Wooden Button', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Button')),

-- Bag Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 2.5, 4202929100, 'Eco-Friendly Bag', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Bag')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 3.0, 4202929101, 'Reusable Shopping Bag', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Bag')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 4.0, 4202929102, 'Canvas Bag', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Bag')),

-- Accessory Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 5.0, 3926909700, 'Sewing Accessory Kit', 1, 'set',
 (SELECT category_id FROM category WHERE category_name = 'Accessory')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 6.0, 3926909701, 'Zipper Pulls', 1, 'set',
 (SELECT category_id FROM category WHERE category_name = 'Accessory')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 7.0, 3926909702, 'Sewing Needles', 1, 'set',
 (SELECT category_id FROM category WHERE category_name = 'Accessory')),

-- Hang Tag Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 0.8, 4911991000, 'Paper Hang Tag', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Hang Tag')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 0.9, 4911991001, 'Plastic Hang Tag', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Hang Tag')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 1.0, 4911991002, 'Metal Hang Tag', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Hang Tag')),

-- Zipper Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 1.5, 9607110000, 'Metal Zipper', 1, 'meter',
 (SELECT category_id FROM category WHERE category_name = 'Zipper')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 1.2, 9607110001, 'Plastic Zipper', 1, 'meter',
 (SELECT category_id FROM category WHERE category_name = 'Zipper')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 1.8, 9607110002, 'Invisible Zipper', 1, 'meter',
 (SELECT category_id FROM category WHERE category_name = 'Zipper'));

-- size
INSERT INTO size (size_id, size_name, status, create_date, last_modified_date)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), 'S', true, current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 'L', true, current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 'M', true, current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 'XL', true, current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 'XXL', true, current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 'XXXL', true, current_timestamp, null);

-- labor_quantity
INSERT INTO labor_quantity (labor_quantity_id, labor_quantity_min_quantity, labor_quantity_max_quantity,
                            labor_quantity_min_price, labor_quantity_max_price, status, create_date, last_modified_date)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), 7, 10, 50.0, 200.0, true, current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 16, 23, 50.0, 200.0, true, current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 11, 20, 100.0, 500.0, true, current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 5, 25, 50.0, 200.0, true, current_timestamp, null);