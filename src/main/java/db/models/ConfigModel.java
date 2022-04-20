package db.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Integer> functionChoices;
    private Map<Integer, String> paramChoices;

    public ConfigModel() {
        functionChoices = new ArrayList<>();
        paramChoices = new HashMap<>();
    }

    public List<Integer> getFunctionChoices() {
        return functionChoices;
    }

    public void setFunctionChoices(List<Integer> functionChoices) {
        this.functionChoices = functionChoices;
    }

    public Map<Integer, String> getParamChoices() {
        return paramChoices;
    }

    public void setParamChoices(Map<Integer, String> paramChoices) {
        this.paramChoices = paramChoices;
    }
}
