package service.impl;

import java.util.List;

import dao.MedicalRecordDao;
import dao.impl.MedicalRecordDaoImpl;
import entity.MedicalRecord;
import service.MedicalRecordService;

public class MedicalRecordServiceImpl implements MedicalRecordService {

    private MedicalRecordDao medicalRecordDao = new MedicalRecordDaoImpl();

    @Override
    public void addRecord(MedicalRecord record) {
        medicalRecordDao.add(record);
    }

    @Override
    public List<MedicalRecord> findByPetId(int petId) {
        return medicalRecordDao.selectByPetId(petId);
    }

    @Override
    public void deleteRecord(int id) {
        medicalRecordDao.delete(id);
    }
}
