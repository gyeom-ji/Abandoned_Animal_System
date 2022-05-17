package DB.dao;

import DB.dto.AnimalDTO;
import DB.dto.RollDTO;
import DB.repository.RollRepository;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class RollDAO implements RollRepository {
    private SqlSessionFactory sqlSessionFactory = null;

    public RollDAO(SqlSessionFactory sqlSessionFactory) {this.sqlSessionFactory = sqlSessionFactory;}

    //계정 정보 입력
    public long insert_Roll(RollDTO rollDTO) {
        long pk = 0;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            pk = session.getMapper(IRollDAO.class).insert_Roll(rollDTO);
            session.commit();
        } finally {
            session.close();
        }
        return pk;
    }

    //계정 정보 수정
    public void update_Roll(RollDTO rollDTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.getMapper(IRollDAO.class).update_Roll(rollDTO);
            session.commit();
        }
        finally {
            session.close();
        }
    }

    //계정 정보 삭제
    public void delete_Roll(String id) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.getMapper(IRollDAO.class).delete_Roll(id);
            session.commit();
        }
        finally {
            session.close();
        }
    }
    public List<RollDTO> select_Roll(String roll_id){
        List<RollDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        IRollDAO mapper = session.getMapper(IRollDAO.class);
        try {
            list = mapper.select_Roll(roll_id);
            session.commit();
        } catch (Exception e){
            e.printStackTrace();
            session.rollback();
        }
        finally{
            session.close();
        }
        return list;
    }

    public RollDTO select(String roll_id){
        RollDTO rollDTO = null;
        SqlSession session = sqlSessionFactory.openSession();
        IRollDAO mapper = session.getMapper(IRollDAO.class);
        try{
            rollDTO = mapper.select(roll_id);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        }
        finally{
            session.close();
        }
        return rollDTO;
    }

    //계정 type 조회
    public List<RollDTO> select_type(String roll_type){
        List<RollDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        IRollDAO mapper = session.getMapper(IRollDAO.class);
        try{
            list = mapper.select_type(roll_type);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        }
        finally{
            session.close();
        }
        return list;
    }

    //계정 전체 조회
    public List<RollDTO> selectOfAll_Roll() {
        List<RollDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        IRollDAO mapper = session.getMapper(IRollDAO.class);
        try {
            list = mapper.selectOfAll_Roll();
            session.commit();
        } catch (Exception e){
            e.printStackTrace();
            session.rollback();
        }
        finally{
            session.close();
        }
        return list;
    }
}