package DB.dao;

import DB.dto.Abandoned_noticeDTO;
import DB.dto.AnimalDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import java.util.List;

public class AnimalDAO{
    private SqlSessionFactory sqlSessionFactory = null;

    public AnimalDAO(SqlSessionFactory sqlSessionFactory) {this.sqlSessionFactory = sqlSessionFactory;}

    public AnimalDTO SelectAnimalMissing(long pk)
    {
        AnimalDTO animalDTO = null;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            animalDTO = session.selectOne("mapper.AnimalMapper.SelectAnimalMissing", pk);
        } finally {
            session.close();
        }
        return animalDTO;
    }

    public AnimalDTO SelectAnimalAbandoned(long pk)
    {
        AnimalDTO animalDTO = null;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            animalDTO = session.selectOne("mapper.AnimalMapper.SelectAnimalAbandoned", pk);
        } finally {
            session.close();
        }
        return animalDTO;
    }

    public List<AnimalDTO> SelectAnimalAll()
    {
        List<AnimalDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            list = session.selectList("mapper.AnimalMapper.SelectAnimalAll");
        } finally {
            session.close();
        }
        return list;
    }

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

    public long InsertAnimal(AnimalDTO animalDTO)
    {
        int pk = 0;
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession(true);
            session.insert("mapper.AnimalMapper.InsertAnimal", animalDTO);
            session.commit();
        } finally {
            session.close();
        }
        System.out.println(animalDTO.getAnimal_pk());
        return animalDTO.getAnimal_pk();
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

    //
//    public AnimalDTO FindByAbandoned_notice_num(String abandoned_notice_num){
//        Abandoned_noticeDTO abandoned_noticeDTO = null;
//        SqlSession session = sqlSessionFactory.openSession();
//        try {
//            abandoned_noticeDTO = session.selectOne("mapper.Abandoned_noticeMapper.FindByAbandoned_notice_num", abandoned_notice_num);
//        }
//        finally{
//            session.close();
//        }
//        return abandoned_noticeDTO;
//    }
}
