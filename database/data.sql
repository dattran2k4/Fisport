use fisport;
-- ========================
-- 1. User & Role
-- ========================

INSERT INTO role (name) VALUES
                            ('ADMIN'),
                            ('USER'),
                            ('OWNER');


INSERT INTO user (username, email, password, phone, birth_day, status, gender, is_two_factor, two_fa_secret, role_id)
VALUES
    ('dattran0901', 'tranquocdat@gmail.com', '$2a$10$s3go5e.GYivSMmrJXG6jceddjfSAbg6O832Sip8XIVNRRLIjXNP6G', '0785819692', '2004-01-09', 'ACTIVE', 'MALE', 0, NULL, 1),
    ('johndoe', 'dattranquoc@gmail.com', '$2a$10$2rQkUgA4sMS5z4QK8f1QXeF6p3iP63PHGQo2og4g0oY6Qp1QjOq9e', '0912345678', '1998-08-15', 'ACTIVE', 'FEMALE', 0,  NULL, 2),
    ('vitidsarn', 'dattran@gmail.com', '$2a$10$llGgE5VlZzM0.pCzbOLGWev.cqdovrjSsq0lGM87wo0FVgATXsh12', '0934567890', '1990-01-10', 'INACTIVE', 'MALE', 0, NULL, 3),
    ('dt12', 'tranquocthanhdat@gmail.com', '$2a$10$L1RrZcmnBVdz5EzZ9LtP9eNcPV1CiYvKwhEK/Qs9ocuqHakirdqg6', '0785819680', '2004-09-20', 'ACTIVE', 'MALE', 1, '4EFZSYXKQ6BFDGMAXWQKSFSTZFUYYLQD', 2);


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


INSERT INTO ward (name, latitude, longitude, city_id, slug) VALUES
  ('Hải Châu', 16.06056, 108.19611, 3, 'hai-chau'),
  ('Hòa Cường', 16.04193, 108.21191, 3, 'hoa-cuong'),
  ('Thanh Khê', 16.05778, 108.18333, 3, 'thanh-khe'),
  ('An Khê', 16.05222, 108.18556, 3, 'an-khe'),
  ('An Hải', 16.07778, 108.23306, 3, 'an-hai'),
  ('Sơn Trà', 16.07778, 108.23306, 3, 'son-tra'),
  ('Ngũ Hành Sơn', 16.07778, 108.23306, 3, 'ngu-hanh-son'),
  ('Hòa Khánh', 16.12389, 108.11778, 3, 'hoa-khanh'),
  ('Liên Chiểu', 16.04710, 108.13100, 3, 'lien-chieu'),
  ('Cẩm Lệ', 16.04710, 108.13100, 3, 'cam-le'),
  ('Hòa Xuân', 16.04710, 108.13100, 3, 'hoa-xuan');


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

INSERT INTO feature (name, slug) VALUES
-- Các tiện ích chung
('Có mái che', 'co-mai-che'),
('Có đèn chiếu sáng ban đêm', 'co-den-chieu-sang-ban-dem'),
('Có chỗ để xe', 'co-cho-de-xe'),
('Có phòng thay đồ', 'co-phong-thay-do'),
('Có phòng tắm', 'co-phong-tam'),
('Có wifi miễn phí', 'co-wifi-mien-phi'),

-- Riêng cho bóng đá
('Sân cỏ nhân tạo chất lượng cao', 'san-co-nhan-tao'),
('Có khung thành tiêu chuẩn', 'khung-thanh-tieu-chuan'),

-- Cầu lông / Tennis / Pickleball
('Sàn gỗ chống trượt', 'san-go-chong-truot'),
('Có vạch kẻ chuẩn quốc tế', 'vach-ke-chuan'),

-- Bơi lội
('Hồ bơi nước nóng', 'ho-boi-nuoc-nong'),
('Có huấn luyện viên hỗ trợ', 'co-huan-luyen-vien'),

-- Bóng rổ
('Bảng rổ tiêu chuẩn NBA', 'bang-ro-nba'),
('Sàn cao su giảm chấn', 'san-cao-su-giam-chan');

INSERT INTO field_type (name, slug) VALUES
                                        ('Bóng đá', 'bong-da'),
                                        ('Cầu lông', 'cau-long'),
                                        ('Tennis', 'tennis'),
                                        ('Bóng rổ', 'bong-ro'),
                                        ('Bơi lội', 'boi-loi'),
                                        ('Pickleball', 'pickleball');

INSERT INTO field (name, slug, address, ward_id, user_id, field_type_id, description, banner, open_time, close_time, status, latitude, longitude, created_at, updated_at)
VALUES
-- Bóng đá (field_type_id = 1)
('Sân bóng đá 1', 'san-bong-da-1', '12 Trần Phú, Hải Châu', 1, 3, 1, 'Sân bóng 5 người, cỏ nhân tạo chất lượng cao', '/web/img/field/san-bong-da-1.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0589, 108.2140, NOW(), NOW()),
('Sân bóng đá 2', 'san-bong-da-2', '34 Phan Chu Trinh, Hải Châu', 1, 3, 1, 'Sân bóng 7 người, có đèn chiếu sáng', '/web/img/field/san-bong-da-2.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0591, 108.2143, NOW(), NOW()),
('Sân bóng đá 3', 'san-bong-da-3', '789 Trần Phú, Hải Châu', 1, 3, 1, 'Sân bóng 5 người, cỏ nhân tạo chất lượng cao', '/web/img/field/san-bong-da-3.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0593, 108.2146, NOW(), NOW()),
('Sân bóng đá 4', 'san-bong-da-4', '321 Phan Đình Phùng, Hải Châu', 1, 3, 1, 'Sân bóng 7 người, có đèn chiếu sáng', '/web/img/field/san-bong-da-4.jpg', '08:00:00', '23:00:00', 'ACTIVE', 16.0595, 108.2148, NOW(), NOW()),
('Sân bóng đá 5', 'san-bong-da-5', '654 Lý Thường Kiệt, Hải Châu', 1, 3, 1, 'Sân bóng 7 người, chất lượng cỏ nhân tạo cao', '/web/img/field/san-bong-da-5.jpg', '06:00:00', '20:00:00', 'ACTIVE', 16.0597, 108.2150, NOW(), NOW()),
('Sân bóng đá 6', 'san-bong-da-6', '987 Hai Bà Trưng, Hải Châu', 1, 3, 1, 'Sân bóng 5 người, có mái che', '/web/img/field/san-bong-da-6.jpg', '09:00:00', '22:00:00', 'PENDING', 16.0599, 108.2152, NOW(), NOW()),
('Sân bóng đá 7', 'san-bong-da-7', '159 Nguyễn Trãi, Hải Châu', 1, 3, 1, 'Sân bóng 7 người, đèn chiếu sáng đầy đủ', '/web/img/field/san-bong-da-7.jpg', '06:00:00', '21:00:00', 'ACTIVE', 16.0601, 108.2154, NOW(), NOW()),
('Sân bóng đá 8', 'san-bong-da-8', '753 Trần Hưng Đạo, Sơn Trà', 1, 3, 1, 'Sân bóng 5 người, cỏ nhân tạo chất lượng', '/web/img/field/san-bong-da-8.jpg', '07:00:00', '23:00:00', 'ACTIVE', 16.0610, 108.2160, NOW(), NOW()),
('Sân bóng đá 9', 'san-bong-da-9', '852 Hoàng Văn Thụ, Hải Châu', 1, 3, 1, 'Sân bóng 7 người, có phòng thay đồ', '/web/img/field/san-bong-da-9.jpg', '08:00:00', '22:00:00', 'PENDING', 16.0612, 108.2162, NOW(), NOW()),
('Sân bóng đá 10', 'san-bong-da-10', '951 Lê Lợi, Hải Châu', 1, 3, 1, 'Sân bóng 5 người, có đèn LED chiếu sáng', '/web/img/field/san-bong-da-10.jpg', '06:00:00', '23:00:00', 'ACTIVE', 16.0614, 108.2164, NOW(), NOW()),
('Sân bóng đá 11', 'san-bong-da-11', '357 Nguyễn Huệ, Hải Châu', 1, 3, 1, 'Sân bóng 7 người, chất lượng cỏ nhân tạo cao', '/web/img/field/san-bong-da-11.jpg', '07:00:00', '20:00:00', 'ACTIVE', 16.0616, 108.2166, NOW(), NOW()),
('Sân bóng đá 12', 'san-bong-da-12', '258 Phạm Ngọc Thạch, Hải Châu', 1, 3, 1, 'Sân bóng 5 người, có mái che và đèn', '/web/img/field/san-bong-da-12.jpg', '08:00:00', '22:00:00', 'PENDING', 16.0618, 108.2168, NOW(), NOW()),

-- Cầu lông (field_type_id = 2)
('Sân cầu lông 1', 'san-cau-long-1', '12 Trần Phú, Hải Châu', 2, 3, 2, 'Sân cầu lông tiêu chuẩn, nền gỗ chống trơn trượt', '/web/img/field/san-cau-long-1.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0589, 108.2140, NOW(), NOW()),
('Sân cầu lông 2', 'san-cau-long-2', '34 Phan Chu Trinh, Hải Châu', 2, 3, 2, 'Sân cầu lông trong nhà, có điều hòa', '/web/img/field/san-cau-long-2.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0591, 108.2143, NOW(), NOW()),
('Sân cầu lông 3', 'san-cau-long-3', '56 Lý Thường Kiệt, Hải Châu', 2, 3, 2, 'Sân cầu lông 2 người, nền gỗ chất lượng', '/web/img/field/san-cau-long-3.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0593, 108.2146, NOW(), NOW()),
('Sân cầu lông 4', 'san-cau-long-4', '78 Nguyễn Trãi, Hải Châu', 2, 3, 2, 'Sân cầu lông trong nhà, có đèn chiếu sáng', '/web/img/field/san-cau-long-4.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0595, 108.2148, NOW(), NOW()),
('Sân cầu lông 5', 'san-cau-long-5', '90 Trần Hưng Đạo, Sơn Trà', 2, 3, 2, 'Sân cầu lông 4 người, sàn gỗ cao cấp', '/web/img/field/san-cau-long-5.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0610, 108.2160, NOW(), NOW()),

-- Tennis (field_type_id = 3)
('Sân Tennis 1', 'san-tennis-1', '56 Nguyễn Hoàng, Hải Châu', 1, 3, 3, 'Sân tennis ngoài trời, mặt sân cứng', '/web/img/field/san-tennis-1.jpg', '06:00:00', '23:00:00', 'ACTIVE', 16.0592, 108.2145, NOW(), NOW()),
('Sân Tennis 2', 'san-tennis-2', '78 Hoàng Diệu, Hải Châu', 1, 3, 3, 'Sân tennis tiêu chuẩn quốc tế', '/web/img/field/san-tennis-2.jpg', '06:00:00', '23:00:00', 'ACTIVE', 16.0594, 108.2148, NOW(), NOW()),
('Sân Tennis 3', 'san-tennis-3', '12 Trần Phú, Hải Châu', 1, 3, 3, 'Sân tennis trong nhà, có mái che', '/web/img/field/san-tennis-3.jpg', '06:00:00', '23:00:00', 'ACTIVE', 16.0596, 108.2150, NOW(), NOW()),

-- Bóng rổ (field_type_id = 4)
('Sân Bóng rổ 1', 'san-bong-ro-1', '90 Lý Thái Tổ, Hải Châu', 1, 3, 4, 'Sân bóng rổ ngoài trời, phù hợp thi đấu 5x5', '/web/img/field/san-bong-ro-1.jpg', '08:00:00', '22:00:00', 'ACTIVE', 16.0598, 108.2152, NOW(), NOW()),
('Sân Bóng rổ 2', 'san-bong-ro-2', '22 Nguyễn Văn Cừ, Hải Châu', 1, 3, 4, 'Sân bóng rổ trong nhà, sàn gỗ', '/web/img/field/san-bong-ro-2.jpg', '08:00:00', '22:00:00', 'ACTIVE', 16.0600, 108.2154, NOW(), NOW()),
('Sân Bóng rổ 3', 'san-bong-ro-3', '44 Lê Lợi, Hải Châu', 1, 3, 4, 'Sân bóng rổ tiêu chuẩn, đèn chiếu sáng đầy đủ', '/web/img/field/san-bong-ro-3.jpg', '08:00:00', '22:00:00', 'ACTIVE', 16.0602, 108.2156, NOW(), NOW()),

-- Bơi lội (field_type_id = 5)
('Hồ Bơi 1', 'ho-boi-1', '11 Điện Biên Phủ, Hải Châu', 1, 3, 5, 'Hồ bơi 25m, phù hợp luyện tập và thi đấu', '/web/img/field/ho-boi-1.jpg', '05:30:00', '20:30:00', 'ACTIVE', 16.0604, 108.2158, NOW(), NOW()),
('Hồ Bơi 2', 'ho-boi-2', '33 Nguyễn Chí Thanh, Hải Châu', 1, 3, 5, 'Hồ bơi ngoài trời, có khu vui chơi trẻ em', '/web/img/field/ho-boi-2.jpg', '05:30:00', '20:30:00', 'ACTIVE', 16.0606, 108.2160, NOW(), NOW()),
('Hồ Bơi 3', 'ho-boi-3', '55 Phạm Ngọc Thạch, Hải Châu', 1, 3, 5, 'Hồ bơi tiêu chuẩn 50m, trong nhà', '/web/img/field/ho-boi-3.jpg', '05:30:00', '20:30:00', 'ACTIVE', 16.0608, 108.2162, NOW(), NOW()),

-- Pickleball (field_type_id = 6)
('Sân Pickleball 1', 'san-pickleball-1', '10 Trần Phú, Hải Châu', 6, 3, 6, 'Sân Pickleball tiêu chuẩn, nền gỗ', '/web/img/field/san-pickleball-1.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0289, 108.2240, NOW(), NOW()),
('Sân Pickleball 2', 'san-pickleball-2', '22 Phan Chu Trinh, Hải Châu', 6, 3, 6, 'Sân Pickleball trong nhà, có điều hòa', '/web/img/field/san-pickleball-2.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0521, 108.2123, NOW(), NOW()),
('Sân Pickleball 3', 'san-pickleball-3', '44 Lý Thường Kiệt, Hải Châu', 6, 3, 6, 'Sân Pickleball 2 người, nền gỗ chất lượng', '/web/img/field/san-pickleball-3.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0513, 108.2346, NOW(), NOW()),
('Sân Pickleball 4', 'san-pickleball-4', '66 Nguyễn Trãi, Hải Châu', 6, 3, 6, 'Sân Pickleball trong nhà, có đèn chiếu sáng', '/web/img/field/san-pickleball-4.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0585, 108.2748, NOW(), NOW()),
('Sân Pickleball 5', 'san-pickleball-5', '88 Trần Hưng Đạo, Sơn Trà', 6, 3, 6, 'Sân Pickleball 4 người, sàn gỗ cao cấp', '/web/img/field/san-pickleball-5.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0690, 108.6160, NOW(), NOW()),
('Sân Pickleball 6', 'san-pickleball-6', '100 Hoàng Văn Thụ, Hải Châu', 6, 3, 6, 'Sân Pickleball tiêu chuẩn, nền gỗ', '/web/img/field/san-pickleball-6.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0613, 108.2762, NOW(), NOW()),
('Sân Pickleball 7', 'san-pickleball-7', '122 Lê Lợi, Hải Châu', 6, 3, 6, 'Sân Pickleball trong nhà, có điều hòa', '/web/img/field/san-pickleball-7.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0914, 108.2064, NOW(), NOW()),
('Sân Pickleball 8', 'san-pickleball-8', '144 Nguyễn Huệ, Hải Châu', 6, 3, 6, 'Sân Pickleball 2 người, nền gỗ chất lượng', '/web/img/field/san-pickleball-8.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0316, 108.5666, NOW(), NOW()),
('Sân Pickleball 9', 'san-pickleball-9', '166 Phạm Ngọc Thạch, Hải Châu', 6, 3, 6, 'Sân Pickleball trong nhà, có đèn chiếu sáng', '/web/img/field/san-pickleball-9.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.9818, 108.7868, NOW(), NOW()),
('Sân Pickleball 10', 'san-pickleball-10', '188 Trần Phú, Hải Châu', 6, 3, 6, 'Sân Pickleball 4 người, sàn gỗ cao cấp', '/web/img/field/san-pickleball-10.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.8620, 108.2270, NOW(), NOW());


INSERT INTO field_feature (field_id, feature_id) VALUES
(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),
(2,1),(2,2),(2,3),(2,4),(2,5),(2,6),(2,7),(2,8),
(3,1),(3,2),(3,3),(3,4),(3,5),(3,6),(3,7),(3,8),
(4,1),(4,2),(4,3),(4,4),(4,5),(4,6),(4,7),(4,8),
(5,1),(5,2),(5,3),(5,4),(5,5),(5,6),(5,7),(5,8),
(6,1),(6,2),(6,3),(6,4),(6,5),(6,6),(6,7),(6,8),
(7,1),(7,2),(7,3),(7,4),(7,5),(7,6),(7,7),(7,8),
(8,1),(8,2),(8,3),(8,4),(8,5),(8,6),(8,7),(8,8),
(9,1),(9,2),(9,3),(9,4),(9,5),(9,6),(9,7),(9,8),
(10,1),(10,2),(10,3),(10,4),(10,5),(10,6),(10,7),(10,8),
(11,1),(11,2),(11,3),(11,4),(11,5),(11,6),(11,7),(11,8),
(12,1),(12,2),(12,3),(12,4),(12,5),(12,6),(12,7),(12,8),

(13,1),(13,2),(13,3),(13,4),(13,5),(13,6),(13,9),(13,10),
(14,1),(14,2),(14,3),(14,4),(14,5),(14,6),(14,9),(14,10),
(15,1),(15,2),(15,3),(15,4),(15,5),(15,6),(13,9),(13,10),
(16,1),(16,2),(16,3),(16,4),(16,5),(16,6),(16,9),(16,10),
(17,1),(17,2),(17,3),(17,4),(17,5),(17,6),(17,9),(17,10),

(18,1),(18,2),(18,3),(18,4),(18,5),(18,6),(18,9),(18,10),
(19,1),(19,2),(19,3),(19,4),(19,5),(19,6),(19,9),(19,10),
(20,1),(20,2),(20,3),(20,4),(20,5),(20,6),(20,9),(20,10),

(21,1),(21,2),(21,3),(21,4),(21,5),(21,6),(21,13),(21,14),
(22,1),(22,2),(22,3),(22,4),(22,5),(22,6),(22,13),(22,14),
(23,1),(23,2),(23,3),(23,4),(23,5),(23,6),(23,13),(23,14),

(24,1),(24,2),(24,3),(24,4),(24,5),(24,6),(24,11),(24,12),
(25,1),(25,2),(25,3),(25,4),(25,5),(25,6),(25,11),(25,12),
(26,1),(26,2),(26,3),(26,4),(26,5),(26,6),(26,11),(26,12),

(27,1),(27,2),(27,3),(27,4),(27,5),(27,6),(27,9),(27,10),
(28,1),(28,2),(28,3),(28,4),(28,5),(28,6),(28,9),(28,10),
(29,1),(29,2),(29,3),(29,4),(29,5),(29,6),(29,9),(29,10),
(30,1),(30,2),(30,3),(30,4),(30,5),(30,6),(30,9),(30,10),
(31,1),(31,2),(31,3),(31,4),(31,5),(31,6),(31,9),(31,10),
(32,1),(32,2),(32,3),(32,4),(32,5),(32,6),(32,9),(32,10),
(33,1),(33,2),(33,3),(33,4),(33,5),(33,6),(33,9),(33,10),
(34,1),(34,2),(34,3),(34,4),(34,5),(34,6),(34,9),(34,10),
(35,1),(35,2),(35,3),(35,4),(35,5),(35,6),(35,9),(35,10),
(36,1),(36,2),(36,3),(36,4),(36,5),(36,6),(36,9),(36,10);


-- Bóng đá (field_id 1 → 12), mỗi sân chính có 2 sub_field
INSERT INTO sub_field (name, status, field_id, created_at, updated_at) VALUES
('Sân con 1', 'ACTIVE', 1, NOW(), NOW()),
('Sân con 2', 'ACTIVE', 1, NOW(), NOW()),
('Sân con 1', 'ACTIVE', 2, NOW(), NOW()),
('Sân con 2', 'ACTIVE', 2, NOW(), NOW()),
('Sân con 1', 'ACTIVE', 3, NOW(), NOW()),
('Sân con 2', 'ACTIVE', 3, NOW(), NOW()),
('Sân con 1', 'ACTIVE', 4, NOW(), NOW()),
('Sân con 2', 'ACTIVE', 4, NOW(), NOW()),
('Sân con 1', 'ACTIVE', 5, NOW(), NOW()),
('Sân con 2', 'ACTIVE', 5, NOW(), NOW()),
('Sân con 1', 'ACTIVE', 6, NOW(), NOW()),
('Sân con 2', 'ACTIVE', 6, NOW(), NOW()),
('Sân con 1', 'ACTIVE', 7, NOW(), NOW()),
('Sân con 2', 'ACTIVE', 7, NOW(), NOW()),
('Sân con 1', 'ACTIVE', 8, NOW(), NOW()),
('Sân con 2', 'ACTIVE', 8, NOW(), NOW()),
('Sân con 1', 'ACTIVE', 9, NOW(), NOW()),
('Sân con 2', 'ACTIVE', 9, NOW(), NOW()),
('Sân con 1', 'ACTIVE', 10, NOW(), NOW()),
('Sân con 2', 'ACTIVE', 10, NOW(), NOW()),
('Sân con 1', 'ACTIVE', 11, NOW(), NOW()),
('Sân con 2', 'ACTIVE', 11, NOW(), NOW()),
('Sân con 1', 'ACTIVE', 12, NOW(), NOW()),
('Sân con 2', 'ACTIVE', 12, NOW(), NOW()),

-- Field 27
('Sân con 1', 'ACTIVE', 27, NOW(), NOW()),
('Sân con 2', 'ACTIVE', 27, NOW(), NOW()),
('Sân con 3', 'ACTIVE', 27, NOW(), NOW()),
-- Field 28
('Sân con 1', 'ACTIVE', 28, NOW(), NOW()),
('Sân con 2', 'ACTIVE', 28, NOW(), NOW()),
('Sân con 3', 'ACTIVE', 28, NOW(), NOW());


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

-- Bóng đá: field_id 1-12, Pickleball: field_id 27-36
-- time_slot_id 1-8 từ bảng time_slot, price ví dụ 200000 cho bóng đá, 150000 cho pickleball
INSERT INTO field_time_slot (field_id, time_slot_id, price) VALUES
-- Bóng đá
(1, 1, 200000),(1, 2, 200000),(1, 3, 200000),(1, 4, 200000),(1, 5, 200000),(1, 6, 200000),(1, 7, 200000),(1, 8, 200000),
(2, 1, 200000),(2, 2, 200000),(2, 3, 200000),(2, 4, 200000),(2, 5, 200000),(2, 6, 200000),(2, 7, 200000),(2, 8, 200000),
(3, 1, 200000),(3, 2, 200000),(3, 3, 200000),(3, 4, 200000),(3, 5, 200000),(3, 6, 200000),(3, 7, 200000),(3, 8, 200000),
(4, 1, 200000),(4, 2, 200000),(4, 3, 200000),(4, 4, 200000),(4, 5, 200000),(4, 6, 200000),(4, 7, 200000),(4, 8, 200000),
(5, 1, 200000),(5, 2, 200000),(5, 3, 200000),(5, 4, 200000),(5, 5, 200000),(5, 6, 200000),(5, 7, 200000),(5, 8, 200000),
(6, 1, 200000),(6, 2, 200000),(6, 3, 200000),(6, 4, 200000),(6, 5, 200000),(6, 6, 200000),(6, 7, 200000),(6, 8, 200000),
(7, 1, 200000),(7, 2, 200000),(7, 3, 200000),(7, 4, 200000),(7, 5, 200000),(7, 6, 200000),(7, 7, 200000),(7, 8, 200000),
(8, 1, 200000),(8, 2, 200000),(8, 3, 200000),(8, 4, 200000),(8, 5, 200000),(8, 6, 200000),(8, 7, 200000),(8, 8, 200000),
(9, 1, 200000),(9, 2, 200000),(9, 3, 200000),(9, 4, 200000),(9, 5, 200000),(9, 6, 200000),(9, 7, 200000),(9, 8, 200000),
(10, 1, 200000),(10, 2, 200000),(10, 3, 200000),(10, 4, 200000),(10, 5, 200000),(10, 6, 200000),(10, 7, 200000),(10, 8, 200000),
(11, 1, 200000),(11, 2, 200000),(11, 3, 200000),(11, 4, 200000),(11, 5, 200000),(11, 6, 200000),(11, 7, 200000),(11, 8, 200000),
(12, 1, 200000),(12, 2, 200000),(12, 3, 200000),(12, 4, 200000),(12, 5, 200000),(12, 6, 200000),(12, 7, 200000),(12, 8, 200000),

-- Pickleball
(27, 1, 150000),(27, 2, 150000),(27, 3, 150000),(27, 4, 150000),(27, 5, 150000),(27, 6, 150000),(27, 7, 150000),(27, 8, 150000),
(28, 1, 150000),(28, 2, 150000),(28, 3, 150000),(28, 4, 150000),(28, 5, 150000),(28, 6, 150000),(28, 7, 150000),(28, 8, 150000),
(29, 1, 150000),(29, 2, 150000),(29, 3, 150000),(29, 4, 150000),(29, 5, 150000),(29, 6, 150000),(29, 7, 150000),(29, 8, 150000),
(30, 1, 150000),(30, 2, 150000),(30, 3, 150000),(30, 4, 150000),(30, 5, 150000),(30, 6, 150000),(30, 7, 150000),(30, 8, 150000),
(31, 1, 150000),(31, 2, 150000),(31, 3, 150000),(31, 4, 150000),(31, 5, 150000),(31, 6, 150000),(31, 7, 150000),(31, 8, 150000),
(32, 1, 150000),(32, 2, 150000),(32, 3, 150000),(32, 4, 150000),(32, 5, 150000),(32, 6, 150000),(32, 7, 150000),(32, 8, 150000),
(33, 1, 150000),(33, 2, 150000),(33, 3, 150000),(33, 4, 150000),(33, 5, 150000),(33, 6, 150000),(33, 7, 150000),(33, 8, 150000),
(34, 1, 150000),(34, 2, 150000),(34, 3, 150000),(34, 4, 150000),(34, 5, 150000),(34, 6, 150000),(34, 7, 150000),(34, 8, 150000),
(35, 1, 150000),(35, 2, 150000),(35, 3, 150000),(35, 4, 150000),(35, 5, 150000),(35, 6, 150000),(35, 7, 150000),(35, 8, 150000),
(36, 1, 150000),(36, 2, 150000),(36, 3, 150000),(36, 4, 150000),(36, 5, 150000),(36, 6, 150000),(36, 7, 150000),(36, 8, 150000);


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



