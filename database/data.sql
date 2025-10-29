
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
    ('johndoe', 'dattranquoc@gmail.com', '$2a$10$EeSehs49igNMz6Vuk69cDuaAGHFrWSjeOvMmNkaAr6ZwyZtltKStS', '0912345678', '1998-08-15', 'ACTIVE', 'FEMALE', 0,  NULL, 2),
    ('vitidsarn', 'dattran@gmail.com', '$2a$10$llGgE5VlZzM0.pCzbOLGWev.cqdovrjSsq0lGM87wo0FVgATXsh12', '0934567890', '1990-01-10', 'ACTIVE', 'MALE', 0, NULL, 3),
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
('Sân bóng đá 1', 'san-bong-da-1', '12 Trần Phú, Hải Châu', 1, 3, 1, 'Sân bóng 5 người, cỏ nhân tạo chất lượng cao', '/web/img/field/football/san-bong-da-1.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0589, 108.2140, NOW(), NOW()),
('Sân bóng đá 2', 'san-bong-da-2', '34 Phan Chu Trinh, Hải Châu', 1, 3, 1, 'Sân bóng 7 người, có đèn chiếu sáng', '/web/img/field/football/san-bong-da-2.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0591, 108.2143, NOW(), NOW()),
('Sân bóng đá 3', 'san-bong-da-3', '789 Trần Phú, Hải Châu', 1, 3, 1, 'Sân bóng 5 người, cỏ nhân tạo chất lượng cao', '/web/img/field/football/san-bong-da-3.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0593, 108.2146, NOW(), NOW()),
('Sân bóng đá 4', 'san-bong-da-4', '321 Phan Đình Phùng, Hải Châu', 1, 3, 1, 'Sân bóng 7 người, có đèn chiếu sáng', '/web/img/field/football/san-bong-da-4.jpg', '08:00:00', '23:00:00', 'ACTIVE', 16.0595, 108.2148, NOW(), NOW()),
('Sân bóng đá 5', 'san-bong-da-5', '654 Lý Thường Kiệt, Hải Châu', 1, 3, 1, 'Sân bóng 7 người, chất lượng cỏ nhân tạo cao', '/web/img/field/football/san-bong-da-5.jpg', '06:00:00', '20:00:00', 'ACTIVE', 16.0597, 108.2150, NOW(), NOW()),
('Sân bóng đá 6', 'san-bong-da-6', '987 Hai Bà Trưng, Hải Châu', 1, 3, 1, 'Sân bóng 5 người, có mái che', '/web/img/field/football/san-bong-da-6.jpg', '09:00:00', '22:00:00', 'PENDING', 16.0599, 108.2152, NOW(), NOW()),
('Sân bóng đá 7', 'san-bong-da-7', '159 Nguyễn Trãi, Hải Châu', 1, 3, 1, 'Sân bóng 7 người, đèn chiếu sáng đầy đủ', '/web/img/field/football/san-bong-da-7.jpg', '06:00:00', '21:00:00', 'ACTIVE', 16.0601, 108.2154, NOW(), NOW()),
('Sân bóng đá 8', 'san-bong-da-8', '753 Trần Hưng Đạo, Sơn Trà', 1, 3, 1, 'Sân bóng 5 người, cỏ nhân tạo chất lượng', '/web/img/field/football/san-bong-da-8.jpg', '07:00:00', '23:00:00', 'ACTIVE', 16.0610, 108.2160, NOW(), NOW()),
('Sân bóng đá 9', 'san-bong-da-9', '852 Hoàng Văn Thụ, Hải Châu', 1, 3, 1, 'Sân bóng 7 người, có phòng thay đồ', '/web/img/field/football/san-bong-da-9.jpg', '08:00:00', '22:00:00', 'PENDING', 16.0612, 108.2162, NOW(), NOW()),
('Sân bóng đá 10', 'san-bong-da-10', '951 Lê Lợi, Hải Châu', 1, 3, 1, 'Sân bóng 5 người, có đèn LED chiếu sáng', '/web/img/field/football/san-bong-da-10.jpg', '06:00:00', '23:00:00', 'ACTIVE', 16.0614, 108.2164, NOW(), NOW()),
('Sân bóng đá 11', 'san-bong-da-11', '357 Nguyễn Huệ, Hải Châu', 1, 3, 1, 'Sân bóng 7 người, chất lượng cỏ nhân tạo cao', '/web/img/field/football/san-bong-da-11.jpg', '07:00:00', '20:00:00', 'ACTIVE', 16.0616, 108.2166, NOW(), NOW()),
('Sân bóng đá 12', 'san-bong-da-12', '258 Phạm Ngọc Thạch, Hải Châu', 1, 3, 1, 'Sân bóng 5 người, có mái che và đèn', '/web/img/field/football/san-bong-da-12.jpg', '08:00:00', '22:00:00', 'PENDING', 16.0618, 108.2168, NOW(), NOW()),

-- Cầu lông (field_type_id = 2)
('Sân cầu lông 1', 'san-cau-long-1', '12 Trần Phú, Hải Châu', 2, 3, 2, 'Sân cầu lông tiêu chuẩn, nền gỗ chống trơn trượt', '/web/img/field/badminton/football/san-cau-long-1.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0589, 108.2140, NOW(), NOW()),
('Sân cầu lông 2', 'san-cau-long-2', '34 Phan Chu Trinh, Hải Châu', 2, 3, 2, 'Sân cầu lông trong nhà, có điều hòa', '/web/img/field/badminton/san-cau-long-2.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0591, 108.2143, NOW(), NOW()),
('Sân cầu lông 3', 'san-cau-long-3', '56 Lý Thường Kiệt, Hải Châu', 2, 3, 2, 'Sân cầu lông 2 người, nền gỗ chất lượng', '/web/img/field/badminton/san-cau-long-3.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0593, 108.2146, NOW(), NOW()),
('Sân cầu lông 4', 'san-cau-long-4', '78 Nguyễn Trãi, Hải Châu', 2, 3, 2, 'Sân cầu lông trong nhà, có đèn chiếu sáng', '/web/img/field/badminton/san-cau-long-4.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0595, 108.2148, NOW(), NOW()),
('Sân cầu lông 5', 'san-cau-long-5', '90 Trần Hưng Đạo, Sơn Trà', 2, 3, 2, 'Sân cầu lông 4 người, sàn gỗ cao cấp', '/web/img/field/badminton/san-cau-long-5.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0610, 108.2160, NOW(), NOW()),

-- Tennis (field_type_id = 3)
('Sân Tennis 1', 'san-tennis-1', '56 Nguyễn Hoàng, Hải Châu', 1, 3, 3, 'Sân tennis ngoài trời, mặt sân cứng', '/web/img/field/tennis/san-tennis-1.jpg', '06:00:00', '23:00:00', 'ACTIVE', 16.0592, 108.2145, NOW(), NOW()),
('Sân Tennis 2', 'san-tennis-2', '78 Hoàng Diệu, Hải Châu', 1, 3, 3, 'Sân tennis tiêu chuẩn quốc tế', '/web/img/field/tennis/san-tennis-2.jpg', '06:00:00', '23:00:00', 'ACTIVE', 16.0594, 108.2148, NOW(), NOW()),
('Sân Tennis 3', 'san-tennis-3', '12 Trần Phú, Hải Châu', 1, 3, 3, 'Sân tennis trong nhà, có mái che', '/web/img/field/tennis/san-tennis-3.jpg', '06:00:00', '23:00:00', 'ACTIVE', 16.0596, 108.2150, NOW(), NOW()),

-- Bóng rổ (field_type_id = 4)
('Sân Bóng rổ 1', 'san-bong-ro-1', '90 Lý Thái Tổ, Hải Châu', 1, 3, 4, 'Sân bóng rổ ngoài trời, phù hợp thi đấu 5x5', '/web/img/field/basketball/san-bong-ro-1.jpg', '08:00:00', '22:00:00', 'ACTIVE', 16.0598, 108.2152, NOW(), NOW()),
('Sân Bóng rổ 2', 'san-bong-ro-2', '22 Nguyễn Văn Cừ, Hải Châu', 1, 3, 4, 'Sân bóng rổ trong nhà, sàn gỗ', '/web/img/field/basketball/san-bong-ro-2.jpg', '08:00:00', '22:00:00', 'ACTIVE', 16.0600, 108.2154, NOW(), NOW()),
('Sân Bóng rổ 3', 'san-bong-ro-3', '44 Lê Lợi, Hải Châu', 1, 3, 4, 'Sân bóng rổ tiêu chuẩn, đèn chiếu sáng đầy đủ', '/web/img/field/basketball/san-bong-ro-3.jpg', '08:00:00', '22:00:00', 'ACTIVE', 16.0602, 108.2156, NOW(), NOW()),

-- Bơi lội (field_type_id = 5)
('Hồ Bơi 1', 'ho-boi-1', '11 Điện Biên Phủ, Hải Châu', 1, 3, 5, 'Hồ bơi 25m, phù hợp luyện tập và thi đấu', '/web/img/field/swimming/ho-boi-1.jpg', '06:00:00', '20:00:00', 'ACTIVE', 16.0604, 108.2158, NOW(), NOW()),
('Hồ Bơi 2', 'ho-boi-2', '33 Nguyễn Chí Thanh, Hải Châu', 1, 3, 5, 'Hồ bơi ngoài trời, có khu vui chơi trẻ em', '/web/img/field/swimming/ho-boi-2.jpg', '06:00:00', '20:00:00', 'ACTIVE', 16.0606, 108.2160, NOW(), NOW()),
('Hồ Bơi 3', 'ho-boi-3', '55 Phạm Ngọc Thạch, Hải Châu', 1, 3, 5, 'Hồ bơi tiêu chuẩn 50m, trong nhà', '/web/img/field/swimming/ho-boi-3.jpg', '05:30:00', '06:00:00', 'ACTIVE', 16.0608, 108.2162, NOW(), NOW()),

-- Pickleball (field_type_id = 6)
('Sân Pickleball 1', 'san-pickleball-1', '10 Trần Phú, Hải Châu', 6, 3, 6, 'Sân Pickleball tiêu chuẩn, nền gỗ', '/web/img/field/pickleball/san-pickleball-1.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0289, 108.2240, NOW(), NOW()),
('Sân Pickleball 2', 'san-pickleball-2', '22 Phan Chu Trinh, Hải Châu', 6, 3, 6, 'Sân Pickleball trong nhà, có điều hòa', '/web/img/field/pickleball/san-pickleball-2.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0521, 108.2123, NOW(), NOW()),
('Sân Pickleball 3', 'san-pickleball-3', '44 Lý Thường Kiệt, Hải Châu', 6, 3, 6, 'Sân Pickleball 2 người, nền gỗ chất lượng', '/web/img/field/pickleball/san-pickleball-3.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0513, 108.2346, NOW(), NOW()),
('Sân Pickleball 4', 'san-pickleball-4', '66 Nguyễn Trãi, Hải Châu', 6, 3, 6, 'Sân Pickleball trong nhà, có đèn chiếu sáng', '/web/img/field/pickleball/san-pickleball-4.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0585, 108.2748, NOW(), NOW()),
('Sân Pickleball 5', 'san-pickleball-5', '88 Trần Hưng Đạo, Sơn Trà', 6, 3, 6, 'Sân Pickleball 4 người, sàn gỗ cao cấp', '/web/img/field/pickleball/san-pickleball-5.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0690, 108.6160, NOW(), NOW()),
('Sân Pickleball 6', 'san-pickleball-6', '100 Hoàng Văn Thụ, Hải Châu', 6, 3, 6, 'Sân Pickleball tiêu chuẩn, nền gỗ', '/web/img/field/pickleball/san-pickleball-1.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0613, 108.2762, NOW(), NOW()),
('Sân Pickleball 7', 'san-pickleball-7', '122 Lê Lợi, Hải Châu', 6, 3, 6, 'Sân Pickleball trong nhà, có điều hòa', '/web/img/field/pickleball/san-pickleball-2.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0914, 108.2064, NOW(), NOW()),
('Sân Pickleball 8', 'san-pickleball-8', '144 Nguyễn Huệ, Hải Châu', 6, 3, 6, 'Sân Pickleball 2 người, nền gỗ chất lượng', '/web/img/field/pickleball/san-pickleball-3.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.0316, 108.5666, NOW(), NOW()),
('Sân Pickleball 9', 'san-pickleball-9', '166 Phạm Ngọc Thạch, Hải Châu', 6, 3, 6, 'Sân Pickleball trong nhà, có đèn chiếu sáng', '/web/img/field/pickleball/san-pickleball-4.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.9818, 108.7868, NOW(), NOW()),
('Sân Pickleball 10', 'san-pickleball-10', '188 Trần Phú, Hải Châu', 6, 3, 6, 'Sân Pickleball 4 người, sàn gỗ cao cấp', '/web/img/field/pickleball/san-pickleball-5.jpg', '07:00:00', '21:00:00', 'ACTIVE', 16.8620, 108.2270, NOW(), NOW());


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
                                                                           ('Sân con 1', 'AVAILABLE', 1, NOW(), NOW()),
                                                                           ('Sân con 2', 'AVAILABLE', 1, NOW(), NOW()),
                                                                           ('Sân con 1', 'AVAILABLE', 2, NOW(), NOW()),
                                                                           ('Sân con 2', 'AVAILABLE', 2, NOW(), NOW()),
                                                                           ('Sân con 1', 'AVAILABLE', 3, NOW(), NOW()),
                                                                           ('Sân con 2', 'AVAILABLE', 3, NOW(), NOW()),
                                                                           ('Sân con 1', 'AVAILABLE', 4, NOW(), NOW()),
                                                                           ('Sân con 2', 'AVAILABLE', 4, NOW(), NOW()),
                                                                           ('Sân con 1', 'AVAILABLE', 5, NOW(), NOW()),
                                                                           ('Sân con 2', 'AVAILABLE', 5, NOW(), NOW()),
                                                                           ('Sân con 1', 'AVAILABLE', 6, NOW(), NOW()),
                                                                           ('Sân con 2', 'AVAILABLE', 6, NOW(), NOW()),
                                                                           ('Sân con 1', 'AVAILABLE', 7, NOW(), NOW()),
                                                                           ('Sân con 2', 'AVAILABLE', 7, NOW(), NOW()),
                                                                           ('Sân con 1', 'AVAILABLE', 8, NOW(), NOW()),
                                                                           ('Sân con 2', 'AVAILABLE', 8, NOW(), NOW()),
                                                                           ('Sân con 1', 'AVAILABLE', 9, NOW(), NOW()),
                                                                           ('Sân con 2', 'AVAILABLE', 9, NOW(), NOW()),
                                                                           ('Sân con 1', 'AVAILABLE', 10, NOW(), NOW()),
                                                                           ('Sân con 2', 'AVAILABLE', 10, NOW(), NOW()),
                                                                           ('Sân con 1', 'AVAILABLE', 11, NOW(), NOW()),
                                                                           ('Sân con 2', 'AVAILABLE', 11, NOW(), NOW()),
                                                                           ('Sân con 1', 'AVAILABLE', 12, NOW(), NOW()),
                                                                           ('Sân con 2', 'AVAILABLE', 12, NOW(), NOW()),

-- Field 27
                                                                           ('Sân con 1', 'MAINTAIN', 27, NOW(), NOW()),
                                                                           ('Sân con 2', 'AVAILABLE', 27, NOW(), NOW()),
                                                                           ('Sân con 3', 'AVAILABLE', 27, NOW(), NOW()),
-- Field 28
                                                                           ('Sân con 1', 'AVAILABLE', 28, NOW(), NOW()),
                                                                           ('Sân con 2', 'MAINTAIN', 28, NOW(), NOW()),
                                                                           ('Sân con 3', 'AVAILABLE', 28, NOW(), NOW());


-- ========================
-- 5. Time Slot & Field Slot
-- ========================


INSERT INTO time_slot (start_time)
VALUES
    ('05:00:00'),
    ('06:00:00'),
    ('07:00:00'),
    ('08:00:00'),
    ('09:00:00'),
    ('10:00:00'),
    ('11:00:00'),
    ('12:00:00'),
    ('13:00:00'),
    ('14:00:00'),
    ('15:00:00'),
    ('16:00:00'),
    ('17:00:00'),
    ('18:00:00'),
    ('19:00:00'),
    ('20:00:00'),
    ('21:00:00'),
    ('22:00:00'),
    ('23:00:00');

-- ======================================
-- INSERT TIME SLOTS FOR FOOTBALL (field_id 1-5)
-- ======================================
INSERT INTO field_time_slot (field_id, time_slot_id, price) VALUES
-- Field 1
(1,1,100000),(1,2,100000),(1,3,120000),(1,4,120000),(1,5,150000),(1,6,150000),(1,7,150000),(1,8,150000),(1,9,150000),
(1,10,150000),(1,11,150000),(1,12,150000),(1,13,150000),(1,14,150000),(1,15,150000),(1,16,150000),(1,17,150000),(1,18,150000),(1,19,150000),
-- Field 2
(2,1,100000),(2,2,100000),(2,3,120000),(2,4,120000),(2,5,150000),(2,6,150000),(2,7,150000),(2,8,150000),(2,9,150000),
(2,10,150000),(2,11,150000),(2,12,150000),(2,13,150000),(2,14,150000),(2,15,150000),(2,16,150000),(2,17,150000),(2,18,150000),(2,19,150000),
-- Field 3
(3,1,100000),(3,2,100000),(3,3,120000),(3,4,120000),(3,5,150000),(3,6,150000),(3,7,150000),(3,8,150000),(3,9,150000),
(3,10,150000),(3,11,150000),(3,12,150000),(3,13,150000),(3,14,150000),(3,15,150000),(3,16,150000),(3,17,150000),(3,18,150000),(3,19,150000),
-- Field 4
(4,1,100000),(4,2,100000),(4,3,120000),(4,4,120000),(4,5,150000),(4,6,150000),(4,7,150000),(4,8,150000),(4,9,150000),
(4,10,150000),(4,11,150000),(4,12,150000),(4,13,150000),(4,14,150000),(4,15,150000),(4,16,150000),(4,17,150000),(4,18,150000),(4,19,150000),
-- Field 5
(5,1,100000),(5,2,100000),(5,3,120000),(5,4,120000),(5,5,150000),(5,6,150000),(5,7,150000),(5,8,150000),(5,9,150000),
(5,10,150000),(5,11,150000),(5,12,150000),(5,13,150000),(5,14,150000),(5,15,150000),(5,16,150000),(5,17,150000),(5,18,150000),(5,19,150000);

-- ======================================
-- INSERT TIME SLOTS FOR PICKLEBALL (field_id 27-31)
-- ======================================
INSERT INTO field_time_slot (field_id, time_slot_id, price) VALUES
-- Field 27
(27,1,80000),(27,2,80000),(27,3,100000),(27,4,100000),(27,5,130000),(27,6,130000),(27,7,130000),(27,8,130000),(27,9,130000),
(27,10,130000),(27,11,130000),(27,12,130000),(27,13,130000),(27,14,130000),(27,15,130000),(27,16,130000),(27,17,130000),(27,18,130000),(27,19,130000),
-- Field 28
(28,1,80000),(28,2,80000),(28,3,100000),(28,4,100000),(28,5,130000),(28,6,130000),(28,7,130000),(28,8,130000),(28,9,130000),
(28,10,130000),(28,11,130000),(28,12,130000),(28,13,130000),(28,14,130000),(28,15,130000),(28,16,130000),(28,17,130000),(28,18,130000),(28,19,130000),
-- Field 29
(29,1,80000),(29,2,80000),(29,3,100000),(29,4,100000),(29,5,130000),(29,6,130000),(29,7,130000),(29,8,130000),(29,9,130000),
(29,10,130000),(29,11,130000),(29,12,130000),(29,13,130000),(29,14,130000),(29,15,130000),(29,16,130000),(29,17,130000),(29,18,130000),(29,19,130000),
-- Field 30
(30,1,80000),(30,2,80000),(30,3,100000),(30,4,100000),(30,5,130000),(30,6,130000),(30,7,130000),(30,8,130000),(30,9,130000),
(30,10,130000),(30,11,130000),(30,12,130000),(30,13,130000),(30,14,130000),(30,15,130000),(30,16,130000),(30,17,130000),(30,18,130000),(30,19,130000),
-- Field 31
(31,1,80000),(31,2,80000),(31,3,100000),(31,4,100000),(31,5,130000),(31,6,130000),(31,7,130000),(31,8,130000),(31,9,130000),
(31,10,130000),(31,11,130000),(31,12,130000),(31,13,130000),(31,14,130000),(31,15,130000),(31,16,130000),(31,17,130000),(31,18,130000),(31,19,130000);


INSERT INTO duration (minutes) VALUES
                                   (60),   -- 1h
                                   (90),   -- 1h30
                                   (120),  -- 2h
                                   (150),  -- 2h30
                                   (180);  -- 3h


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
                                                                 (5, 5);  -- 180

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


INSERT INTO service_item (service_id, name) VALUES
-- Football
(1, 'Giày đá bóng Nike'),
(1, 'Giày đá bóng Adidas'),
(2, 'Bóng đá Nike'),
(2, 'Bóng đá Adidas'),

-- Badminton
(3, 'Vợt cầu lông Yonex'),
(3, 'Vợt cầu lông Li-Ning'),
(4, 'Giày cầu lông Yonex'),
(4, 'Giày cầu lông Li-Ning'),

-- Tennis
(5, 'Vợt tennis Wilson'),
(5, 'Vợt tennis Babolat'),
(6, 'Bóng tennis Wilson'),
(6, 'Bóng tennis Babolat'),

-- Basketball
(7, 'Bóng rổ Spalding'),
(7, 'Bóng rổ Jordan'),
(8, 'Áo bóng rổ Nike'),
(8, 'Áo bóng rổ Jordan'),

-- Swimming
(9, 'Kính bơi Speedo'),
(9, 'Kính bơi Arena'),
(10, 'Phao bơi Speedo'),
(10, 'Phao bơi Arena'),

-- Pickleball
(11, 'Vợt Pickleball Selkirk'),
(11, 'Vợt Pickleball Onix'),
(12, 'Bóng Pickleball Selkirk'),
(12, 'Bóng Pickleball Onix');

INSERT INTO field_service_item (field_id, service_item_id, price, quantity, status) VALUES
-- Football
(1,1,50000,100,'ACTIVE'),(1,2,48000,100,'ACTIVE'),(1,3,30000,100,'ACTIVE'),(1,4,28000,100,'ACTIVE'),
(2,1,52000,100,'ACTIVE'),(2,2,49000,100,'ACTIVE'),(2,3,31000,100,'ACTIVE'),(2,4,29000,100,'ACTIVE'),
(3,1,53000,100,'ACTIVE'),(3,2,50000,100,'ACTIVE'),(3,3,32000,100,'ACTIVE'),(3,4,30000,100,'ACTIVE'),
(4,1,55000,100,'ACTIVE'),(4,2,52000,100,'ACTIVE'),(4,3,33000,100,'ACTIVE'),(4,4,31000,100,'ACTIVE'),
(5,1,54000,100,'ACTIVE'),(5,2,51000,100,'ACTIVE'),(5,3,34000,100,'ACTIVE'),(5,4,32000,100,'ACTIVE'),
(6,1,56000,100,'ACTIVE'),(6,2,53000,100,'ACTIVE'),(6,3,35000,100,'ACTIVE'),(6,4,33000,100,'ACTIVE'),
(7,1,55000,100,'ACTIVE'),(7,2,52000,100,'ACTIVE'),(7,3,34000,100,'ACTIVE'),(7,4,31000,100,'ACTIVE'),
(8,1,57000,100,'ACTIVE'),(8,2,54000,100,'ACTIVE'),(8,3,35000,100,'ACTIVE'),(8,4,32000,100,'ACTIVE'),
(9,1,58000,100,'ACTIVE'),(9,2,55000,100,'ACTIVE'),(9,3,36000,100,'ACTIVE'),(9,4,33000,100,'ACTIVE'),
(10,1,59000,100,'ACTIVE'),(10,2,56000,100,'ACTIVE'),(10,3,37000,100,'ACTIVE'),(10,4,34000,100,'ACTIVE'),
(11,1,60000,100,'ACTIVE'),(11,2,57000,100,'ACTIVE'),(11,3,38000,100,'ACTIVE'),(11,4,35000,100,'ACTIVE'),
(12,1,61000,100,'ACTIVE'),(12,2,58000,100,'ACTIVE'),(12,3,39000,100,'ACTIVE'),(12,4,36000,100,'ACTIVE'),
-- Pickleball
(26,21,40000,100,'ACTIVE'),(26,22,42000,100,'ACTIVE'),(26,23,20000,100,'ACTIVE'),(26,24,22000,100,'ACTIVE'),
(27,21,41000,100,'ACTIVE'),(27,22,43000,100,'ACTIVE'),(27,23,21000,100,'ACTIVE'),(27,24,23000,100,'ACTIVE'),
(28,21,42000,100,'ACTIVE'),(28,22,44000,100,'ACTIVE'),(28,23,22000,100,'ACTIVE'),(28,24,24000,100,'ACTIVE'),
(29,21,43000,100,'ACTIVE'),(29,22,45000,100,'ACTIVE'),(29,23,23000,100,'ACTIVE'),(29,24,25000,100,'ACTIVE'),
(30,21,44000,100,'ACTIVE'),(30,22,46000,100,'ACTIVE'),(30,23,24000,100,'ACTIVE'),(30,24,26000,100,'ACTIVE'),
(31,21,45000,100,'ACTIVE'),(31,22,47000,100,'ACTIVE'),(31,23,25000,100,'ACTIVE'),(31,24,27000,100,'ACTIVE'),
(32,21,46000,100,'ACTIVE'),(32,22,48000,100,'ACTIVE'),(32,23,26000,100,'ACTIVE'),(32,24,28000,100,'ACTIVE'),
(33,21,47000,100,'ACTIVE'),(33,22,49000,100,'ACTIVE'),(33,23,27000,100,'ACTIVE'),(33,24,29000,100,'ACTIVE'),
(34,21,48000,100,'ACTIVE'),(34,22,50000,100,'ACTIVE'),(34,23,28000,100,'ACTIVE'),(34,24,30000,100,'ACTIVE'),
(35,21,49000,100,'ACTIVE'),(35,22,51000,100,'ACTIVE'),(35,23,29000,100,'ACTIVE'),(35,24,31000,100,'ACTIVE');

INSERT INTO voucher (code, description, discount, status, limit_qnt, start_date, end_date, created_at, updated_at)
VALUES
('TEST10', 'Voucher giảm 10%', 10, 'ACTIVE', 100, '2025-10-01', '2025-12-31', NOW(), NOW()),
('TEST20', 'Voucher giảm 20%', 20, 'ACTIVE', 50, '2025-10-01', '2025-12-31', NOW(), NOW()),
('TEST30', 'Voucher giảm 30%', 30, 'ACTIVE', 10, '2025-10-01', '2025-12-31', NOW(), NOW());

INSERT INTO user_voucher(user_id, voucher_id)
VALUES
(1, 1),
(2, 2),
(2, 3),
(4, 1),
(4, 2);

INSERT INTO booking (booking_date, payment_method, start_time, end_time, duration, total_price, status, subfield_id, user_id, created_at, expired_at)
VALUES
(DATE_ADD(CURDATE(), INTERVAL -1 DAY), 'VNPAY', '08:00:00', '09:00:00', 60, 120000.00, 'COMPLETED', 26, 2, DATE_ADD(CURDATE(), INTERVAL -2 DAY), DATE_ADD(DATE_ADD(CURDATE(), INTERVAL -2 DAY), INTERVAL 15 MINUTE)),
(DATE_ADD(CURDATE(), INTERVAL -3 DAY), 'PAYOS', '08:00:00', '09:00:00', 60, 120000.00, 'COMPLETED', 26, 2, DATE_ADD(CURDATE(), INTERVAL -4 DAY), DATE_ADD(DATE_ADD(CURDATE(), INTERVAL -4 DAY), INTERVAL 15 MINUTE)),
(DATE_ADD(CURDATE(), INTERVAL 6 DAY), 'MOMO', '18:00:00', '19:30:00', 90, 200000.00, 'PAID', 27, 2, DATE_ADD(CURDATE(), INTERVAL -2 DAY), DATE_ADD(DATE_ADD(CURDATE(), INTERVAL 5 DAY), INTERVAL 15 MINUTE)),
(DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'ZALOPAY', '19:00:00', '20:00:00', 60, 120000.00, 'PAID', 26, 2, DATE_ADD(CURDATE(), INTERVAL -1 DAY), DATE_ADD(DATE_ADD(CURDATE(), INTERVAL 5 DAY), INTERVAL 15 MINUTE)),

(DATE_ADD(CURDATE(), INTERVAL -2 DAY), 'PAYOS', '08:00:00', '09:00:00', 60, 120000.00, 'COMPLETED', 26, 4, DATE_ADD(CURDATE(), INTERVAL -3 DAY), DATE_ADD(DATE_ADD(CURDATE(), INTERVAL -3 DAY), INTERVAL 15 MINUTE)),
(DATE_ADD(CURDATE(), INTERVAL 5 DAY), 'VNPAY', '18:00:00', '19:30:00', 90, 180000.00, 'PENDING', 3, 4, DATE_SUB(NOW(), INTERVAL 3 MINUTE), DATE_SUB(NOW(), INTERVAL 15 MINUTE)),
(DATE_ADD(CURDATE(), INTERVAL 5 DAY), 'PAYOS', '09:00:00', '10:00:00', 60, 150000.00, 'CANCELLED', 5, 4, DATE_ADD(CURDATE(), INTERVAL 4 DAY), DATE_ADD(DATE_ADD(CURDATE(), INTERVAL 4 DAY), INTERVAL 15 MINUTE)),
(DATE_ADD(CURDATE(), INTERVAL 6 DAY), 'VNPAY', '17:30:00', '18:00:00', 90, 220000.00, 'PENDING', 27, 4, DATE_SUB(NOW(), INTERVAL 2 MINUTE), DATE_SUB(NOW(), INTERVAL 15 MINUTE));

INSERT INTO payment (booking_id, method, status, amount, transaction_code, payment_time, created_at)
VALUES
(1, 'VNPAY', 'SUCCESS', 120000.00, 'TXN001', DATE_ADD(CURDATE(), INTERVAL -1 DAY), DATE_ADD(CURDATE(), INTERVAL -2 DAY)),
(2, 'PAYOS', 'SUCCESS', 120000.00, 'TXN002', DATE_ADD(CURDATE(), INTERVAL -3 DAY), DATE_ADD(CURDATE(), INTERVAL -4 DAY)),
(3, 'MOMO', 'SUCCESS', 200000.00, 'TXN003', DATE_ADD(CURDATE(), INTERVAL 6 DAY), DATE_ADD(CURDATE(), INTERVAL 5 DAY)),
(4, 'ZALOPAY', 'SUCCESS', 120000.00, 'TXN004', DATE_ADD(CURDATE(), INTERVAL 6 DAY), DATE_ADD(CURDATE(), INTERVAL 5 DAY)),
(5, 'PAYOS', 'SUCCESS', 120000.00, 'TXN005', DATE_ADD(CURDATE(), INTERVAL -2 DAY), DATE_ADD(CURDATE(), INTERVAL -3 DAY)),
(6, 'MOMO', 'FAILED', 180000.00, 'TXN006', NULL, DATE_ADD(CURDATE(), INTERVAL 4 DAY)),
(7, 'PAYOS', 'SUCCESS', 150000.00, NULL, NULL, DATE_ADD(CURDATE(), INTERVAL 4 DAY)),
(8, 'VNPAY', 'FAILED', 220000.00, NULL, NULL, DATE_ADD(CURDATE(), INTERVAL 4 DAY));

INSERT INTO review (rating, comment, user_id, field_id, booking_id, create_at, updated_at)
VALUES
(5, 'Dịch vụ rất tốt, hài lòng!', 2, 26, 1, DATE_ADD(DATE_ADD(CURDATE(), INTERVAL -1 DAY), INTERVAL 1 HOUR), DATE_ADD(DATE_ADD(CURDATE(), INTERVAL -1 DAY), INTERVAL 1 HOUR)),
(5, 'Rất chuyên nghiệp và nhiệt tình.', 2, 26, 4, DATE_ADD(DATE_ADD(CURDATE(), INTERVAL 6 DAY), INTERVAL 1 HOUR), DATE_ADD(DATE_ADD(CURDATE(), INTERVAL 6 DAY), INTERVAL 1 HOUR)),
(4, 'Ok, sẽ quay lại lần sau.', 4, 26, 5, DATE_ADD(DATE_ADD(CURDATE(), INTERVAL -2 DAY), INTERVAL 1 HOUR), DATE_ADD(DATE_ADD(CURDATE(), INTERVAL -2 DAY), INTERVAL 1 HOUR));

INSERT INTO Wallet (balance, created_at, updated_at, user_id)
VALUES
(0, NOW(), NOW(), 1),
(2000000, NOW(), NOW(), 2),
(0, NOW(), NOW(), 3),
(500000, NOW(), NOW(), 4);

INSERT INTO challenge_match_type(name, max_players, field_type_id)
VALUES
("Đơn", 2, 6),
("Đôi nam", 4, 6),
("Đôi nữ", 4, 6),
("Đôi nam nữ", 4, 6);

INSERT INTO challenge_match
(creator_id, challenge_match_type_id, field_type_id, title, suggested_level, fee, note, status, booking_id, created_at, updated_at)
VALUES
(1, 1, 6,  'Thách đấu giao hữu sân 5', 'TRUNGBINH', 50000,   'Giao hữu vui vẻ, không phân biệt trình độ.', 'PENDING', 1, NOW(), NOW()),

(2, 2, 6,  'Giao hữu chiều thứ 4', 'KHA', 80000,  'Ưu tiên các bạn đá cánh nhanh nhẹn.', 'MATCHED', 2, NOW(), NOW()),

(3, 3, 1,  'Trận test sân mới', 'TRUNGBINH_KHA', 0,   'Test sân trước giải đấu nội bộ.', 'OPEN', 3, NOW(), NOW());

--(1, 3, 1,  'Trận test sân mới', 'TRUNGBINH', 0,   'Test sân trước giải đấu nội bộ.', 'OPEN', 3, NOW(), NOW());
--
--(3, 3, 1,  'Trận test sân mới', 'YEU', 0,   'Test sân trước giải đấu nội bộ.', 'OPEN', 3, NOW(), NOW());
--
--(4, 3, 1,  'Trận test sân mới', 'YEU', 0,   'Test sân trước giải đấu nội bộ.', 'OPEN', 3, NOW(), NOW());
--
--(4, 3, 1,  'Trận test sân mới', 'YEU', 0,   'Test sân trước giải đấu nội bộ.', 'OPEN', 3, NOW(), NOW());

INSERT INTO challenge_participant
(match_id, user_id, team, status, request_message, paid, created_at, updated_at)
VALUES
(1, 2, 'TEAM_A', 'PENDING', 'Cho mình tham gia nhé!', FALSE, NOW(), NOW()),
(1, 3, 'TEAM_A', 'ACCEPTED', 'Rất mong được đá chung!', TRUE, NOW(), NOW()),

(2, 2, 'TEAM_A', 'ACCEPTED', 'Cho mình tham gia trận này.', TRUE, NOW(), NOW()),
(2, 3, 'TEAM_B', 'ACCEPTED', 'Hi.', TRUE, NOW(), NOW());


INSERT INTO user_sport_elo(user_id, field_type_id, level, elo)
VALUES
(1, 1, 'TRUNGBINH_YEU', 980),
(1, 2, 'TRUNGBINH', 1180),
(1, 3, 'TRUNGBINH_KHA', 1370),
(1, 4, 'KHA', 1530),
(1, 5, 'KHA_GIOI', 1580),
(1, 6, 'TRUNGBINH', 1280);
(2, 1, "YEU", 868),
(2, 2, "TRUNGBINH_YEU", 1011),
(2, 3, "TRUNGBINH", 1211),
(2, 4, "TRUNGBINH_KHA", 1451),
(2, 5, "TRUNGBINH_KHA", 1500),
(2, 6, "TRUNGBINH", 1200),
(3, 1, 'TRUNGBINH_YEU', 950),
(3, 2, 'TRUNGBINH', 1150),
(3, 3, 'TRUNGBINH_KHA', 1350),
(3, 4, 'KHA', 1520),
(3, 5, 'KHA', 1550),
(3, 6, "TRUNGBINH", 1300),
(4, 1, 'YEU', 870),
(4, 2, 'TRUNGBINH_YEU', 1020),
(4, 3, 'TRUNGBINH', 1200),
(4, 4, 'TRUNGBINH_KHA', 1430),
(4, 5, 'KHA', 1510),
(4, 6, 'TRUNGBINH', 1250);




INSERT INTO challenge_result (match_id, team_a_scort, team_b_scort, created_at)
VALUES
--(2, 1, 2, NOW()),   -- Creator:
