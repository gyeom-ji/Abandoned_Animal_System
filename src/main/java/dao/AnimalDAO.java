package dao;

import dto.Abandoned_noticeDTO;
import dto.AnimalDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import java.util.List;

public class AnimalDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public AnimalDAO(SqlSessionFactory sqlSessionFactory) {this.sqlSessionFactory = sqlSessionFactory;}

    public void UpdateAnimal(AnimalDTO animalDTO)
    {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            session.update("mapper.AnimalMapper.UpdateAnimal", animalDTO);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void InsertAnimal(AnimalDTO animalDTO)
    {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession(true);
            session.insert("mapper.AnimalMapper.InsertAnimal", animalDTO);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void RemoveAnimal(long id) {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession(true);
            session.delete("mapper.AnimalMapper.RemoveAnimal", id);
            session.commit();
        } finally {
            session.close();
        }

    }
}
