package db.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "result")
public class Result extends CommonParent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Result() {
    }

    public Result(String uuid, String jsonData) {
        this.uuid = uuid;
        this.jsonData = jsonData;
        this.status = Status.COMPLETED;
        this.uploadedOn = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Result)) return false;
        Result result = (Result) o;
        return getId() == result.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
