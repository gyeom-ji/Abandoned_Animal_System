package DB.dao;

import DB.dto.Missing_noticeDTO;
import DB.mapper.MyBatisConnectionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class Missing_noticeDAO {
    private SqlSessionFactory sqlSessionFactory = null;
    private AnimalDAO animalDAO = new AnimalDAO(MyBatisConnectionFactory.getSqlSessionFactory());

    public Missing_noticeDAO(AnimalDAO animalDAO, SqlSessionFactory sqlSessionFactory) {
        this.animalDAO = animalDAO;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<Missing_noticeDTO> ReadAll() {
        List<Missing_noticeDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("mapper.Missing_noticeMapper.ReadAll");
        } finally {
            session.close();
        }
        return list;
    }

    public List<Missing_noticeDTO> FindByOption(Missing_noticeDTO missing_noticeDTO) {

        List<Missing_noticeDTO> list = null;

        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("mapper.Missing_noticeMapper.FindByOption", missing_noticeDTO);
        } finally {
            session.close();
        }
        return list;
    }

    public Missing_noticeDTO FindByPk(long pk) {
        Missing_noticeDTO missing = null;

        SqlSession session = sqlSessionFactory.openSession();
        try {
            missing = session.selectOne("mapper.Missing_noticeMapper.FindByPk", pk);
        } finally {
            session.close();
        }
        return missing;
    }

    public void UpdateMissing(Missing_noticeDTO missing_noticeDTO) {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            session.update("mapper.Missing_noticeMapper.UpdateMissing", missing_noticeDTO);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void InsertMissing(Missing_noticeDTO missing_noticeDTO) {
        SqlSession session = null;
        long animal_pk = 0;
        animal_pk = animalDAO.InsertAnimal(missing_noticeDTO.getAnimalDTOList().get(0));
        try {
            missing_noticeDTO.setMissing_animal_pk(animal_pk);
            session = sqlSessionFactory.openSession(true);
            session.insert("mapper.Missing_noticeMapper.InsertMissing", missing_noticeDTO);
            session.commit();
        } finally {
            session.close();
        }
        System.out.println("missing animal pk " + animal_pk);
    }

    public void RemoveMissing(long id) {
        SqlSession session = null;
        Missing_noticeDTO missing = FindByPk(id);
        try {
            session = sqlSessionFactory.openSession(true);
            session.delete("mapper.Missing_noticeMapper.RemoveMissing", id);
            session.commit();
        } finally {
            session.close();
        }
    }
}


