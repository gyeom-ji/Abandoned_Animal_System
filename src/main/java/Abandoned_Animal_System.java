import DB.dao.*;
import network.Server;
import org.apache.ibatis.session.SqlSessionFactory;

public class Abandoned_Animal_System {
    private final Abandoned_noticeDAO abandoned_noticeDAO;
    private final AnimalDAO animalDAO;
    private final FormDAO formDAO;
    private final Missing_noticeDAO missing_noticeDAO;
    private final Recommend_materialsDAO recommend_materialsDAO;
    private final RollDAO rollDAO;
    private final Shelter_listDAO shelter_listDAO;
    private final VaccineDAO vaccineDAO;
    private final SqlSessionFactory sqlSessionFactory;
    private final Server mainServer;


    public Abandoned_Animal_System() {
        sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
        animalDAO = new AnimalDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        shelter_listDAO = new Shelter_listDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        abandoned_noticeDAO = new Abandoned_noticeDAO(animalDAO, shelter_listDAO, MyBatisConnectionFactory.getSqlSessionFactory());
        rollDAO = new RollDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        formDAO = new FormDAO(rollDAO, shelter_listDAO, abandoned_noticeDAO, MyBatisConnectionFactory.getSqlSessionFactory());
        missing_noticeDAO = new Missing_noticeDAO(animalDAO, MyBatisConnectionFactory.getSqlSessionFactory());
        recommend_materialsDAO = new Recommend_materialsDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        vaccineDAO = new VaccineDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        mainServer = new Server(animalDAO, shelter_listDAO, abandoned_noticeDAO,rollDAO,formDAO,missing_noticeDAO,
                recommend_materialsDAO,vaccineDAO);
    }

    public void run() {
        try {
            mainServer.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
