package entity;

import java.sql.Date;

public class MedicalRecord {

    private int id;
    private int petId;
    private String recordType;
    private String title;
    private Date recordDate;
    private String hospital;
    private String doctor;
    private String note;

    public MedicalRecord() {
    }

    public MedicalRecord(int id, int petId, String recordType, String title, Date recordDate,
            String hospital, String doctor, String note) {
        this.id = id;
        this.petId = petId;
        this.recordType = recordType;
        this.title = title;
        this.recordDate = recordDate;
        this.hospital = hospital;
        this.doctor = doctor;
        this.note = note;
    }

    public MedicalRecord(int petId, String recordType, String title, Date recordDate,
            String hospital, String doctor, String note) {
        this.petId = petId;
        this.recordType = recordType;
        this.title = title;
        this.recordDate = recordDate;
        this.hospital = hospital;
        this.doctor = doctor;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
