package entity;

import java.sql.Date;

public class WeightRecord {
    private int id;
    private int petId;
    private Date weightDate;
    private double weightKg;
    private String note;

    public WeightRecord() {}

    public WeightRecord(int petId, Date weightDate, double weightKg, String note) {
        this.petId = petId;
        this.weightDate = weightDate;
        this.weightKg = weightKg;
        this.note = note;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPetId() { return petId; }
    public void setPetId(int petId) { this.petId = petId; }
    public Date getWeightDate() { return weightDate; }
    public void setWeightDate(Date weightDate) { this.weightDate = weightDate; }
    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
