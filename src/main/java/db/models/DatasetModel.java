package db.models;

import java.io.Serializable;
import java.util.List;

public class DatasetModel implements Serializable {

    private static final long serialVersionUID = 1L;

    List<String> columns;
    List<List<Double>> X;
    List<Double> Y;

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<List<Double>> getX() {
        return X;
    }

    public void setX(List<List<Double>> x) {
        X = x;
    }

    public List<Double> getY() {
        return Y;
    }

    public void setY(List<Double> y) {
        Y = y;
    }
}
