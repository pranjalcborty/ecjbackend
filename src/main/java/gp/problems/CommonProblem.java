package gp.problems;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import db.models.*;
import db.utils.JPAUtil;
import ec.EvolutionState;
import ec.Individual;
import ec.app.tutorial4.DoubleData;
import ec.gp.GPProblem;
import ec.simple.SimpleProblemForm;
import ec.util.Parameter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class CommonProblem extends GPProblem implements SimpleProblemForm {

    public static final int SUPPORTED_COLUMNS = 30;

    public double currentX1, currentX2, currentX3, currentX4, currentX5,
            currentX6, currentX7, currentX8, currentX9, currentX10,
            currentX11, currentX12, currentX13, currentX14, currentX15,
            currentX16, currentX17, currentX18, currentX19, currentX20,
            currentX21, currentX22, currentX23, currentX24, currentX25,
            currentX26, currentX27, currentX28, currentX29, currentX30;

    protected abstract void loadData(EvolutionState state) throws JsonProcessingException;

    @Override
    public void setup(EvolutionState state, Parameter base) {
        super.setup(state, base);

        if (!(input instanceof DoubleData)) {
            state.output.fatal("GPData class must subclass from " + DoubleData.class,
                    base.push(P_DATA), null);
        }

        try {
            loadData(state);
        } catch (Exception e) {
            JPAUtil jpaUtil = new JPAUtil();
            failTask(jpaUtil);
            e.printStackTrace();
        }
    }

    @Override
    public void describe(EvolutionState evolutionState, Individual individual,
                         int subPopulation, int threadNum, int log) {

        int totalJobs = evolutionState.parameters.getInt(new Parameter("jobs"), null);

        if ((int) evolutionState.job[0] == (totalJobs - 1)) {
            ResultModel resultModel = new ResultModel();

            try (Stream<Path> filesWalk = Files.walk(Path.of("./results"))) {

                filesWalk.filter(filePath -> filePath.toString().endsWith(".stat"))
                        .forEach(filePath -> {
                            String runId = filePath.toString().split("\\.")[2];

                            if (Integer.parseInt(runId) < totalJobs) {
                                try {
                                    BufferedReader br = new BufferedReader(new FileReader(filePath.toString()));
                                    List<Double> fitness = new ArrayList<>();

                                    StringBuilder bestTree = new StringBuilder();

                                    for (String s = br.readLine(); s != null; s = br.readLine()) {
                                        if (s.startsWith("Fitness: ")) {
                                            fitness.add(Double.parseDouble(s.split("\\s")[1]));
                                        }

                                        if (s.startsWith("Tree 0:")) {
                                            bestTree = new StringBuilder();
                                        } else {
                                            bestTree.append(s);
                                        }
                                    }

                                    resultModel.getBestIndividualFitnessMap()
                                            .put(runId, fitness.remove(fitness.size() - 1));

                                    resultModel.getAllRunInfoMap().put(runId, fitness);
                                    resultModel.getBestTreeMap().put(runId, bestTree.toString());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String jsonData = new ObjectMapper().writeValueAsString(resultModel);
                JPAUtil jpaUtil = new JPAUtil();
                Config config = jpaUtil.getCurrentTask();

                Result result = new Result(config.getUuid(), jsonData);
                jpaUtil.save(result);

                config.setStatus(Status.COMPLETED);
                jpaUtil.update(config);

                Dataset dataset = jpaUtil.getDatasetByUUID(config.getUuid());
                dataset.setStatus(Status.COMPLETED);
                jpaUtil.update(config);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    protected void failTask(JPAUtil jpaUtil) {
        Config config = jpaUtil.getCurrentTask();
        Dataset dataset = jpaUtil.getDatasetByUUID(config.getUuid());

        config.setStatus(Status.FAILED);
        dataset.setStatus(Status.FAILED);
        jpaUtil.update(config);
        jpaUtil.update(dataset);
    }
}
