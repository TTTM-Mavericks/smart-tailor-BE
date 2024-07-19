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

-- system_image
INSERT INTO system_image (image_id, image_name, image_url, image_status, image_type, is_premium, create_date, last_modified_date) VALUES

(UNHEX(REPLACE(UUID(), '-', '')), 'Happy Sad White', 'https://i.pinimg.com/564x/bb/93/7d/bb937d07baf2901499ba0c0816134756.jpg', TRUE, 'ICON', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Happy Sad Nigga', 'https://i.pinimg.com/564x/ce/58/5e/ce585e8cabfd4e42eb9746e41bcb6ee3.jpg', TRUE, 'ICON', FALSE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Music Chilling', 'https://i.pinimg.com/564x/71/0c/7a/710c7ad97d56308beec204996a585fb6.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Just Do It', 'https://i.pinimg.com/564x/41/de/ef/41deef3a2605542cee92d81426d9b457.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'No Pain No Gain', 'https://i.pinimg.com/564x/9a/3e/72/9a3e721f294acab96a4d595490b1976f.jpg', TRUE, 'IMAGE', FALSE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Placeit', 'https://i.pinimg.com/564x/90/e6/fb/90e6fbe65defaa0ff7dd256baae830f2.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Sales', 'https://i.pinimg.com/564x/c3/4a/78/c34a78eaad81ff9528c3ec8380b607f9.jpg', TRUE, 'ICON', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Shop Sustences', 'https://i.pinimg.com/564x/60/ec/be/60ecbed5dd2cc35cf54dcabdd1d12b1d.jpg', TRUE, 'ICON', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Fashion', 'https://i.pinimg.com/564x/f0/31/91/f03191848b26015c4e5ac55b13398ae5.jpg', TRUE, 'ICON', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'MR.P', 'https://i.pinimg.com/564x/e2/96/8d/e2968d48caca668cefb0a10005d4b2af.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'KarmaShek', 'https://i.pinimg.com/564x/eb/17/79/eb17794a5e71699517e531c606a81a1d.jpg', TRUE, 'IMAGE', FALSE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Dog Cute', 'https://i.pinimg.com/564x/20/de/67/20de67934a421bbb3ca5a6a052524b51.jpg', TRUE, 'ICON', FALSE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Orange', 'https://i.pinimg.com/564x/14/88/7e/14887e4c8a0753b8b7c8f1ea4065b56f.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Happy', 'https://i.pinimg.com/564x/80/f5/79/80f579089709999aa870eadc2f4197e8.jpg', TRUE, 'IMAGE', FALSE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Life', 'https://i.pinimg.com/564x/23/36/2e/23362e8fe82745c93bc0c38f563c707e.jpg', TRUE, 'IMAGE', FALSE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'SuiSailet', 'https://i.pinimg.com/564x/4c/6a/2d/4c6a2d3c1103306123e24c4f3674f82f.jpg', TRUE, 'ICON', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Sautter', 'https://i.pinimg.com/564x/c0/38/c8/c038c8d17f4d93cc4a3975a56e9e6a89.jpg', TRUE, 'ICON', FALSE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Bus', 'https://i.pinimg.com/564x/39/60/1e/39601e12a52c8ecdb49a47113d4a3f46.jpg', TRUE, 'ICON', FALSE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Box', 'https://i.pinimg.com/236x/53/b1/fd/53b1fd253e54a9fed689935f8982359b.jpg', TRUE, 'ICON', FALSE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Shopping', 'https://i.pinimg.com/564x/b6/b3/c2/b6b3c266de4272978816746d5e6a982b.jpg', TRUE, 'ICON', FALSE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Shop', 'https://i.pinimg.com/564x/01/40/78/014078e022373b6eb0f5600fff66cbbb.jpg', TRUE, 'ICON', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Melody', 'https://i.pinimg.com/564x/3c/31/f2/3c31f2936b29c622bf39d69d47bb77c4.jpg', TRUE, 'ICON', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Candy', 'https://i.pinimg.com/564x/1e/98/ff/1e98fff5c3c7781ca0f9b883a98f3b0e.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Comments', 'https://i.pinimg.com/564x/0e/a1/d8/0ea1d81f6ee0df19d36e3d319f9031e7.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Loive', 'https://i.pinimg.com/236x/0c/fc/e5/0cfce5e80520edead31058a6a78c282f.jpg', TRUE, 'IMAGE', FALSE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Sunday Morning', 'https://i.pinimg.com/564x/84/de/25/84de25c3653f3813a05c7df509006757.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'London', 'https://i.pinimg.com/236x/74/15/6e/74156e2afcb9c557a9c9141f5781b48e.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Quote', 'https://i.pinimg.com/236x/ea/e1/8d/eae18dc4db431d7892cd7cb82a6726db.jpg', TRUE, 'IMAGE', FALSE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'lLive', 'https://i.pinimg.com/236x/08/7a/b5/087ab5cf1865a962a942ec9336296729.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Cherry', 'https://i.pinimg.com/564x/b1/ab/10/b1ab10f770bbd6da1caa7a6cc46b14ed.jpg', TRUE, 'ICON', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Bubble', 'https://i.pinimg.com/564x/a4/dd/02/a4dd02c4b6d16981d2aee6eb9f448c3e.jpg', TRUE, 'ICON', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Duck', 'https://i.pinimg.com/564x/ac/c3/2c/acc32c5e9614d1e68f796833188d3c49.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Ducky', 'https://i.pinimg.com/564x/f8/91/b5/f891b53d10e93bc6edb25bc73d59dc05.jpg', TRUE, 'IMAGE', FALSE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), '8 Balls', 'https://i.pinimg.com/564x/fa/89/57/fa89579d742e87c08921bbfec1c1f6c1.jpg', TRUE, 'ICON', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Chilly', 'https://i.pinimg.com/564x/e8/8d/19/e88d19684158e22460292e77c6ceedf5.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Coca', 'https://i.pinimg.com/564x/e2/cd/cc/e2cdcc9a04afafca80637bc699afd66e.jpg', TRUE, 'ICON', FALSE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Sweet', 'https://i.pinimg.com/564x/72/f3/5f/72f35fe198f34cac282fb3868fbfcf61.jpg', TRUE, 'ICON', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Smile', 'https://i.pinimg.com/564x/72/f3/5f/72f35fe198f34cac282fb3868fbfcf61.jpg', TRUE, 'IMAGE', FALSE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Thunder', 'https://i.pinimg.com/564x/f1/05/d6/f105d671c6ce8940ea34caee0fea5bd2.jpg', TRUE, 'ICON', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'T1', 'https://i.pinimg.com/564x/b2/0a/65/b20a653428d9f1a035e7d0bf69c1fcc4.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'World Cup', 'https://i.pinimg.com/736x/8c/47/30/8c4730ceef14c8925f19ae7b199d5633.jpg', TRUE, 'IMAGE', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Animal', 'https://i.pinimg.com/564x/a4/c4/0f/a4c40fb085c3b43964bb0941c4de6af4.jpg', TRUE, 'IMAGE', FALSE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Ảnh con rồng con', 'https://th.bing.com/th/id/OIP.28898F789NfJAXbpwl1pwwHaEK?w=318&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7', TRUE, 'STAMP', FALSE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Ảnh rồng mẹ', 'https://khoinguonsangtao.vn/wp-content/uploads/2022/08/avatar-nu-de-thuong.jpg', TRUE, 'STAMP', TRUE, NOW(6), NULL),
(UNHEX(REPLACE(UUID(), '-', '')), 'Ảnh rồng ba', 'https://animeanime.global/wp-content/uploads/2020/04/329423.png', TRUE, 'STAMP', TRUE, NOW(6), NULL);

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


INSERT INTO material (material_id, create_date, last_modified_date, base_price, hs_code, material_name, status, unit,
                      category_id)
VALUES
-- Fabric Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 15000, 5407101000, 'Cotton Fabric', 1, 'meter',
 (SELECT category_id FROM category WHERE category_name = 'Fabric')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 20000, 5407101001, 'Silk Fabric', 1, 'meter',
 (SELECT category_id FROM category WHERE category_name = 'Fabric')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 10000, 5407101002, 'Linen Fabric', 1, 'meter',
 (SELECT category_id FROM category WHERE category_name = 'Fabric')),

-- Thread Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 35000, 5508100000, 'Polyester Thread', 1, 'spool',
 (SELECT category_id FROM category WHERE category_name = 'Thread')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 40000, 5508100001, 'Nylon Thread', 1, 'spool',
 (SELECT category_id FROM category WHERE category_name = 'Thread')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 50000, 5508100002, 'Cotton Thread', 1, 'spool',
 (SELECT category_id FROM category WHERE category_name = 'Thread')),

-- Cotton Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 12000, 5201000000, 'Organic Cotton', 1, 'kilogram',
 (SELECT category_id FROM category WHERE category_name = 'Cotton')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 10000, 5201000001, 'Recycled Cotton', 1, 'kilogram',
 (SELECT category_id FROM category WHERE category_name = 'Cotton')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 80000, 5201000002, 'Combed Cotton', 1, 'kilogram',
 (SELECT category_id FROM category WHERE category_name = 'Cotton')),

-- Ink Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 25000, 3215190000, 'Textile Ink', 1, 'liter',
 (SELECT category_id FROM category WHERE category_name = 'Ink')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 20000, 3215190001, 'Silk Screen Ink', 1, 'liter',
 (SELECT category_id FROM category WHERE category_name = 'Ink')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 30000, 3215190002, 'Dye Sublimation Ink', 1, 'liter',
 (SELECT category_id FROM category WHERE category_name = 'Ink')),

-- Tape Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 12000, 4823908500, 'Masking Tape', 1, 'roll',
 (SELECT category_id FROM category WHERE category_name = 'Tape')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 15000, 4823908501, 'Double-Sided Tape', 1, 'roll',
 (SELECT category_id FROM category WHERE category_name = 'Tape')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 10000, 4823908502, 'Packing Tape', 1, 'roll',
 (SELECT category_id FROM category WHERE category_name = 'Tape')),

-- Label Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 50000, 4821102000, 'Woven Label', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Label')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 70000, 4821102001, 'Printed Label', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Label')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 90000, 4821102002, 'Heat Transfer Label', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Label')),

-- Button Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 20000, 9606210000, 'Plastic Button', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Button')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 20000, 9606210001, 'Metal Button', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Button')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 30000, 9606210002, 'Wooden Button', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Button')),

-- Bag Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 25000, 4202929100, 'Eco-Friendly Bag', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Bag')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 30000, 4202929101, 'Reusable Shopping Bag', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Bag')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 40000, 4202929102, 'Canvas Bag', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Bag')),

-- Accessory Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 50000, 3926909700, 'Sewing Accessory Kit', 1, 'set',
 (SELECT category_id FROM category WHERE category_name = 'Accessory')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 60000, 3926909701, 'Zipper Pulls', 1, 'set',
 (SELECT category_id FROM category WHERE category_name = 'Accessory')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 70000, 3926909702, 'Sewing Needles', 1, 'set',
 (SELECT category_id FROM category WHERE category_name = 'Accessory')),

-- Hang Tag Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 80000, 4911991000, 'Paper Hang Tag', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Hang Tag')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 90000, 4911991001, 'Plastic Hang Tag', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Hang Tag')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 10000, 4911991002, 'Metal Hang Tag', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Hang Tag')),

-- Zipper Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 15000, 9607110000, 'Metal Zipper', 1, 'meter',
 (SELECT category_id FROM category WHERE category_name = 'Zipper')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 12000, 9607110001, 'Plastic Zipper', 1, 'meter',
 (SELECT category_id FROM category WHERE category_name = 'Zipper')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 20000, 9607110002, 'Invisible Zipper', 1, 'meter',
 (SELECT category_id FROM category WHERE category_name = 'Zipper')),

-- Manual Printing Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 50000, 3215, 'Screen Printing Ink', 1, 'liter',
 (SELECT category_id FROM category WHERE category_name = 'Manual Printing')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 30000, 48169040, 'Heat Transfer Paper', 1, 'sheet',
 (SELECT category_id FROM category WHERE category_name = 'Manual Printing')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 45000, 55111010, 'Embroidery Thread', 1, 'spool',
 (SELECT category_id FROM category WHERE category_name = 'Manual Printing')),

-- Heat Printing Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 60000, 84629950, 'Heat Press Machine', 1, 'unit',
 (SELECT category_id FROM category WHERE category_name = 'Heat Printing')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 25000, 48169040, 'Transfer Vinyl', 1, 'roll',
 (SELECT category_id FROM category WHERE category_name = 'Heat Printing')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 40000, 39199010, 'Thermal Tape', 1, 'roll',
 (SELECT category_id FROM category WHERE category_name = 'Heat Printing')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 55000, 39199020, 'Heat Transfer Film', 1, 'roll',
 (SELECT category_id FROM category WHERE category_name = 'Heat Printing')),

-- Embroider Category
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 70000, 44140000, 'Embroidery Hoops', 1, 'piece',
 (SELECT category_id FROM category WHERE category_name = 'Embroider')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 50000, 73199010, 'Embroidery Needles', 1, 'pack',
 (SELECT category_id FROM category WHERE category_name = 'Embroider')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 60000, 62043300, 'Embroidery Floss', 1, 'skein',
 (SELECT category_id FROM category WHERE category_name = 'Embroider')),
(UNHEX(REPLACE(UUID(), '-', '')), NOW(6), NULL, 65000, 73199020, 'Embroidery Scissors', 1, 'pair',
 (SELECT category_id FROM category WHERE category_name = 'Embroider'));


-- -- Sample Data Expert Tailoring Material
-- INSERT INTO expert_tailoring_material (expert_tailoring_id, material_id, status, create_date, last_modified_date) VALUES
-- -- ExpertTailoring : shirtModel and Category : Fabric
-- ((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),(SELECT material_id FROM material WHERE material_name = 'Cotton Fabric'), true, NOW(), NULL),
-- ((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),(SELECT material_id FROM material WHERE material_name = 'Silk Fabric'),true,NOW(), NULL),
-- ((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),(SELECT material_id FROM material WHERE material_name = 'Linen Fabric'),true,NOW(),NULL),
--
-- -- ExpertTailoring : shirtModel and Category : Thread
-- ((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'), (SELECT material_id FROM material WHERE material_name = 'Polyester Thread'),true,NOW(),NULL),
-- ((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),(SELECT material_id FROM material WHERE material_name = 'Nylon Thread'),true,NOW(), NULL),
-- ((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),(SELECT material_id FROM material WHERE material_name = 'Cotton Thread'),true,NOW(),NULL);

-- size
INSERT INTO size (size_id, size_name, status, create_date, last_modified_date)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), 'S', true, current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 'L', true, current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 'M', true, current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 'XL', true, current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 'XXL', true, current_timestamp, null),
       (UNHEX(REPLACE(UUID(), '-', '')), 'XXXL', true, current_timestamp, null);

-- labor_quantity
INSERT INTO labor_quantity (labor_quantity_id, labor_quantity_min_quantity, labor_quantity_max_quantity, labor_quantity_min_price, labor_quantity_max_price, status, create_date, last_modified_date)
VALUES
    (UNHEX(REPLACE(UUID(), '-', '')), 1, 10, 150000, 170000, true, NOW(), NULL),
    (UNHEX(REPLACE(UUID(), '-', '')), 11, 20, 140000, 160000, true, NOW(), NULL),
    (UNHEX(REPLACE(UUID(), '-', '')), 21, 50, 130000, 150000, true, NOW(), NULL),
    (UNHEX(REPLACE(UUID(), '-', '')), 51, 100, 120000, 140000, true, NOW(), NULL),
    (UNHEX(REPLACE(UUID(), '-', '')), 101, 200, 110000, 130000, true, NOW(), NULL),
    (UNHEX(REPLACE(UUID(), '-', '')), 201, 100000, 100000, 120000, true, NOW(), NULL);


-- user with password Aa@123456
INSERT INTO users (user_id, email, password, full_name, language, phone_number, role_id, provider, user_status, image_url, create_date, last_modified_date)
VALUES
     (UNHEX(REPLACE(UUID(), '-', '')), 'nguyenvanquan@gmail.com', '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Nguyen Van Quan', 'vietnam', '0902818618',
     (SELECT role_id FROM roles WHERE role_name = 'CUSTOMER'), 'LOCAL', 'ACTIVE', 'http://example.com/image1.jpg', current_timestamp, null),

     (UNHEX(REPLACE(UUID(), '-', '')), 'hoanganhduy1122@gmail.com', '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Ha Anh Duy', 'vietnam', '0961782569',
      (SELECT role_id FROM roles WHERE role_name = 'CUSTOMER'), 'LOCAL', 'ACTIVE', 'http://example.com/image2.jpg', current_timestamp, null),

     (UNHEX(REPLACE(UUID(), '-', '')), 'chuongquocviet123@gmail.com', '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Chuong Quoc Viet', 'vietnam', '0904357264',
      (SELECT role_id FROM roles WHERE role_name = 'CUSTOMER'), 'LOCAL', 'ACTIVE', 'http://example.com/image1.jpg', current_timestamp, null),

     (UNHEX(REPLACE(UUID(), '-', '')), 'doanthuan97@gmail.com', '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Doan Thu An', 'vietnam', '0903826475',
      (SELECT role_id FROM roles WHERE role_name = 'BRAND'), 'LOCAL', 'ACTIVE', 'http://example.com/image2.jpg', current_timestamp, null),

     (UNHEX(REPLACE(UUID(), '-', '')), 'ngohongquang999@gmail.com', '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Ngo Hong Quang', 'vietnam', '0904659543',
      (SELECT role_id FROM roles WHERE role_name = 'BRAND'), 'LOCAL', 'ACTIVE', 'http://example.com/image1.jpg', current_timestamp, null),

     (UNHEX(REPLACE(UUID(), '-', '')), 'phamthanhgiang458@gmail.com', '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Pham Thanh Giang', 'vietnam', '0905638465',
      (SELECT role_id FROM roles WHERE role_name = 'BRAND'), 'LOCAL', 'ACTIVE', 'http://example.com/image2.jpg', current_timestamp, null);


-- Insert into customer table
INSERT INTO customer (customer_id, gender, date_of_birth, address, ward, district, province, number_of_violations, create_date, last_modified_date)
VALUES
    ((SELECT user_id FROM users WHERE email = 'nguyenvanquan@gmail.com'), true, '1990-01-01', '240 Phạm Văn Đồng', 'Hiệp Bình Chánh', 'Thủ Đức',  'Thành phố Hồ Chí Minh' ,0, current_timestamp, null),

    ((SELECT user_id FROM users WHERE email = 'hoanganhduy1122@gmail.com'), true, '1990-01-02', '50 Lê Văn Việt', 'Hiệp Phú', 'Quận 9',  'Thành phố Hồ Chí Minh' ,0, current_timestamp, null),

    ((SELECT user_id FROM users WHERE email = 'chuongquocviet123@gmail.com'), true, '1990-01-03', '81 Nguyễn Xiển', 'Long Thạnh Mỹ', 'Quận 9',  'Thành phố Hồ Chí Minh' ,0, current_timestamp, null);


-- Insert into Brand table
INSERT INTO brand (brand_id, brand_name, rating, bank_name, account_number, account_name, qr_payment, address, ward,district, province, brand_status, number_of_violations, create_date, last_modified_date)
VALUES
    ((SELECT user_id FROM users WHERE email = 'doanthuan97@gmail.com'), 'Nike Brand', 4.5, 'Brand Bank', '1234567890', 'Nike', 'http://example.com/qr_nike.jpg', '740 Nguyễn Xiển', ' Long Thạnh Mỹ', 'Quận 9', 'Thành phố Hồ Chí Minh', 'ACCEPT', 0, current_timestamp, null),

    ((SELECT user_id FROM users WHERE email = 'ngohongquang999@gmail.com'), 'Adidas Brand', 4.2, 'Brand Bank', '0987654321', 'Adidas', 'http://example.com/qr_adidas.jpg', '269 Đ. Liên Phường', 'Phước Long B', 'Quận 9', 'Thành phố Hồ Chí Minh', 'ACCEPT', 0, current_timestamp, null),

    ((SELECT user_id FROM users WHERE email = 'phamthanhgiang458@gmail.com'), 'Puma Brand', 4.0, 'Brand Bank', '1357924680', 'Puma', 'http://example.com/qr_puma.jpg', '441 Lê Văn Việt', 'Tăng Nhơn Phú A', ' Quận 9', 'Thành phố Hồ Chí Minh', 'ACCEPT', 0, current_timestamp, null);
