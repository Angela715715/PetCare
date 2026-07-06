USE petcare;

CREATE TABLE IF NOT EXISTS medical_record (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pet_id INT NOT NULL,
    record_type VARCHAR(50) NOT NULL,
    title VARCHAR(100) NOT NULL,
    record_date DATE NOT NULL,
    hospital VARCHAR(100),
    doctor VARCHAR(50),
    note VARCHAR(255),
    FOREIGN KEY (pet_id) REFERENCES pet(id)
);

-- 測試資料：請先確認 pet 表裡有 id = 1 的寵物
-- INSERT INTO medical_record(pet_id, record_type, title, record_date, hospital, doctor, note)
-- VALUES
-- (1, '疫苗', '狂犬病疫苗', '2024-04-10', '幸福動物醫院', '王醫師', '一年一次疫苗'),
-- (1, '看診', '腸胃不適', '2024-05-02', '幸福動物醫院', '陳醫師', '食慾不佳，開藥觀察');
