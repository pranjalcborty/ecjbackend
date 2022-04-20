import com.fasterxml.jackson.databind.ObjectMapper;
import db.models.Config;
import db.models.ConfigModel;
import db.models.Status;
import db.utils.JPAUtil;
import db.utils.Util;
import gp.Evolve;

import java.util.Objects;

public class Main {

    private static final long FIVE_MINUTES = 10000L;

    public static void main(String[] args) {
        JPAUtil dbUtil = new JPAUtil();

        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();

        while (true) {
            if (end - start >= FIVE_MINUTES) {
                start = System.currentTimeMillis();

                Config conf = dbUtil.getNextTask();

                if (Objects.nonNull(conf)) {
                    try {
                        ConfigModel config = new ObjectMapper().readValue(conf.getJsonData(), ConfigModel.class);

                        if (Objects.nonNull(config)) {
                            conf.setStatus(Status.PROCESSING);
                            dbUtil.update(conf);

                            String[] runConfig = new Util().getRunConfig(config, conf.getUuid());

                            Evolve.main(runConfig);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            end = System.currentTimeMillis();

            if (dbUtil.interruptBackendJob()) {
                break;
            }
        }
    }
}
