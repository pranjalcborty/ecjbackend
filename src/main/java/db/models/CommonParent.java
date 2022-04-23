package db.models;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public abstract class CommonParent {

    @Column(unique = true)
    protected String uuid;

    @Lob
    @Column(name = "json_data")
    protected String jsonData;
    protected Status status;

    @Column(name = "uploaded_on")
    protected Date uploadedOn;

    public abstract int getId();

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String json_data) {
        this.jsonData = json_data;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(Date uploadedOn) {
        this.uploadedOn = uploadedOn;
    }
}
