use fisport;
-- ========================
-- 1. User & Role
-- ========================

INSERT INTO role (name) VALUES
                            ('ADMIN'),
                            ('USER'),
                            ('OWNER');


INSERT INTO user (username, email, password, phone, birth_day, status, gender, role_id)
VALUES
    ('dattran0901', 'tranquocdat@gmail.com', '$2a$10$s3go5e.GYivSMmrJXG6jceddjfSAbg6O832Sip8XIVNRRLIjXNP6G', '0785819692', '2004-01-09', 'ACTIVE', 'MALE', 1),
    ('johndoe', 'dattranquoc@gmail.com', '$2a$10$2rQkUgA4sMS5z4QK8f1QXeF6p3iP63PHGQo2og4g0oY6Qp1QjOq9e', '0912345678', '1998-08-15', 'ACTIVE', 'FEMALE', 2),
    ('vitidsarn', 'dattran@gmail.com', '$10$rdj4PofmprN4QDf.uanJoOmcru3qmRU6zlGImoOyWhsrVIRIbdv6C', '0934567890', '1990-01-10', 'INACTIVE', 'MALE', 3);


-- ========================
-- 2. Location
-- ========================
INSERT INTO city (name, slug) VALUES
                                  ('Hà Nội', 'ha-noi'),
                                  ('TP Hồ Chí Minh', 'ho-chi-minh'),
                                  ('Đà Nẵng', 'da-nang'),
                                  ('Cần Thơ', 'can-tho'),
                                  ('An Giang', 'an-giang'),
                                  ('Bà Rịa - Vũng Tàu', 'ba-ria-vung-tau'),
                                  ('Bắc Giang', 'bac-giang'),
                                  ('Bắc Kạn', 'bac-kan'),
                                  ('Bạc Liêu', 'bac-lieu'),
                                  ('Bắc Ninh', 'bac-ninh'),
                                  ('Bến Tre', 'ben-tre'),
                                  ('Bình Dương', 'binh-duong'),
                                  ('Bình Định', 'binh-dinh'),
                                  ('Bình Phước', 'binh-phuoc'),
                                  ('Bình Thuận', 'binh-thuan'),
                                  ('Cà Mau', 'ca-mau'),
                                  ('Cao Bằng', 'cao-bang'),
                                  ('Đắk Lắk', 'dak-lak'),
                                  ('Đắk Nông', 'dak-nong'),
                                  ('Điện Biên', 'dien-bien'),
                                  ('Đồng Nai', 'dong-nai'),
                                  ('Đồng Tháp', 'dong-thap'),
                                  ('Gia Lai', 'gia-lai'),
                                  ('Hà Giang', 'ha-giang'),
                                  ('Hà Nam', 'ha-nam'),
                                  ('Hà Tĩnh', 'ha-tinh'),
                                  ('Hải Dương', 'hai-duong'),
                                  ('Hậu Giang', 'hau-giang'),
                                  ('Hòa Bình', 'hoa-binh'),
                                  ('Hưng Yên', 'hung-yen'),
                                  ('Khánh Hòa', 'khanh-hoa'),
                                  ('Kiên Giang', 'kien-giang'),
                                  ('Kon Tum', 'kon-tum'),
                                  ('Lai Châu', 'lai-chau'),
                                  ('Lâm Đồng', 'lam-dong'),
                                  ('Lạng Sơn', 'lang-son'),
                                  ('Lào Cai', 'lao-cai'),
                                  ('Long An', 'long-an'),
                                  ('Nam Định', 'nam-dinh'),
                                  ('Nghệ An', 'nghe-an'),
                                  ('Ninh Bình', 'ninh-binh'),
                                  ('Ninh Thuận', 'ninh-thuan'),
                                  ('Phú Thọ', 'phu-tho'),
                                  ('Phú Yên', 'phu-yen'),
                                  ('Quảng Bình', 'quang-binh'),
                                  ('Quảng Nam', 'quang-nam'),
                                  ('Quảng Ngãi', 'quang-ngai'),
                                  ('Quảng Ninh', 'quang-ninh'),
                                  ('Quảng Trị', 'quang-tri'),
                                  ('Sóc Trăng', 'soc-trang'),
                                  ('Sơn La', 'son-la'),
                                  ('Tây Ninh', 'tay-ninh'),
                                  ('Thái Bình', 'thai-binh'),
                                  ('Thái Nguyên', 'thai-nguyen'),
                                  ('Thanh Hóa', 'thanh-hoa'),
                                  ('Thừa Thiên Huế', 'thua-thien-hue'),
                                  ('Tiền Giang', 'tien-giang'),
                                  ('Trà Vinh', 'tra-vinh'),
                                  ('Tuyên Quang', 'tuyen-quang'),
                                  ('Vĩnh Long', 'vinh-long'),
                                  ('Vĩnh Phúc', 'vinh-phuc');






INSERT INTO ward (name, city_id, slug) VALUES
                                           ('Hải Châu', 3, 'hai-chau'),
                                           ('Hòa Cường', 3, 'hoa-cuong'),
                                           ('Thanh Khê', 3, 'thanh-khe'),
                                           ('An Khê', 3, 'an-khe'),
                                           ('An Hải', 3, 'an-hai'),
                                           ('Sơn Trà', 3, 'son-tra'),
                                           ('Ngũ Hành Sơn', 3, 'ngu-hanh-son'),
                                           ('Hòa Khánh', 3, 'hoa-khanh'),
                                           ('Liên Chiểu', 3, 'lien-chieu'),
                                           ('Cẩm Lệ', 3, 'cam-le'),
                                           ('Hòa Xuân', 3, 'hoa-xuan');

-- ========================
-- 3. Brand
-- ========================
INSERT INTO brand (name, code) VALUES
-- 1: Football
('Nike', 'NIKE'),
('Adidas', 'ADIDAS'),

-- 2: Badminton
('Yonex', 'YONEX'),
('Li-Ning', 'LINING'),

-- 3: Tennis
('Wilson', 'WILSON'),
('Babolat', 'BABOLAT'),

-- 4: Basketball
('Spalding', 'SPALDING'),
('Jordan', 'JORDAN'),

-- 5: Swimming
('Speedo', 'SPEEDO'),
('Arena', 'ARENA'),

-- 6: Pickleball
('Selkirk', 'SELKIRK'),
('Onix', 'ONIX');

-- ========================
-- 4. Field & Feature
-- ========================

INSERT INTO field_type (name, slug) VALUES
                                        ('Bóng đá', 'bong-da'),
                                        ('Cầu lông', 'cau-long'),
                                        ('Tennis', 'tennis'),
                                        ('Bóng rổ', 'bong-ro'),
                                        ('Bơi lội', 'boi-loi'),
                                        ('Pickleball', 'pickleball');

INSERT INTO field (name, slug, address, ward_id, user_id, field_type_id, description, banner, status, created_at, updated_at)
VALUES
-- Bóng đá (field_type_id = 1)
('Sân Bóng đá 1', 'san-bong-da-1', '123 Nguyễn Văn Linh', 1, 1, 1, 'Sân bóng đá rộng, cỏ nhân tạo chất lượng cao', '/images/fields/san-bong-da-1.jpg', 'ACTIVE', NOW(), NOW()),
('Sân Bóng đá 2', 'san-bong-da-2', '456 Lê Duẩn', 1, 1, 1, 'Sân bóng đá tiêu chuẩn 7 người, có đèn chiếu sáng', '/images/fields/san-bong-da-2.jpg', 'ACTIVE', NOW(), NOW()),

-- Cầu lông (field_type_id = 2)
('Sân Cầu lông 1', 'san-cau-long-1', '12 Trần Phú', 1, 1, 2, 'Sân cầu lông tiêu chuẩn, nền gỗ chống trơn trượt', '/images/fields/san-cau-long-1.jpg', 'ACTIVE', NOW(), NOW()),
('Sân Cầu lông 2', 'san-cau-long-2', '34 Phan Chu Trinh', 1, 1, 2, 'Sân cầu lông trong nhà, có điều hòa', '/images/fields/san-cau-long-2.jpg', 'ACTIVE', NOW(), NOW()),

-- Tennis (field_type_id = 3)
('Sân Tennis 1', 'san-tennis-1', '56 Nguyễn Hoàng', 1, 1, 3, 'Sân tennis ngoài trời, mặt sân cứng', '/images/fields/san-tennis-1.jpg', 'ACTIVE', NOW(), NOW()),
('Sân Tennis 2', 'san-tennis-2', '78 Hoàng Diệu', 1, 1, 3, 'Sân tennis tiêu chuẩn quốc tế', '/images/fields/san-tennis-2.jpg', 'INACTIVE', NOW(), NOW()),

-- Bóng rổ (field_type_id = 4)
('Sân Bóng rổ 1', 'san-bong-ro-1', '90 Lý Thái Tổ', 1, 1, 4, 'Sân bóng rổ ngoài trời, phù hợp thi đấu 5x5', '/images/fields/san-bong-ro-1.jpg', 'ACTIVE', NOW(), NOW()),
('Sân Bóng rổ 2', 'san-bong-ro-2', '22 Nguyễn Văn Cừ', 1, 1, 4, 'Sân bóng rổ trong nhà, sàn gỗ', '/images/fields/san-bong-ro-2.jpg', 'ACTIVE', NOW(), NOW()),

-- Bơi lội (field_type_id = 5)
('Hồ Bơi 1', 'ho-boi-1', '11 Điện Biên Phủ', 1, 1, 5, 'Hồ bơi 25m, phù hợp luyện tập và thi đấu', '/images/fields/ho-boi-1.jpg', 'ACTIVE', NOW(), NOW()),
('Hồ Bơi 2', 'ho-boi-2', '33 Nguyễn Chí Thanh', 1, 1, 5, 'Hồ bơi ngoài trời, có khu vui chơi trẻ em', '/images/fields/ho-boi-2.jpg', 'INACTIVE', NOW(), NOW()),

-- Pickleball (field_type_id = 6)
('Sân Pickleball 1', 'san-pickleball-1', '44 Hùng Vương', 1, 1, 6, 'Sân pickleball tiêu chuẩn, phù hợp thi đấu đôi', '/images/fields/san-pickleball-1.jpg', 'ACTIVE', NOW(), NOW()),
('Sân Pickleball 2', 'san-pickleball-2', '55 Pasteur', 1, 1, 6, 'Sân pickleball trong nhà, có mái che', '/images/fields/san-pickleball-2.jpg', 'ACTIVE', NOW(), NOW());
-- ========================
-- 5. Time Slot & Field Slot
-- ========================


INSERT INTO time_slot (start_time)
VALUES
    ('07:00:00'),
    ('09:00:00'),
    ('11:00:00'),
    ('13:00:00'),
    ('15:00:00'),
    ('17:00:00'),
    ('19:00:00'),
    ('21:00:00');

-- Field 1 đến 12, mỗi field có 8 time_slot
INSERT INTO field_time_slot (field_id, time_slot_id, price)
VALUES
-- Field 1
(1, 1, 50000), (1, 2, 50000), (1, 3, 50000),
(1, 4, 70000), (1, 5, 70000), (1, 6, 70000),
(1, 7, 90000), (1, 8, 90000),

-- Field 2
(2, 1, 50000), (2, 2, 50000), (2, 3, 50000),
(2, 4, 70000), (2, 5, 70000), (2, 6, 70000),
(2, 7, 90000), (2, 8, 90000),

-- Field 3
(3, 1, 50000), (3, 2, 50000), (3, 3, 50000),
(3, 4, 70000), (3, 5, 70000), (3, 6, 70000),
(3, 7, 90000), (3, 8, 90000),

-- Field 4
(4, 1, 50000), (4, 2, 50000), (4, 3, 50000),
(4, 4, 70000), (4, 5, 70000), (4, 6, 70000),
(4, 7, 90000), (4, 8, 90000),

-- Field 5
(5, 1, 50000), (5, 2, 50000), (5, 3, 50000),
(5, 4, 70000), (5, 5, 70000), (5, 6, 70000),
(5, 7, 90000), (5, 8, 90000),

-- Field 6
(6, 1, 50000), (6, 2, 50000), (6, 3, 50000),
(6, 4, 70000), (6, 5, 70000), (6, 6, 70000),
(6, 7, 90000), (6, 8, 90000),

-- Field 7
(7, 1, 50000), (7, 2, 50000), (7, 3, 50000),
(7, 4, 70000), (7, 5, 70000), (7, 6, 70000),
(7, 7, 90000), (7, 8, 90000),

-- Field 8
(8, 1, 50000), (8, 2, 50000), (8, 3, 50000),
(8, 4, 70000), (8, 5, 70000), (8, 6, 70000),
(8, 7, 90000), (8, 8, 90000),

-- Field 9
(9, 1, 50000), (9, 2, 50000), (9, 3, 50000),
(9, 4, 70000), (9, 5, 70000), (9, 6, 70000),
(9, 7, 90000), (9, 8, 90000),

-- Field 10
(10, 1, 50000), (10, 2, 50000), (10, 3, 50000),
(10, 4, 70000), (10, 5, 70000), (10, 6, 70000),
(10, 7, 90000), (10, 8, 90000),

-- Field 11
(11, 1, 50000), (11, 2, 50000), (11, 3, 50000),
(11, 4, 70000), (11, 5, 70000), (11, 6, 70000),
(11, 7, 90000), (11, 8, 90000),

-- Field 12
(12, 1, 50000), (12, 2, 50000), (12, 3, 50000),
(12, 4, 70000), (12, 5, 70000), (12, 6, 70000),
(12, 7, 90000), (12, 8, 90000);

INSERT INTO duration (minutes) VALUES
                                   (60),   -- 1h
                                   (90),   -- 1h30
                                   (120),  -- 2h
                                   (150),  -- 2h30
                                   (180),  -- 3h
                                   (210),  -- 3h30
                                   (240),  -- 4h
                                   (300),  -- 5h
                                   (360),  -- 6h
                                   (420),  -- 7h
                                   (480),  -- 8h
                                   (600);  -- 10h

-- Football
INSERT INTO field_type_duration (field_type_id, duration_id) VALUES
                                                                 (1, 1),  -- 60
                                                                 (1, 2),  -- 90
                                                                 (1, 3),  -- 120
                                                                 (1, 4),  -- 150
                                                                 (1, 5);  -- 180

-- Badminton
INSERT INTO field_type_duration (field_type_id, duration_id) VALUES
                                                                 (2, 1),  -- 60
                                                                 (2, 2),  -- 90
                                                                 (2, 3);  -- 120

-- Tennis
INSERT INTO field_type_duration (field_type_id, duration_id) VALUES
                                                                 (3, 1),  -- 60
                                                                 (3, 2),  -- 90
                                                                 (3, 3),  -- 120
                                                                 (3, 4),  -- 150
                                                                 (3, 5);  -- 180

-- Basketball
INSERT INTO field_type_duration (field_type_id, duration_id) VALUES
                                                                 (4, 1),  -- 60
                                                                 (4, 2),  -- 90
                                                                 (4, 3);  -- 120

-- Swimming
INSERT INTO field_type_duration (field_type_id, duration_id) VALUES
                                                                 (5, 1),  -- 60
                                                                 (5, 3),  -- 120
                                                                 (5, 5),  -- 180
                                                                 (5, 7),  -- 240
                                                                 (5, 11), -- 480
                                                                 (5, 12); -- 600

-- Pickleball
INSERT INTO field_type_duration (field_type_id, duration_id) VALUES
                                                                 (6, 1),  -- 60
                                                                 (6, 2),  -- 90
                                                                 (6, 3);  -- 120

-- ========================
-- 6. Service & Service Item
-- ========================
INSERT INTO service (name) VALUES
-- Football (field_type_id = 1)
('Thuê giày đá bóng'),
('Thuê bóng đá'),

-- Badminton (field_type_id = 2)
('Thuê vợt cầu lông'),
('Thuê giày cầu lông'),

-- Tennis (field_type_id = 3)
('Thuê vợt tennis'),
('Thuê bóng tennis'),

-- Basketball (field_type_id = 4)
('Thuê bóng rổ'),
('Thuê áo bóng rổ'),

-- Swimming (field_type_id = 5)
('Thuê kính bơi'),
('Thuê phao bơi'),

-- Pickleball (field_type_id = 6)
('Thuê vợt pickleball'),
('Thuê bóng pickleball');


INSERT INTO service_item (service_id, name, brand_id) VALUES
-- Football
(1, 'Giày đá bóng Nike', 1),
(1, 'Giày đá bóng Adidas', 2),
(2, 'Bóng đá Nike', 1),
(2, 'Bóng đá Adidas', 2),

-- Badminton
(3, 'Vợt cầu lông Yonex', 3),
(3, 'Vợt cầu lông Li-Ning', 4),
(4, 'Giày cầu lông Yonex', 3),
(4, 'Giày cầu lông Li-Ning', 4),

-- Tennis
(5, 'Vợt tennis Wilson', 5),
(5, 'Vợt tennis Babolat', 6),
(6, 'Bóng tennis Wilson', 5),
(6, 'Bóng tennis Babolat', 6),

-- Basketball
(7, 'Bóng rổ Spalding', 7),
(7, 'Bóng rổ Jordan', 8),
(8, 'Áo bóng rổ Nike', 1),
(8, 'Áo bóng rổ Jordan', 8),

-- Swimming
(9, 'Kính bơi Speedo', 9),
(9, 'Kính bơi Arena', 10),
(10, 'Phao bơi Speedo', 9),
(10, 'Phao bơi Arena', 10),

-- Pickleball
(11, 'Vợt Pickleball Selkirk', 11),
(11, 'Vợt Pickleball Onix', 12),
(12, 'Bóng Pickleball Selkirk', 11),
(12, 'Bóng Pickleball Onix', 12);



INSERT INTO field_service_item (field_id, service_item_id, price) VALUES
-- Football: field_id = 1, 2; service_item_id = 1..4
(1, 1, 50000), (1, 2, 50000), (1, 3, 30000), (1, 4, 30000),
(2, 1, 50000), (2, 2, 50000), (2, 3, 30000), (2, 4, 30000),

-- Badminton: field_id = 3, 4; service_item_id = 5..8
(3, 5, 70000), (3, 6, 70000), (3, 7, 50000), (3, 8, 50000),
(4, 5, 70000), (4, 6, 70000), (4, 7, 50000), (4, 8, 50000),

-- Tennis: field_id = 5, 6; service_item_id = 9..12
(5, 9, 80000), (5, 10, 80000), (5, 11, 40000), (5, 12, 40000),
(6, 9, 80000), (6, 10, 80000), (6, 11, 40000), (6, 12, 40000),

-- Basketball: field_id = 7, 8; service_item_id = 13..16
(7, 13, 60000), (7, 14, 60000), (7, 15, 40000), (7, 16, 40000),
(8, 13, 60000), (8, 14, 60000), (8, 15, 40000), (8, 16, 40000),

-- Swimming: field_id = 9, 10; service_item_id = 17..20
(9, 17, 25000), (9, 18, 25000), (9, 19, 20000), (9, 20, 20000),
(10, 17, 25000), (10, 18, 25000), (10, 19, 20000), (10, 20, 20000),

-- Pickleball: field_id = 11, 12; service_item_id = 21..24
(11, 21, 70000), (11, 22, 70000), (11, 23, 30000), (11, 24, 30000),
(12, 21, 70000), (12, 22, 70000), (12, 23, 30000), (12, 24, 30000);

