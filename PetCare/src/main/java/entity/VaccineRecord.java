package entity;

import java.sql.Date;

public class VaccineRecord {
    private int id;
    private int petId;
    private String vaccineName;
    private Date vaccineDate;
    private Date nextDate;
    private String hospital;
    private String note;

    public VaccineRecord() {}

    public VaccineRecord(int petId, String vaccineName, Date vaccineDate, Date nextDate, String hospital, String note) {
        this.petId = petId;
        this.vaccineName = vaccineName;
        this.vaccineDate = vaccineDate;
        this.nextDate = nextDate;
        this.hospital = hospital;
        this.note = note;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPetId() { return petId; }
    public void setPetId(int petId) { this.petId = petId; }
    public String getVaccineName() { return vaccineName; }
    public void setVaccineName(String vaccineName) { this.vaccineName = vaccineName; }
    public Date getVaccineDate() { return vaccineDate; }
    public void setVaccineDate(Date vaccineDate) { this.vaccineDate = vaccineDate; }
    public Date getNextDate() { return nextDate; }
    public void setNextDate(Date nextDate) { this.nextDate = nextDate; }
    public String getHospital() { return hospital; }
    public void setHospital(String hospital) { this.hospital = hospital; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
