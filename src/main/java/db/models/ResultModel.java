package db.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, List<Double>> allRunInfoMap;
    private Map<String, Double> bestIndividualFitnessMap;
    private Map<String, String> bestIndividualMap;
    private Map<String, String> bestTreeMap;

    public ResultModel() {
        this.allRunInfoMap = new HashMap<>();
        this.bestIndividualFitnessMap = new HashMap<>();
        this.bestIndividualMap = new HashMap<>();
        this.bestTreeMap = new HashMap<>();
    }

    public Map<String, List<Double>> getAllRunInfoMap() {
        return allRunInfoMap;
    }

    public void setAllRunInfoMap(Map<String, List<Double>> allRunInfoMap) {
        this.allRunInfoMap = allRunInfoMap;
    }

    public Map<String, Double> getBestIndividualFitnessMap() {
        return bestIndividualFitnessMap;
    }

    public void setBestIndividualFitnessMap(Map<String, Double> bestIndividualFitnessMap) {
        this.bestIndividualFitnessMap = bestIndividualFitnessMap;
    }

    public Map<String, String> getBestIndividualMap() {
        return bestIndividualMap;
    }

    public void setBestIndividualMap(Map<String, String> bestIndividualMap) {
        this.bestIndividualMap = bestIndividualMap;
    }

    public Map<String, String> getBestTreeMap() {
        return bestTreeMap;
    }

    public void setBestTreeMap(Map<String, String> bestTreeMap) {
        this.bestTreeMap = bestTreeMap;
    }
}
