package service;

import java.util.List;

import entity.MedicalRecord;

public interface MedicalRecordService {

    // 新增健康紀錄
    void addRecord(MedicalRecord record);

    // 查詢某一隻寵物的健康紀錄
    List<MedicalRecord> findByPetId(int petId);

    void deleteRecord(int id);
}
