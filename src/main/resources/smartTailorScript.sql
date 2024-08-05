use smart_tailor_be;

-- INSERT INTO ROLES
insert into roles(role_id, create_date, last_modified_date, role_name)
values (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null, 'CUSTOMER'),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null, 'ADMIN'),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null, 'MANAGER'),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null, 'EMPLOYEE'),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null, 'ACCOUNTANT'),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null, 'BRAND');

-- INSERT INTO SYSTEM PROPERTIES
insert into system_properties(property_id, create_date, last_modified_date, property_detail, property_name,
                              property_type, property_status, property_unit, property_value)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null,
        'The period within which customers can cancel their order.', 'CANCELLATION_TIME', 'DURATION', true, 'MINUTES',
        '150'),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null,
        'The duration required to find a suitable option for the order.', 'MATCHING_TIME', 'DURATION', true, 'MINUTES',
        '2'),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null,
        'The cost associated with the brand, specified in a particular currency.', 'BRAND_COST', 'COST', true, 'VND',
        ''),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null,
        'The percent for deposit.', 'DEPOSIT_PERCENT', 'PERCENT', true, '%',
        '50'),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null,
        'The product can produce in one day.', 'BRAND_PRODUCTIVITY', 'PIECE', true, 'PIECE',
        ''),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null,
        'The number for divide.', 'DIVIDE_NUMBER', 'NUMBER', true, 'INT',
        '100'),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null,
        'The rate for Brand have Order Status ahead of schedule.', 'RATE_AHEAD_SCHEDULE', 'NUMBER', true, 'FLOAT',
        '0.25'),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null,
        'The rate for Brand have Order Status late of schedule.', 'RATE_LATE_SCHEDULE', 'NUMBER', true, 'FLOAT',
        '0.75'),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null, 'The duration for which an unverified account can exist before being deleted. Once this time expires, the account will be removed.',
        'TIME_BEFORE_ACCOUNT_DELETION', 'NUMBER', true, 'INT','12'),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null,
        'The percentage range within which the price for Material can vary. Prices outside this range will not be accepted for Material pricing.',
        'PRICE_VARIATION_PERCENTAGE_FOR_MATERIAL', 'NUMBER', true, 'FLOAT', '0.07');

-- INSERT INTO SYSTEM IMAGE
INSERT INTO system_image (image_id, image_name, image_url, image_status, image_type, is_premium, create_date,
                          last_modified_date)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), 'Happy Sad White',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1721382631/system-item/zldstwj0wsqw7kmaqxb0.jpg', TRUE,
        'ICON', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Happy Sad Nigga',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1721382623/system-item/qcls0ellfxwcmr3owkfj.jpg', TRUE,
        'ICON', FALSE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Music Chilling',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1721382631/system-item/iws75egp5pya3wrad22e.jpg', TRUE,
        'IMAGE', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Just Do It',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1721382631/system-item/ghgndgsazhr1fsw9s02u.jpg', TRUE,
        'IMAGE', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'No Pain No Gain',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1721382622/system-item/fzdonu32wlczedt4rl2r.jpg', TRUE,
        'IMAGE', FALSE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Placeit',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1721382631/system-item/xovhntso2wvzutywphvz.jpg', TRUE,
        'IMAGE', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Sales',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1721382631/system-item/d5k2glgxnfcfj3hghc6a.jpg', TRUE,
        'ICON', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Shop Sustences',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1721382624/system-item/qqkmptye2kuageh1dsjr.jpg', TRUE,
        'ICON', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Fashion',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1721382623/system-item/on7eyszvsanisllwsdlw.jpg', TRUE,
        'ICON', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'MR.P',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1721382623/system-item/g7ztxjyyafjrwbue4vcm.jpg', TRUE,
        'IMAGE', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'KarmaShek',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1721382616/system-item/n2i4r52dkn86fqdgc8hi.jpg', TRUE,
        'IMAGE', FALSE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Dog Cute',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1721382616/system-item/jhmck5g6akn6g45u7zes.jpg', TRUE,
        'ICON', FALSE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Orange',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1721382616/system-item/vpta9r0m0uzmtvhxrmll.jpg', TRUE,
        'IMAGE', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Happy',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1721382616/system-item/f7m0dsbfzmwwsziudhrp.jpg', TRUE,
        'IMAGE', FALSE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Life',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1721382616/system-item/ie0crj3nifajmvti4mcf.jpg', TRUE,
        'IMAGE', FALSE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'SuiSailet',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1721382607/system-item/cxemu0v5yymkgrimsg5p.jpg', TRUE,
        'ICON', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Sautter',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1721382607/system-item/eu8xmqu8afgkhi8ditvt.jpg', TRUE,
        'ICON', FALSE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Bus',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1721382606/system-item/fewdcw6xcx28qodd4cul.jpg', TRUE,
        'ICON', FALSE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Box',
        'https://i.pinimg.com/236x/53/b1/fd/53b1fd253e54a9fed689935f8982359b.jpg', TRUE, 'ICON', FALSE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Shopping',
        'https://i.pinimg.com/564x/b6/b3/c2/b6b3c266de4272978816746d5e6a982b.jpg', TRUE, 'ICON', FALSE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Shop',
        'https://i.pinimg.com/564x/01/40/78/014078e022373b6eb0f5600fff66cbbb.jpg', TRUE, 'ICON', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Melody',
        'https://i.pinimg.com/564x/3c/31/f2/3c31f2936b29c622bf39d69d47bb77c4.jpg', TRUE, 'ICON', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Candy',
        'https://i.pinimg.com/564x/1e/98/ff/1e98fff5c3c7781ca0f9b883a98f3b0e.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Comments',
        'https://i.pinimg.com/564x/0e/a1/d8/0ea1d81f6ee0df19d36e3d319f9031e7.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Loive',
        'https://i.pinimg.com/236x/0c/fc/e5/0cfce5e80520edead31058a6a78c282f.jpg', TRUE, 'IMAGE', FALSE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Sunday Morning',
        'https://i.pinimg.com/564x/84/de/25/84de25c3653f3813a05c7df509006757.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'London',
        'https://i.pinimg.com/236x/74/15/6e/74156e2afcb9c557a9c9141f5781b48e.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Quote',
        'https://i.pinimg.com/236x/ea/e1/8d/eae18dc4db431d7892cd7cb82a6726db.jpg', TRUE, 'IMAGE', FALSE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'lLive',
        'https://i.pinimg.com/236x/08/7a/b5/087ab5cf1865a962a942ec9336296729.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Cherry',
        'https://i.pinimg.com/564x/b1/ab/10/b1ab10f770bbd6da1caa7a6cc46b14ed.jpg', TRUE, 'ICON', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Bubble',
        'https://i.pinimg.com/564x/a4/dd/02/a4dd02c4b6d16981d2aee6eb9f448c3e.jpg', TRUE, 'ICON', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Duck',
        'https://i.pinimg.com/564x/ac/c3/2c/acc32c5e9614d1e68f796833188d3c49.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Ducky',
        'https://i.pinimg.com/564x/f8/91/b5/f891b53d10e93bc6edb25bc73d59dc05.jpg', TRUE, 'IMAGE', FALSE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), '8 Balls',
        'https://i.pinimg.com/564x/fa/89/57/fa89579d742e87c08921bbfec1c1f6c1.jpg', TRUE, 'ICON', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Chilly',
        'https://i.pinimg.com/564x/e8/8d/19/e88d19684158e22460292e77c6ceedf5.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Coca',
        'https://i.pinimg.com/564x/e2/cd/cc/e2cdcc9a04afafca80637bc699afd66e.jpg', TRUE, 'ICON', FALSE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Sweet',
        'https://i.pinimg.com/564x/72/f3/5f/72f35fe198f34cac282fb3868fbfcf61.jpg', TRUE, 'ICON', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Smile',
        'https://i.pinimg.com/564x/72/f3/5f/72f35fe198f34cac282fb3868fbfcf61.jpg', TRUE, 'IMAGE', FALSE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Thunder',
        'https://i.pinimg.com/564x/f1/05/d6/f105d671c6ce8940ea34caee0fea5bd2.jpg', TRUE, 'ICON', TRUE, NOW(6), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 'T1',
        'https://i.pinimg.com/564x/b2/0a/65/b20a653428d9f1a035e7d0bf69c1fcc4.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL);

-- INSERT INTO EXPERT TAILORING
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

-- INSERT INTO CATEGORY
INSERT INTO category (category_id, create_date, status, last_modified_date, category_name)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), true, NULL, 'Fabric'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), true, NULL, 'Thread'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), true, NULL, 'Cotton'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), true, NULL, 'Ink'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), true, NULL, 'Tape'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), true, NULL, 'Label'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), true, NULL, 'Button'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), true, NULL, 'Bag'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), true, NULL, 'Accessory'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), true, NULL, 'Hang Tag'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), true, NULL, 'Zipper'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), true, NULL, 'Manual Printing'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), true, NULL, 'Heat Printing'),
       (UNHEX(REPLACE(UUID(), '-', '')), NOW(6), true, NULL, 'Embroider');

-- INSERT INTO MATERIAL
INSERT INTO material (material_id, create_date, last_modified_date, base_price, hs_code, material_name, status, unit,
                      category_id)
VALUES
-- Fabric Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 4000, 5407101000, 'Cotton Fabric', 1, 'meter',
 (SELECT category_id FROM category WHERE category_name = 'Fabric')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 5100, 5407101001, 'Silk Fabric', 1, 'meter',
 (SELECT category_id FROM category WHERE category_name = 'Fabric')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 4700, 5407101002, 'Linen Fabric', 1, 'meter',
 (SELECT category_id FROM category WHERE category_name = 'Fabric')),

-- Thread Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 1300, 5508100000, 'Polyester Thread', 1, 'spool',
 (SELECT category_id FROM category WHERE category_name = 'Thread')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 8000, 5508100001, 'Nylon Thread', 1, 'spool',
 (SELECT category_id FROM category WHERE category_name = 'Thread')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 1500, 5508100002, 'Cotton Thread', 1, 'spool',
 (SELECT category_id FROM category WHERE category_name = 'Thread')),

-- Cotton Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 3800, 5201000000, 'Organic Cotton', 1, 'kilogram',
 (SELECT category_id FROM category WHERE category_name = 'Cotton')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 4200, 5201000001, 'Recycled Cotton', 1, 'kilogram',
 (SELECT category_id FROM category WHERE category_name = 'Cotton')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 3300, 5201000002, 'Combed Cotton', 1, 'kilogram',
 (SELECT category_id FROM category WHERE category_name = 'Cotton')),

-- Ink Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 1200, 3215190000, 'Textile Ink', 1, 'liter',
 (SELECT category_id FROM category WHERE category_name = 'Ink')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 1700, 3215190001, 'Silk Screen Ink', 1, 'liter',
 (SELECT category_id FROM category WHERE category_name = 'Ink')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 3500, 3215190002, 'Dye Sublimation Ink', 1, 'liter',
 (SELECT category_id FROM category WHERE category_name = 'Ink')),

-- Tape Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 7000, 4823908500, 'Masking Tape', 1, 'roll',
 (SELECT category_id FROM category WHERE category_name = 'Tape')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 9000, 4823908501, 'Double-Sided Tape', 1, 'roll',
 (SELECT category_id FROM category WHERE category_name = 'Tape')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 1700, 4823908502, 'Packing Tape', 1, 'roll',
 (SELECT category_id FROM category WHERE category_name = 'Tape')),

-- Label Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 1800, 4821102000, 'Woven Label', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Label')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 6600, 4821102001, 'Printed Label', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Label')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 5800, 4821102002, 'Heat Transfer Label', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Label')),

-- Button Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 8500, 9606210000, 'Plastic Button', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Button')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 1300, 9606210001, 'Metal Button', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Button')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 2800, 9606210002, 'Wooden Button', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Button')),

-- Bag Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 7500, 4202929100, 'Eco-Friendly Bag', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Bag')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 1600, 4202929101, 'Reusable Shopping Bag', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Bag')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 4700, 4202929102, 'Canvas Bag', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Bag')),

-- Accessory Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 2760, 3926909700, 'Sewing Accessory Kit', 1, 'set',
 (SELECT category_id FROM category WHERE category_name = 'Accessory')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 1150, 3926909701, 'Zipper Pulls', 1, 'set',
 (SELECT category_id FROM category WHERE category_name = 'Accessory')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 1500, 3926909702, 'Sewing Needles', 1, 'set',
 (SELECT category_id FROM category WHERE category_name = 'Accessory')),

-- Hang Tag Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 9500, 4911991000, 'Paper Hang Tag', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Hang Tag')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 1090, 4911991001, 'Plastic Hang Tag', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Hang Tag')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 4000, 4911991002, 'Metal Hang Tag', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Hang Tag')),

-- Zipper Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 5000, 9607110000, 'Metal Zipper', 1, 'meter',
 (SELECT category_id FROM category WHERE category_name = 'Zipper')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 7000, 9607110001, 'Plastic Zipper', 1, 'meter',
 (SELECT category_id FROM category WHERE category_name = 'Zipper')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 4600, 9607110002, 'Invisible Zipper', 1, 'meter',
 (SELECT category_id FROM category WHERE category_name = 'Zipper')),

-- Manual Printing Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 3300, 3215, 'Screen Printing Ink', 1, 'liter',
 (SELECT category_id FROM category WHERE category_name = 'Manual Printing')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 9400, 48169040, 'Heat Transfer Paper', 1, 'sheet',
 (SELECT category_id FROM category WHERE category_name = 'Manual Printing')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 2400, 55111010, 'Embroidery Thread', 1, 'spool',
 (SELECT category_id FROM category WHERE category_name = 'Manual Printing')),

-- Heat Printing Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 2300, 84629950, 'Heat Press Machine', 1, 'unit',
 (SELECT category_id FROM category WHERE category_name = 'Heat Printing')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 5600, 48169040, 'Transfer Vinyl', 1, 'roll',
 (SELECT category_id FROM category WHERE category_name = 'Heat Printing')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 1800, 39199010, 'Thermal Tape', 1, 'roll',
 (SELECT category_id FROM category WHERE category_name = 'Heat Printing')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 5600, 39199020, 'Heat Transfer Film', 1, 'roll',
 (SELECT category_id FROM category WHERE category_name = 'Heat Printing')),

-- Embroider Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 9400, 44140000, 'Embroidery Hoops', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Embroider')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 9300, 73199010, 'Embroidery Needles', 1, 'pack',
 (SELECT category_id FROM category WHERE category_name = 'Embroider')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 1300, 62043300, 'Embroidery Floss', 1, 'skein',
 (SELECT category_id FROM category WHERE category_name = 'Embroider')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 1200, 73199020, 'Embroidery Scissors', 1, 'pair',
 (SELECT category_id FROM category WHERE category_name = 'Embroider'));

-- INSERT INTO SIZE
INSERT INTO size (size_id, size_name, status, create_date, last_modified_date)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), 'S', true, current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 'L', true, current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 'M', true, current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 'XL', true, current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 'XXL', true, current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 'XXXL', true, current_timestamp, null);

-- INSERT INTO LABOR QUANTITY
INSERT INTO labor_quantity (labor_quantity_id, labor_quantity_min_quantity, labor_quantity_max_quantity,
                            labor_quantity_min_price, labor_quantity_max_price, status, create_date, last_modified_date)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), 1, 100, 50000, 80000, true, NOW(), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 101, 500, 45000, 75000, true, NOW(), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 501, 1000, 40000, 70000, true, NOW(), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 1001, 2000, 35000, 65000, true, NOW(), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 2001, 999999, 30000, 60000, true, NOW(), NULL);


-- INSERT INTO USERS with password Aa@123456
INSERT INTO users (user_id, email, password, full_name, language, phone_number, role_id, provider, user_status,
                   image_url, create_date, last_modified_date)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), 'nguyenvanquan@gmail.com',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Nguyen Van Quan', 'vietnam', '0902818618',
        (SELECT role_id FROM roles WHERE role_name = 'CUSTOMER'), 'LOCAL', 'ACTIVE', 'http://example.com/image1.jpg',
        current_timestamp, null),

       (UNHEX(REPLACE(UUID(), '-', '')), 'hoanganhduy1122@gmail.com',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Ha Anh Duy', 'vietnam', '0961782569',
        (SELECT role_id FROM roles WHERE role_name = 'CUSTOMER'), 'LOCAL', 'ACTIVE', 'http://example.com/image2.jpg',
        current_timestamp, null),

       (UNHEX(REPLACE(UUID(), '-', '')), 'chuongquocviet123@gmail.com',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Chuong Quoc Viet', 'vietnam', '0904357264',
        (SELECT role_id FROM roles WHERE role_name = 'CUSTOMER'), 'LOCAL', 'ACTIVE', 'http://example.com/image1.jpg',
        current_timestamp, null),

       (UNHEX(REPLACE(UUID(), '-', '')), 'tammtse161087@fpt.edu.vn',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Thanh Tam', 'vietnam', '0903826475',
        (SELECT role_id FROM roles WHERE role_name = 'BRAND'), 'LOCAL', 'ACTIVE', 'http://example.com/image2.jpg',
        current_timestamp, null),

       (UNHEX(REPLACE(UUID(), '-', '')), 'tunmse161130@fpt.edu.vn',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Minh Tu', 'vietnam', '0904659543',
        (SELECT role_id FROM roles WHERE role_name = 'BRAND'), 'LOCAL', 'ACTIVE', 'http://example.com/image1.jpg',
        current_timestamp, null),

       (UNHEX(REPLACE(UUID(), '-', '')), 'truongnhlse160191@fpt.edu.vn',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Lam Truong', 'vietnam', '0905638465',
        (SELECT role_id FROM roles WHERE role_name = 'BRAND'), 'LOCAL', 'ACTIVE', 'http://example.com/image2.jpg',
        current_timestamp, null),

       (UNHEX(REPLACE(UUID(), '-', '')), 'ngohongquang999@gmail.com',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Ngo Hong Quang', 'vietnam', '0903826475',
        (SELECT role_id FROM roles WHERE role_name = 'BRAND'), 'LOCAL', 'ACTIVE', 'http://example.com/image2.jpg',
        current_timestamp, null),

       (UNHEX(REPLACE(UUID(), '-', '')), 'doanthuan97@gmail.com',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Doan Thu An', 'vietnam', '0904659543',
        (SELECT role_id FROM roles WHERE role_name = 'BRAND'), 'LOCAL', 'ACTIVE', 'http://example.com/image1.jpg',
        current_timestamp, null),

       (UNHEX(REPLACE(UUID(), '-', '')), 'phamthanhgiang458@gmail.com',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Pham Thanh Giang', 'vietnam', '0905638465',
        (SELECT role_id FROM roles WHERE role_name = 'BRAND'), 'LOCAL', 'ACTIVE', 'http://example.com/image2.jpg',
        current_timestamp, null),

       (UNHEX(REPLACE(UUID(), '-', '')), 'adminsmarttailor123@gmail.com',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Admin Smart Tailor', 'vietnam', '0914633465',
        (SELECT role_id FROM roles WHERE role_name = 'ADMIN'), 'LOCAL', 'ACTIVE', 'http://example.com/image2.jpg',
        current_timestamp, null),

       (UNHEX(REPLACE(UUID(), '-', '')), 'managersmarttailor123@gmail.com',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Manager Smart Tailor', 'vietnam',
        '09146376465',
        (SELECT role_id FROM roles WHERE role_name = 'MANAGER'), 'LOCAL', 'ACTIVE', 'http://example.com/image2.jpg',
        current_timestamp, null),

       (UNHEX(REPLACE(UUID(), '-', '')), 'employeesmarttailor123@gmail.com',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Employee Smart Tailor', 'vietnam',
        '09146993465',
        (SELECT role_id FROM roles WHERE role_name = 'EMPLOYEE'), 'LOCAL', 'ACTIVE', 'http://example.com/image2.jpg',
        current_timestamp, null),

       (UNHEX(REPLACE(UUID(), '-', '')), 'accountantsmarttailor123@gmail.com',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Accountant Smart Tailor', 'vietnam',
        '09146993290',
        (SELECT role_id FROM roles WHERE role_name = 'ACCOUNTANT'), 'LOCAL', 'ACTIVE', 'http://example.com/image2.jpg',
        current_timestamp, null);


-- INSERT INTO CUSTOMER
INSERT INTO customer (customer_id, gender, date_of_birth, address, ward, district, province, number_of_violations,
                      create_date, last_modified_date)
VALUES ((SELECT user_id FROM users WHERE email = 'nguyenvanquan@gmail.com'), true, '1990-01-01', '240 Phạm Văn Đồng',
        'Hiệp Bình Chánh', 'Thủ Đức', 'Thành phố Hồ Chí Minh', 0, current_timestamp, null),

       ((SELECT user_id FROM users WHERE email = 'hoanganhduy1122@gmail.com'), true, '1990-01-02', '50 Lê Văn Việt',
        'Hiệp Phú', 'Quận 9', 'Thành phố Hồ Chí Minh', 0, current_timestamp, null),

       ((SELECT user_id FROM users WHERE email = 'chuongquocviet123@gmail.com'), true, '1990-01-03', '81 Nguyễn Xiển',
        'Long Thạnh Mỹ', 'Quận 9', 'Thành phố Hồ Chí Minh', 0, current_timestamp, null);


-- INSERT INTO BRAND
INSERT INTO brand (brand_id, brand_name, rating, number_of_ratings, total_rating_score, bank_name, account_number,
                   account_name, qr_payment, address, ward,
                   district, province, brand_status, number_of_violations, create_date, last_modified_date)
VALUES ((SELECT user_id FROM users WHERE email = 'tammtse161087@fpt.edu.vn'), 'Nike Brand', 1, 1, 1, 'Brand Bank',
        '1234567890', 'Nike', 'http://example.com/qr_nike.jpg', '740 Nguyễn Xiển', ' Long Thạnh Mỹ', 'Quận 9',
        'Thành phố Hồ Chí Minh', 'ACCEPT', 0, current_timestamp, null),

       ((SELECT user_id FROM users WHERE email = 'tunmse161130@fpt.edu.vn'), 'Adidas Brand', 1, 1, 1, 'Brand Bank',
        '0987654321', 'Adidas', 'http://example.com/qr_adidas.jpg', '269 Đ. Liên Phường', 'Phước Long B', 'Quận 9',
        'Thành phố Hồ Chí Minh', 'ACCEPT', 0, current_timestamp, null),

       ((SELECT user_id FROM users WHERE email = 'truongnhlse160191@fpt.edu.vn'), 'Puma Brand', 1, 1, 1, 'Brand Bank',
        '1357924680', 'Puma', 'http://example.com/qr_puma.jpg', '441 Lê Văn Việt', 'Tăng Nhơn Phú A', 'Quận 9',
        'Thành phố Hồ Chí Minh', 'ACCEPT', 0, current_timestamp, null),

       ((SELECT user_id FROM users WHERE email = 'ngohongquang999@gmail.com'), 'Hong Quang Brand', 1, 1, 1,
        'Brand Bank',
        '1357924680', 'Puma', 'http://example.com/qr_puma.jpg', '857 Phạm Văn Đồng', 'Linh Tây', 'Thủ Đức',
        'Thành phố Hồ Chí Minh', 'ACCEPT', 0, current_timestamp, null),

       ((SELECT user_id FROM users WHERE email = 'doanthuan97@gmail.com'), 'Thuan An Brand', 1, 1, 1, 'Brand Bank',
        '1357924680', 'Puma', 'http://example.com/qr_puma.jpg', '432 Đ. Liên Phường', 'Phước Long B', 'Quận 9',
        'Thành phố Hồ Chí Minh', 'ACCEPT', 0, current_timestamp, null),

       ((SELECT user_id FROM users WHERE email = 'phamthanhgiang458@gmail.com'), 'Thanh Giang Brand', 1, 1, 1,
        'Brand Bank',
        '1357924680', 'Puma', 'http://example.com/qr_puma.jpg', '77C Trần Ngọc Diện', 'Thảo Điền', 'Thủ Đức',
        'Thành phố Hồ Chí Minh', 'ACCEPT', 0, current_timestamp, null);

-- INSERT SIZE EXPERT TAILORING

INSERT INTO size_expert_tailoring (expert_tailoring_id, size_id, ratio, create_date, last_modified_date)
VALUES
-- hoodieModel
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'hoodieModel'),
 (SELECT size_id FROM size WHERE size_name = 'S'), 1, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'hoodieModel'),
 (SELECT size_id FROM size WHERE size_name = 'M'), 1.2, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'hoodieModel'),
 (SELECT size_id FROM size WHERE size_name = 'L'), 1.4, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'hoodieModel'),
 (SELECT size_id FROM size WHERE size_name = 'XL'), 1.6, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'hoodieModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXL'), 1.8, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'hoodieModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXXL'), 2, CURRENT_TIMESTAMP, NULL),

-- shirtModel
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'S'), 1, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'M'), 1.15, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'L'), 1.3, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'XL'), 1.5, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXL'), 1.7, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXXL'), 1.9, CURRENT_TIMESTAMP, NULL),

-- longSkirtModel
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'longSkirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'S'), 1, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'longSkirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'M'), 1.25, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'longSkirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'L'), 1.5, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'longSkirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'XL'), 1.75, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'longSkirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXL'), 2, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'longSkirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXXL'), 2.25, CURRENT_TIMESTAMP, NULL),

-- skirtFullModel
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'),
 (SELECT size_id FROM size WHERE size_name = 'S'), 1, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'),
 (SELECT size_id FROM size WHERE size_name = 'M'), 1.1, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'),
 (SELECT size_id FROM size WHERE size_name = 'L'), 1.3, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'),
 (SELECT size_id FROM size WHERE size_name = 'XL'), 1.5, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXL'), 1.7, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXXL'), 1.9, CURRENT_TIMESTAMP, NULL),

-- womenSkirtTopModel
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'),
 (SELECT size_id FROM size WHERE size_name = 'S'), 1, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'),
 (SELECT size_id FROM size WHERE size_name = 'M'), 1.2, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'),
 (SELECT size_id FROM size WHERE size_name = 'L'), 1.4, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'),
 (SELECT size_id FROM size WHERE size_name = 'XL'), 1.6, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXL'), 1.8, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXXL'), 2, CURRENT_TIMESTAMP, NULL),

-- womenSkirtBottomModel
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtBottomModel'),
 (SELECT size_id FROM size WHERE size_name = 'S'), 1, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtBottomModel'),
 (SELECT size_id FROM size WHERE size_name = 'M'), 1.2, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtBottomModel'),
 (SELECT size_id FROM size WHERE size_name = 'L'), 1.4, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtBottomModel'),
 (SELECT size_id FROM size WHERE size_name = 'XL'), 1.6, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtBottomModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXL'), 1.8, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtBottomModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXXL'), 2, CURRENT_TIMESTAMP, NULL);

-- INSERT BRAND EXPERT TAILORING
INSERT INTO brand_expert_tailoring(brand_id, expert_tailoring_id, create_date, last_modified_date)
VALUES

-- Brand with email tunmse161130@fpt.edu.vn have ExpertTailoringName: shirtModel,
((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'shirtModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'hoodieModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'longSkirtModel'), current_timestamp,
 null),

-- Brand with email tammtse161087@fpt.edu.vn have ExpertTailoringName: shirtModel,
((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'shirtModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'),
 current_timestamp, null),

-- Brand with email tammtse161087@fpt.edu.vn have ExpertTailoringName: shirtModel,
((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'shirtModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'),
 current_timestamp, null),

-- Brand with email ngohongquang999@gmail.com have ExpertTailoringName: shirtModel,
((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'ngohongquang999@gmail.com'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'shirtModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'ngohongquang999@gmail.com'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'hoodieModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'ngohongquang999@gmail.com'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'longSkirtModel'), current_timestamp,
 null),

-- Brand with email doanthuan97@gmail.com have ExpertTailoringName: shirtModel,
((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'doanthuan97@gmail.com'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'shirtModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'doanthuan97@gmail.com'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'doanthuan97@gmail.com'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'),
 current_timestamp, null),

-- Brand with email doanthuan97@gmail.com have ExpertTailoringName: shirtModel,
((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'phamthanhgiang458@gmail.com'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'womenSkirtBottomModel'),
 current_timestamp, null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'phamthanhgiang458@gmail.com'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'phamthanhgiang458@gmail.com'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'),
 current_timestamp, null);


-- INSERT BRAND LABOR QUANTITY
INSERT INTO brand_labor_quantity(brand_id, labor_quantity_id, brand_labor_cost_per_quantity, status, create_date,
                                 last_modified_date)
values
-- Brand Labor Quantity with Email tunmse161130@fpt.edu.vn
((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 1), 65469, true, current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 101), 65242, true, current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 501), 61469, true, current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 1001), 42521, true,
 current_timestamp, null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 2001), 39240, true,
 current_timestamp, null),

-- Brand Labor Quantity with Email tammtse161087@fpt.edu.vn
((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 1), 61301, true, current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 101), 58891, true, current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 501), 56075, true, current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 1001), 50651, true,
 current_timestamp, null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 2001), 48904, true,
 current_timestamp, null),

-- Brand Labor Quantity with Email truongnhlse160191@fpt.edu.vn
((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 1), 62686, true, current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 101), 48843, true, current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 501), 48436, true, current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 1001), 39240, true,
 current_timestamp, null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 2001), 36038, true,
 current_timestamp, null);

-- INSERT INTO BRAND MATERIAL
INSERT INTO brand_material (brand_id, material_id, brand_price, create_date, last_modified_date)
VALUES
--     BRAND MATERIAL WITH EMAIL tunmse161130@fpt.edu.vn
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Cotton Fabric'), 4007, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Silk Fabric'), 5082, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Linen Fabric'), 4681, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Polyester Thread'), 1277, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Nylon Thread'), 8003, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Cotton Thread'), 1480, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Organic Cotton'), 3786, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Recycled Cotton'), 4192, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Combed Cotton'), 3298, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Textile Ink'), 1174, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Silk Screen Ink'), 1726, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Dye Sublimation Ink'), 3473, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Masking Tape'), 6991, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Double-Sided Tape'), 8975, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Packing Tape'), 1730, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Woven Label'), 1790, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Printed Label'), 6617, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Transfer Label'), 5821, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Plastic Button'), 8527, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Metal Button'), 1325, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Wooden Button'), 2805, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Eco-Friendly Bag'), 7517, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Reusable Shopping Bag'), 1588, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Canvas Bag'), 4705, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Sewing Accessory Kit'), 2770, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Zipper Pulls'), 1163, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Sewing Needles'), 1520, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Paper Hang Tag'), 9520, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Plastic Hang Tag'), 1105, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Metal Hang Tag'), 4018, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Metal Zipper'), 5002, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Plastic Zipper'), 7028, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Invisible Zipper'), 4619, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Screen Printing Ink'), 3300, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Transfer Paper'), 9429, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Thread'), 2412, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Press Machine'), 2327, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Transfer Vinyl'), 5600, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Thermal Tape'), 1775, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Transfer Film'), 5598, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Hoops'), 9377, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Needles'), 9323, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Floss'), 1303, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Scissors'), 1223, CURRENT_TIMESTAMP, NULL),

--     BRAND MATERIAL WITH EMAIL tammtse161087@fpt.edu.vn
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Cotton Fabric'), 3991, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Silk Fabric'), 5093, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Linen Fabric'), 4679, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Polyester Thread'), 1292, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Nylon Thread'), 8019, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Cotton Thread'), 1519, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Organic Cotton'), 3796, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Recycled Cotton'), 4207, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Combed Cotton'), 3295, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Textile Ink'), 1174, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Silk Screen Ink'), 1677, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Dye Sublimation Ink'), 3519, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Masking Tape'), 6992, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Double-Sided Tape'), 9002, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Packing Tape'), 1680, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Woven Label'), 1807, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Printed Label'), 6613, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Transfer Label'), 5802, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Plastic Button'), 8522, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Metal Button'), 1324, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Wooden Button'), 2772, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Eco-Friendly Bag'), 7506, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Reusable Shopping Bag'), 1589, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Canvas Bag'), 4715, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Sewing Accessory Kit'), 2753, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Zipper Pulls'), 1142, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Sewing Needles'), 1496, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Paper Hang Tag'), 9523, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Plastic Hang Tag'), 1076, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Metal Hang Tag'), 4010, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Metal Zipper'), 4996, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Plastic Zipper'), 6972, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Invisible Zipper'), 4629, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Screen Printing Ink'), 3301, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Transfer Paper'), 9428, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Thread'), 2381, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Press Machine'), 2315, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Transfer Vinyl'), 5628, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Thermal Tape'), 1830, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Transfer Film'), 5615, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Hoops'), 9397, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Needles'), 9284, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Floss'), 1301, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Scissors'), 1220, CURRENT_TIMESTAMP, NULL),

--     BRAND MATERIAL WITH EMAIL truongnhlse160191@fpt.edu.vn
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Cotton Fabric'), 3986, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Silk Fabric'), 5079, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Linen Fabric'), 4729, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Polyester Thread'), 1301, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Nylon Thread'), 8021, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Cotton Thread'), 1507, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Organic Cotton'), 3819, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Recycled Cotton'), 4205, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Combed Cotton'), 3282, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Textile Ink'), 1213, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Silk Screen Ink'), 1724, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Dye Sublimation Ink'), 3499, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Masking Tape'), 6992, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Double-Sided Tape'), 8977, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Packing Tape'), 1704, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Woven Label'), 1822, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Printed Label'), 6615, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Transfer Label'), 5782, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Plastic Button'), 8524, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Metal Button'), 1270, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Wooden Button'), 2797, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Eco-Friendly Bag'), 7507, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Reusable Shopping Bag'), 1615, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Canvas Bag'), 4703, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Sewing Accessory Kit'), 2736, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Zipper Pulls'), 1131, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Sewing Needles'), 1523, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Paper Hang Tag'), 9488, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Plastic Hang Tag'), 1096, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Metal Hang Tag'), 3992, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Metal Zipper'), 4977, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Plastic Zipper'), 6974, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Invisible Zipper'), 4590, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Screen Printing Ink'), 3279, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Transfer Paper'), 9419, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Thread'), 2406, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Press Machine'), 2278, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Transfer Vinyl'), 5578, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Thermal Tape'), 1791, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Transfer Film'), 5598, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Hoops'), 9379, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Needles'), 9301, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Floss'), 1323, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Scissors'), 1190, CURRENT_TIMESTAMP, NULL);

-- INSERT INTO BRAND PROPERTY
INSERT INTO brand_properties (brand_property_id, brand_id, property_id, brand_property_value, brand_property_status,
                              create_date, last_modified_date)
VALUES
-- Insert for email tunmse161130@fpt.edu.vn
(UNHEX(REPLACE(UUID(), '-', '')), (SELECT b.brand_id
                                   FROM users u
                                            JOIN brand b ON u.user_id = b.brand_id
                                   WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT property_id FROM system_properties WHERE property_name = 'BRAND_PRODUCTIVITY'), 50, true, CURRENT_TIMESTAMP,
 NULL);

-- Insert for email tammtse161087@fpt.edu.vn
INSERT INTO brand_properties (brand_property_id, brand_id, property_id, brand_property_value, brand_property_status,
                              create_date, last_modified_date)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), (SELECT b.brand_id
                                          FROM users u
                                                   JOIN brand b ON u.user_id = b.brand_id
                                          WHERE u.email = 'tammtse161087@fpt.edu.vn'),
        (SELECT property_id FROM system_properties WHERE property_name = 'BRAND_PRODUCTIVITY'), 52, true,
        CURRENT_TIMESTAMP, NULL);

-- Insert for email truongnhlse160191@fpt.edu.vn
INSERT INTO brand_properties (brand_property_id, brand_id, property_id, brand_property_value, brand_property_status,
                              create_date, last_modified_date)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), (SELECT b.brand_id
                                          FROM users u
                                                   JOIN brand b ON u.user_id = b.brand_id
                                          WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
        (SELECT property_id FROM system_properties WHERE property_name = 'BRAND_PRODUCTIVITY'), 48, true,
        CURRENT_TIMESTAMP, NULL);

-- INSERT INTO Expert Tailoring Material
INSERT INTO expert_tailoring_material (expert_tailoring_id, material_id, status, create_date, last_modified_date)
VALUES
-- ExpertTailoring : shirtModel and Category : Fabric
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Cotton Fabric'), true, NOW(), NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Silk Fabric'), true, NOW(), NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Linen Fabric'), true, NOW(), NULL),

-- ExpertTailoring : shirtModel and Category : Thread
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Polyester Thread'), true, NOW(), NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Nylon Thread'), true, NOW(), NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Cotton Thread'), true, NOW(), NULL);
