package DB.dao;

import DB.dto.Abandoned_noticeDTO;
import DB.dto.Shelter_listDTO;
import DB.mapper.MyBatisConnectionFactory;
import DB.repository.Abandoned_noticeRepository;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import java.util.List;

public class Abandoned_noticeDAO implements Abandoned_noticeRepository{
    private SqlSessionFactory sqlSessionFactory = null;
    private AnimalDAO animalDAO;
    private Shelter_listDAO shelter_listDAO;

    public Abandoned_noticeDAO(AnimalDAO animalDAO, Shelter_listDAO shelter_listDAO, SqlSessionFactory sqlSessionFactory) {
        this.animalDAO = animalDAO;
        this.shelter_listDAO = shelter_listDAO;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<Abandoned_noticeDTO> ReadAll()
    {
        List<Abandoned_noticeDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            list = session.selectList("mapper.Abandoned_noticeMapper.ReadAll");
        } finally {
            session.close();
        }
        return list;
    }

    public List<Abandoned_noticeDTO> FindByOption(final String shelter_county, final String shelter_city)
    {
        Shelter_listDTO shelter_listDTO = new Shelter_listDTO();
        shelter_listDTO.setShelter_county(shelter_county);
        shelter_listDTO.setShelter_city(shelter_city);
        List<Abandoned_noticeDTO> list = null;

        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("mapper.Abandoned_noticeMapper.FindByOption", shelter_listDTO);
        } finally {
            session.close();
        }
        return list;
    }

    public void UpdateAbandoned(Abandoned_noticeDTO abandoned_noticeDTO)
    {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            session.update("mapper.Abandoned_noticeMapper.UpdateAbandoned", abandoned_noticeDTO);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void InsertAbandoned(Abandoned_noticeDTO abandoned_noticeDTO)
    {
        SqlSession session = null;
        long animal_pk = 0, shelter_pk = 0;
        AnimalDAO animalDAO = new AnimalDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        animal_pk = animalDAO.InsertAnimal(abandoned_noticeDTO.getAnimalDTO());
        String name = abandoned_noticeDTO.getShelter_listDTO().getShelter_name();
        String phone = abandoned_noticeDTO.getShelter_listDTO().getShelter_phone();
        Shelter_listDTO shelter_listDTO = shelter_listDAO.select_abandoned(name, phone);
        abandoned_noticeDTO.getShelter_listDTO().setShelter_list_pk(shelter_listDTO.getShelter_list_pk());
        try {
            abandoned_noticeDTO.setAbandoned_animal_pk(animal_pk);
            session = sqlSessionFactory.openSession(true);
            session.insert("mapper.Abandoned_noticeMapper.InsertAbandoned", abandoned_noticeDTO);

            session.commit();
        } finally {
            session.close();
        }
    }

    public void RemoveAbandoned(String abandoned_notice_num) {
        SqlSession session = null;
        Abandoned_noticeDTO abandoned_noticeDTO = FindByAbandoned_notice_num(abandoned_notice_num);
        long animal_pk = abandoned_noticeDTO.getAnimalDTO().getAnimal_pk();

        try {
            session = sqlSessionFactory.openSession(true);
            session.delete("mapper.Abandoned_noticeMapper.RemoveAbandoned", abandoned_notice_num);
            session.commit();
        } finally {
            session.close();
        }
        animalDAO.RemoveAnimal(animal_pk);
    }

    public Abandoned_noticeDTO FindByAbandoned_notice_num(String abandoned_notice_num){
        Abandoned_noticeDTO abandoned_noticeDTO = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            abandoned_noticeDTO = session.selectOne("mapper.Abandoned_noticeMapper.FindByAbandoned_notice_num", abandoned_notice_num);
        }
        finally{
            session.close();
        }
        return abandoned_noticeDTO;
    }

    public Abandoned_noticeDTO FindByID(long abandoned_notice_pk){
        Abandoned_noticeDTO abandoned_noticeDTO = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            abandoned_noticeDTO = session.selectOne("mapper.Abandoned_noticeMapper.FindByID", abandoned_notice_pk);
        }
        finally{
            session.close();
        }
        return abandoned_noticeDTO;
    }

}
