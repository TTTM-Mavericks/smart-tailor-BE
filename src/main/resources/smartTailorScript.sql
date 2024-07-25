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
        ''),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null,
        'The percent for deposit.', 'DEPOSIT_PERCENT', 'PERCENT', true, '%',
        '50'),
       (UNHEX(REPLACE(UUID(), '-', '')), current_timestamp, null,
        'The number for divide.', 'DIVIDE_NUMBER', 'NUMBER', true, 'INT',
        '100');

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
VALUES (UNHEX(REPLACE(UUID(), '-', '')), 1, 10, 150000, 170000, true, NOW(), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 11, 20, 140000, 160000, true, NOW(), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 21, 50, 130000, 150000, true, NOW(), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 51, 100, 120000, 140000, true, NOW(), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 101, 200, 110000, 130000, true, NOW(), NULL),
       (UNHEX(REPLACE(UUID(), '-', '')), 201, 100000, 100000, 120000, true, NOW(), NULL);


-- user with password Aa@123456
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
        current_timestamp, null);


-- Insert into customer table
INSERT INTO customer (customer_id, gender, date_of_birth, address, ward, district, province, number_of_violations,
                      create_date, last_modified_date)
VALUES ((SELECT user_id FROM users WHERE email = 'nguyenvanquan@gmail.com'), true, '1990-01-01', '240 Phạm Văn Đồng',
        'Hiệp Bình Chánh', 'Thủ Đức', 'Thành phố Hồ Chí Minh', 0, current_timestamp, null),

       ((SELECT user_id FROM users WHERE email = 'hoanganhduy1122@gmail.com'), true, '1990-01-02', '50 Lê Văn Việt',
        'Hiệp Phú', 'Quận 9', 'Thành phố Hồ Chí Minh', 0, current_timestamp, null),

       ((SELECT user_id FROM users WHERE email = 'chuongquocviet123@gmail.com'), true, '1990-01-03', '81 Nguyễn Xiển',
        'Long Thạnh Mỹ', 'Quận 9', 'Thành phố Hồ Chí Minh', 0, current_timestamp, null);


-- Insert into Brand table
INSERT INTO brand (brand_id, brand_name, rating, bank_name, account_number, account_name, qr_payment, address, ward,
                   district, province, brand_status, number_of_violations, create_date, last_modified_date)
VALUES ((SELECT user_id FROM users WHERE email = 'tammtse161087@fpt.edu.vn'), 'Nike Brand', 4.5, 'Brand Bank',
        '1234567890', 'Nike', 'http://example.com/qr_nike.jpg', '740 Nguyễn Xiển', ' Long Thạnh Mỹ', 'Quận 9',
        'Thành phố Hồ Chí Minh', 'ACCEPT', 0, current_timestamp, null),

       ((SELECT user_id FROM users WHERE email = 'tunmse161130@fpt.edu.vn'), 'Adidas Brand', 4.2, 'Brand Bank',
        '0987654321', 'Adidas', 'http://example.com/qr_adidas.jpg', '269 Đ. Liên Phường', 'Phước Long B', 'Quận 9',
        'Thành phố Hồ Chí Minh', 'ACCEPT', 0, current_timestamp, null),

       ((SELECT user_id FROM users WHERE email = 'truongnhlse160191@fpt.edu.vn'), 'Puma Brand', 4.0, 'Brand Bank',
        '1357924680', 'Puma', 'http://example.com/qr_puma.jpg', '441 Lê Văn Việt', 'Tăng Nhơn Phú A', 'Quận 9',
        'Thành phố Hồ Chí Minh', 'ACCEPT', 0, current_timestamp, null),

       ((SELECT user_id FROM users WHERE email = 'ngohongquang999@gmail.com'), 'Hong Quang Brand', 4.0, 'Brand Bank',
        '1357924680', 'Puma', 'http://example.com/qr_puma.jpg', '857 Phạm Văn Đồng', 'Linh Tây', 'Thủ Đức',
        'Thành phố Hồ Chí Minh', 'ACCEPT', 0, current_timestamp, null),

       ((SELECT user_id FROM users WHERE email = 'doanthuan97@gmail.com'), 'Thuan An Brand', 4.0, 'Brand Bank',
        '1357924680', 'Puma', 'http://example.com/qr_puma.jpg', '432 Đ. Liên Phường', 'Phước Long B', 'Quận 9',
        'Thành phố Hồ Chí Minh', 'ACCEPT', 0, current_timestamp, null),

       ((SELECT user_id FROM users WHERE email = 'phamthanhgiang458@gmail.com'), 'Thanh Giang Brand', 4.0, 'Brand Bank',
        '1357924680', 'Puma', 'http://example.com/qr_puma.jpg', '77C Trần Ngọc Diện', 'Thảo Điền', 'Thủ Đức',
        'Thành phố Hồ Chí Minh', 'ACCEPT', 0, current_timestamp, null);


-- INSERT BRAND EXPERT TAILORING
INSERT INTO brand_expert_tailoring(brand_id, expert_tailoring_id, create_date, last_modified_date)
VALUES

-- Brand with email tunmse161130@fpt.edu.vn have ExpertTailoringName: shirtModel,
((SELECT b.brand_id FROM users u JOIN brand b on u.user_id = b.brand_id WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'shirtModel'), current_timestamp, null),

((SELECT b.brand_id FROM users u JOIN brand b on u.user_id = b.brand_id WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'hoodieModel'), current_timestamp, null),

((SELECT b.brand_id FROM users u JOIN brand b on u.user_id = b.brand_id WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'longSkirtModel'), current_timestamp, null),

-- Brand with email tammtse161087@fpt.edu.vn have ExpertTailoringName: shirtModel,
((SELECT b.brand_id FROM users u JOIN brand b on u.user_id = b.brand_id WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'shirtModel'), current_timestamp, null),

((SELECT b.brand_id FROM users u JOIN brand b on u.user_id = b.brand_id WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'), current_timestamp, null),

((SELECT b.brand_id FROM users u JOIN brand b on u.user_id = b.brand_id WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'), current_timestamp, null),

-- Brand with email tammtse161087@fpt.edu.vn have ExpertTailoringName: shirtModel,
((SELECT b.brand_id FROM users u JOIN brand b on u.user_id = b.brand_id WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'shirtModel'), current_timestamp, null),

((SELECT b.brand_id FROM users u JOIN brand b on u.user_id = b.brand_id WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'), current_timestamp, null),

((SELECT b.brand_id FROM users u JOIN brand b on u.user_id = b.brand_id WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'), current_timestamp, null),

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