import db.models.Config;
import db.models.Status;
import db.utils.JPAUtil;

public class Main {
    public static void main(String[] args) {
        Config config = new Config();
        config.setUuid("uwo");
        config.setJsonData("uwoojoajdfsa");
        config.setStatus(Status.PENDING);

        JPAUtil dbUtil = new JPAUtil();
        dbUtil.save(config);

        String uuid = dbUtil.getNextTask().getUuid();
        System.out.println("UUID: " + uuid);
    }
}
