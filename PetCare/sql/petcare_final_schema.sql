-- PetCare 完整 MySQL 建表與測試資料
-- 適用 MySQL 8.x
-- 執行方式：MySQL Workbench 開啟後整份執行

DROP DATABASE IF EXISTS petcare;
CREATE DATABASE petcare DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE petcare;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS medical_file;
DROP TABLE IF EXISTS pet_photo;
DROP TABLE IF EXISTS reminder;
DROP TABLE IF EXISTS weight_record;
DROP TABLE IF EXISTS vaccine_record;
DROP TABLE IF EXISTS medical_record;
DROP TABLE IF EXISTS pet;
DROP TABLE IF EXISTS member;

SET FOREIGN_KEY_CHECKS = 1;

-- 會員資料
CREATE TABLE member (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 寵物資料
CREATE TABLE pet (
    id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    species VARCHAR(30),
    breed VARCHAR(50),
    gender VARCHAR(10),
    birthday DATE,
    note VARCHAR(255),
    photo_path VARCHAR(255),
    CONSTRAINT fk_pet_member
        FOREIGN KEY (member_id) REFERENCES member(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 看診 / 健康紀錄
CREATE TABLE medical_record (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pet_id INT NOT NULL,
    record_type VARCHAR(50) NOT NULL,
    title VARCHAR(100) NOT NULL,
    record_date DATE,
    hospital VARCHAR(100),
    doctor VARCHAR(50),
    note VARCHAR(255),
    CONSTRAINT fk_medical_record_pet
        FOREIGN KEY (pet_id) REFERENCES pet(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 疫苗紀錄
CREATE TABLE vaccine_record (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pet_id INT NOT NULL,
    vaccine_name VARCHAR(100) NOT NULL,
    vaccine_date DATE,
    next_date DATE,
    hospital VARCHAR(100),
    note VARCHAR(255),
    CONSTRAINT fk_vaccine_record_pet
        FOREIGN KEY (pet_id) REFERENCES pet(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 體重紀錄
CREATE TABLE weight_record (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pet_id INT NOT NULL,
    weight_date DATE,
    weight_kg DECIMAL(5,2) NOT NULL,
    note VARCHAR(255),
    CONSTRAINT fk_weight_record_pet
        FOREIGN KEY (pet_id) REFERENCES pet(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 提醒事項 / 預約管理
CREATE TABLE reminder (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pet_id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    remind_date DATE,
    remind_time VARCHAR(20),
    status VARCHAR(20) DEFAULT '未完成',
    note VARCHAR(255),
    CONSTRAINT fk_reminder_pet
        FOREIGN KEY (pet_id) REFERENCES pet(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 成長相簿
CREATE TABLE pet_photo (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pet_id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    photo_path VARCHAR(255) NOT NULL,
    photo_date DATE,
    note VARCHAR(255),
    CONSTRAINT fk_pet_photo_pet
        FOREIGN KEY (pet_id) REFERENCES pet(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 醫療文件
CREATE TABLE medical_file (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pet_id INT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    file_type VARCHAR(80),
    upload_date DATE,
    note VARCHAR(255),
    CONSTRAINT fk_medical_file_pet
        FOREIGN KEY (pet_id) REFERENCES pet(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 測試帳號
INSERT INTO member(id, username, password, name, role) VALUES
(1, 'admin', '1234', '系統管理員', 'ADMIN'),
(2, 'user01', '1234', 'Angela', 'USER'),
(3, 'user02', '1234', 'Amy', 'USER');

-- 測試寵物
-- photo_path 可依你自己的圖片檔名修改，例如 uploads/pet_photos/妹妹.JPG
INSERT INTO pet(id, member_id, name, species, breed, gender, birthday, note, photo_path) VALUES
(1, 2, '妹妹', '狗', '馬爾濟斯', '母', '2024-07-15', '個性親人，喜歡散步', 'uploads/pet_photos/妹妹.JPG'),
(2, 2, 'Momo', '狗', '馬爾濟斯', '母', '2020-03-15', '容易緊張，回診時要安撫', ''),
(3, 3, 'Lulu', '貓', '米克斯', '母', '2021-06-20', '需要定期追蹤體重', '');

-- 看診 / 健康紀錄測試資料
INSERT INTO medical_record(pet_id, record_type, title, record_date, hospital, doctor, note) VALUES
(1, '看診', '皮膚搔癢', '2026-06-18', '幸福動物醫院', '王醫師', '開藥觀察一週'),
(1, '健康檢查', '年度健康檢查', '2026-05-10', '幸福動物醫院', '陳醫師', '狀況良好'),
(2, '看診', '腸胃不適', '2026-04-20', '毛孩動物醫院', '林醫師', '飲食需清淡');

-- 疫苗紀錄測試資料
INSERT INTO vaccine_record(pet_id, vaccine_name, vaccine_date, next_date, hospital, note) VALUES
(1, '狂犬病疫苗', '2026-03-10', '2027-03-10', '幸福動物醫院', '一年一次'),
(1, '八合一疫苗', '2026-04-12', '2027-04-12', '幸福動物醫院', '無不良反應');

-- 體重紀錄測試資料：提供折線圖與體重分析使用
INSERT INTO weight_record(pet_id, weight_date, weight_kg, note) VALUES
(1, '2026-01-01', 3.05, '第一次紀錄'),
(1, '2026-02-01', 3.12, '正常增加'),
(1, '2026-03-01', 3.20, '穩定增加'),
(1, '2026-04-01', 3.20, '體重正常'),
(1, '2026-05-01', 3.35, '略增，持續觀察'),
(1, '2026-06-01', 3.30, '穩定'),
(1, '2026-07-01', 3.42, '食慾良好'),
(2, '2026-04-05', 4.60, '第一次紀錄'),
(2, '2026-05-05', 4.55, '稍微下降'),
(2, '2026-06-05', 4.70, '恢復正常');

-- 提醒事項測試資料
INSERT INTO reminder(pet_id, title, remind_date, remind_time, status, note) VALUES
(1, '下次回診', '2026-07-20', '10:00', '未完成', '皮膚狀況追蹤'),
(1, '疫苗提醒', '2027-03-10', '09:00', '未完成', '狂犬病疫苗');

-- 成長相簿測試資料
INSERT INTO pet_photo(pet_id, title, photo_path, photo_date, note) VALUES
(1, '第一張照片', 'uploads/pet_photos/妹妹.JPG', '2026-06-01', '剛加入 PetCare 的照片');

-- 醫療文件測試資料
INSERT INTO medical_file(pet_id, file_name, file_path, file_type, upload_date, note) VALUES
(1, '健康檢查報告.pdf', 'uploads/medical_files/checkup_report.pdf', 'PDF', '2026-05-10', '年度健康檢查報告');

-- 驗證用查詢
SELECT * FROM member;
SELECT * FROM pet;
SELECT * FROM medical_record;
SELECT * FROM vaccine_record;
SELECT * FROM weight_record;
SELECT * FROM reminder;
SELECT * FROM pet_photo;
SELECT * FROM medical_file;
