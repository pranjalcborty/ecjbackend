package gp.problems;

import com.fasterxml.jackson.core.JsonProcessingException;
import db.models.Config;
import db.models.Dataset;
import db.models.Status;
import db.utils.JPAUtil;
import ec.EvolutionState;
import ec.app.tutorial4.DoubleData;
import ec.gp.GPProblem;
import ec.simple.SimpleProblemForm;
import ec.util.Parameter;

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

    protected void failTask(JPAUtil jpaUtil) {
        Config config = jpaUtil.getCurrentTask();
        Dataset dataset = jpaUtil.getDatasetByUUID(config.getUuid());

        config.setStatus(Status.FAILED);
        dataset.setStatus(Status.FAILED);
        jpaUtil.update(config);
        jpaUtil.update(dataset);
    }
}
