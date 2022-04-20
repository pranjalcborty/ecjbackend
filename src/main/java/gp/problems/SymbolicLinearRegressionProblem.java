package gp.problems;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import db.models.*;
import db.utils.JPAUtil;
import ec.EvolutionState;
import ec.Individual;
import ec.app.tutorial4.DoubleData;
import ec.gp.GPIndividual;
import ec.gp.GPProblem;
import ec.simple.SimpleFitness;
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

import static java.lang.Math.abs;

public class SymbolicLinearRegressionProblem extends GPProblem implements SimpleProblemForm {

    private static final int SUPPORTED_COLUMNS = 30;

    public double currentX1, currentX2, currentX3, currentX4, currentX5,
            currentX6, currentX7, currentX8, currentX9, currentX10,
            currentX11, currentX12, currentX13, currentX14, currentX15,
            currentX16, currentX17, currentX18, currentX19, currentX20,
            currentX21, currentX22, currentX23, currentX24, currentX25,
            currentX26, currentX27, currentX28, currentX29, currentX30;

    public List<double[]> inputX = new ArrayList<>();
    public List<Double> inputY = new ArrayList<>();

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
    public void evaluate(final EvolutionState evolutionState, final Individual individual,
                         final int subPopulation, final int threadNum) {

        JPAUtil jpaUtil = new JPAUtil();
        if (jpaUtil.interruptBackendJob()) {
            failTask(jpaUtil);
            return;
        }

        if (!individual.evaluated) {
            DoubleData input = (DoubleData) (this.input);
            GPIndividual ind = (GPIndividual) individual;

            int hits = 0;
            double sum = 0;
            double expectedResult, result;

            for (int i = 0; i < inputX.size(); i++) {
                double[] currentX = inputX.get(i);

                currentX1 = currentX[0];
                currentX2 = currentX[1];
                currentX3 = currentX[2];
                currentX4 = currentX[3];
                currentX5 = currentX[4];
                currentX6 = currentX[5];
                currentX7 = currentX[6];
                currentX8 = currentX[7];
                currentX9 = currentX[8];
                currentX10 = currentX[9];

                currentX11 = currentX[10];
                currentX12 = currentX[11];
                currentX13 = currentX[12];
                currentX14 = currentX[13];
                currentX15 = currentX[14];
                currentX16 = currentX[15];
                currentX17 = currentX[16];
                currentX18 = currentX[17];
                currentX19 = currentX[18];
                currentX20 = currentX[19];

                currentX21 = currentX[20];
                currentX22 = currentX[21];
                currentX23 = currentX[22];
                currentX24 = currentX[23];
                currentX25 = currentX[24];
                currentX26 = currentX[25];
                currentX27 = currentX[26];
                currentX28 = currentX[27];
                currentX29 = currentX[28];
                currentX30 = currentX[29];

                expectedResult = inputY.get(i);

                ind.trees[0].child.eval(evolutionState, threadNum, input, stack, ind, this);

                result = abs(expectedResult - input.x);
                if (result <= 0.001) {
                    hits++;
                }

                sum += result;
            }

            SimpleFitness f = ((SimpleFitness) ind.fitness);
            f.setFitness(evolutionState, sum, (hits == inputX.size()));

            ind.evaluated = true;
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

                            try {
                                BufferedReader br = new BufferedReader(new FileReader(filePath.toString()));
                                List<Double> fitness = new ArrayList<>();

                                for (String s = br.readLine(); s != null; s = br.readLine()) {
                                    if (s.startsWith("Fitness: ")) {
                                        fitness.add(Double.parseDouble(s.split("\\s")[1]));
                                    }
                                }

                                resultModel.getBestIndividualFitnessMap()
                                        .put(runId, fitness.remove(fitness.size() - 1));

                                resultModel.getAllRunInfoMap().put(runId, fitness);
                            } catch (IOException e) {
                                e.printStackTrace();
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

    private void loadData(EvolutionState state) throws JsonProcessingException {
        JPAUtil jpaUtil = new JPAUtil();
        Config config = jpaUtil.getCurrentTask();
        Dataset dataset = jpaUtil.getDatasetByUUID(config.getUuid());

        ObjectMapper objectMapper = new ObjectMapper();
        DatasetModel datasetModel = objectMapper.readValue(dataset.getJsonData(), DatasetModel.class);

        int totalColumns = datasetModel.getColumns().size();

        for (List<Double> row : datasetModel.getX()) {
            double[] dataRow = new double[SUPPORTED_COLUMNS];

            for (int i = 0; i < totalColumns - 1; i++) {
                dataRow[i] = row.get(i);
            }

            inputX.add(dataRow);
        }

        inputY = datasetModel.getY();
    }

    private void failTask(JPAUtil jpaUtil) {
        Config config = jpaUtil.getCurrentTask();
        Dataset dataset = jpaUtil.getDatasetByUUID(config.getUuid());

        config.setStatus(Status.FAILED);
        dataset.setStatus(Status.FAILED);
        jpaUtil.update(config);
        jpaUtil.update(dataset);
    }
}
