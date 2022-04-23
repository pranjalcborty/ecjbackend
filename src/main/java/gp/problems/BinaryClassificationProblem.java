package gp.problems;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import db.models.Config;
import db.models.Dataset;
import db.models.DatasetModel;
import db.utils.JPAUtil;
import ec.EvolutionState;
import ec.Individual;
import ec.app.tutorial4.DoubleData;
import ec.gp.GPIndividual;
import ec.simple.SimpleFitness;

import java.util.ArrayList;
import java.util.List;

public class BinaryClassificationProblem extends CommonProblem {

    public List<double[]> inputX = new ArrayList<>();
    public List<String> inputY = new ArrayList<>();
    public String[] uniqueVals;

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
            String expectedResult, result;

            /*
             * tp = True Positive
             * tn = True Negative
             * fp = False Positive
             * fn = False Negative
             * */
            int tp = 0, tn = 0, fp = 0, fn = 0;

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
                result = input.x > 0 ? uniqueVals[0] : uniqueVals[1];

                if (expectedResult.equals(result)) {
                    hits++;
                }

                if (expectedResult.equals(uniqueVals[0])) {
                    if (result.equals(uniqueVals[0])) {
                        tp++;
                    } else {
                        fn++;
                    }
                } else {
                    if (result.equals(uniqueVals[0])) {
                        fp++;
                    } else {
                        tn++;
                    }
                }
            }

            double fitness = hits / (double) inputX.size();

            SimpleFitness f = ((SimpleFitness) ind.fitness);
            f.setFitness(evolutionState, fitness, (hits == inputX.size()));

            ind.evaluated = true;
        }
    }

    @Override
    protected void loadData(EvolutionState state) throws JsonProcessingException {
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

        uniqueVals = inputY.stream().distinct().toArray(String[]::new);

        if (uniqueVals.length != 2) {
            throw new IllegalStateException();
        }
    }
}
