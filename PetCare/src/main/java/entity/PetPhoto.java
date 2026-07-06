package entity;

import java.sql.Date;

public class PetPhoto {
    private int id;
    private int petId;
    private String title;
    private String photoPath;
    private Date photoDate;
    private String note;

    public PetPhoto() {}

    public PetPhoto(int petId, String title, String photoPath, Date photoDate, String note) {
        this.petId = petId;
        this.title = title;
        this.photoPath = photoPath;
        this.photoDate = photoDate;
        this.note = note;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPetId() { return petId; }
    public void setPetId(int petId) { this.petId = petId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
    public Date getPhotoDate() { return photoDate; }
    public void setPhotoDate(Date photoDate) { this.photoDate = photoDate; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
