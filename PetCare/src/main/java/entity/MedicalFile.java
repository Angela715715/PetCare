package entity;

import java.sql.Date;

public class MedicalFile {
    private int id;
    private int petId;
    private String fileName;
    private String filePath;
    private String fileType;
    private Date uploadDate;
    private String note;

    public MedicalFile() {}

    public MedicalFile(int petId, String fileName, String filePath, String fileType, Date uploadDate, String note) {
        this.petId = petId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.uploadDate = uploadDate;
        this.note = note;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPetId() { return petId; }
    public void setPetId(int petId) { this.petId = petId; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public Date getUploadDate() { return uploadDate; }
    public void setUploadDate(Date uploadDate) { this.uploadDate = uploadDate; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
