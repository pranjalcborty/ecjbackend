package db.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import db.models.ConfigModel;
import db.models.DatasetModel;
import ec.Evolve;
import ec.gp.GPNode;
import gp.functions.*;

import java.util.*;

import static java.lang.String.format;

public class Util {

    public static Map<Integer, GPNode> FUNCTION_CHOICES = new HashMap<>();
    public static Map<Integer, String> GP_PARAM_CHOICES = new HashMap<>();

    static {
        FUNCTION_CHOICES.put(0, new Add());
        FUNCTION_CHOICES.put(1, new Sub());
        FUNCTION_CHOICES.put(2, new Mul());
        FUNCTION_CHOICES.put(3, new Div());
        FUNCTION_CHOICES.put(4, new Sin());
        FUNCTION_CHOICES.put(5, new Cos());
        FUNCTION_CHOICES.put(6, new Max());
        FUNCTION_CHOICES.put(7, new Min());
        FUNCTION_CHOICES = Collections.unmodifiableMap(FUNCTION_CHOICES);

        GP_PARAM_CHOICES.put(0, "pop.subpop.0.size");
        GP_PARAM_CHOICES.put(1, "pop.subpop.0.species.pipe.source.0.prob");
        GP_PARAM_CHOICES.put(2, "pop.subpop.0.species.pipe.source.1.prob");
        GP_PARAM_CHOICES.put(3, "generations");
        GP_PARAM_CHOICES.put(4, "select.tournament.size");
        GP_PARAM_CHOICES.put(5, "breed.elite.0");
        GP_PARAM_CHOICES.put(6, "jobs");
        GP_PARAM_CHOICES = Collections.unmodifiableMap(GP_PARAM_CHOICES);
    }

    public String[] getRunConfig(ConfigModel config, String uuid) throws JsonProcessingException {
        List<String> runConfig = new ArrayList<>();
        int functionCount = 0;

        runConfig.add(Evolve.A_FILE);
        runConfig.add("src/main/java/gp/params/default.params");

        for (int functionChoice : config.getFunctionChoices()) {
            runConfig.add("-p");
            runConfig.add(format("gp.fs.0.func.%d=%s",
                    functionCount, FUNCTION_CHOICES.get(functionChoice).getClass().getName()));

            runConfig.add("-p");
            runConfig.add(format("gp.fs.0.func.%d.nc=nc%d",
                    functionCount, FUNCTION_CHOICES.get(functionChoice).expectedChildren()));

            functionCount++;
        }

        JPAUtil jpaUtil = new JPAUtil();
        DatasetModel datasetModel = new ObjectMapper().readValue(jpaUtil.getDatasetByUUID(uuid).getJsonData(), DatasetModel.class);

        for (int i = 1; i < datasetModel.getColumns().size(); i++) {
            runConfig.add("-p");
            runConfig.add(format("gp.fs.0.func.%d=gp.nodes.X%d", functionCount, i));

            runConfig.add("-p");
            runConfig.add(format("gp.fs.0.func.%d.nc=nc0", functionCount));

            functionCount++;
        }

        runConfig.add("-p");
        runConfig.add(format("gp.fs.0.size=%d", functionCount));

        for (Map.Entry<Integer, String> entry : config.getParamChoices().entrySet()) {
            if (!entry.getValue().isEmpty()) {
                runConfig.add("-p");
                runConfig.add(format("%s=%s", GP_PARAM_CHOICES.get(entry.getKey()), entry.getValue()));
            }
        }

        runConfig.add("-p");
        runConfig.add("eval.problem=gp.problems.SymbolicLinearRegressionProblem");

        return runConfig.toArray(new String[0]);
    }
}
