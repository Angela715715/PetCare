package entity;

import java.sql.Date;

public class Reminder {
    private int id;
    private int petId;
    private String title;
    private Date remindDate;
    private String remindTime;
    private String status;
    private String note;

    public Reminder() {}

    public Reminder(int petId, String title, Date remindDate, String remindTime, String status, String note) {
        this.petId = petId;
        this.title = title;
        this.remindDate = remindDate;
        this.remindTime = remindTime;
        this.status = status;
        this.note = note;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPetId() { return petId; }
    public void setPetId(int petId) { this.petId = petId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Date getRemindDate() { return remindDate; }
    public void setRemindDate(Date remindDate) { this.remindDate = remindDate; }
    public String getRemindTime() { return remindTime; }
    public void setRemindTime(String remindTime) { this.remindTime = remindTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
