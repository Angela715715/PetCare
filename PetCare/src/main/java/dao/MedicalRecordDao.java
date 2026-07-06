package dao;

import java.util.List;

import entity.MedicalRecord;

public interface MedicalRecordDao {

    // 新增健康紀錄
    void add(MedicalRecord record);

    // 用寵物 id 查詢健康紀錄
    List<MedicalRecord> selectByPetId(int petId);

    void delete(int id);
}
