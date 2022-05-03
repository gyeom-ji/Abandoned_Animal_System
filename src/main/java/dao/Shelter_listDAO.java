package dao;

import dto.Shelter_listDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class Shelter_listDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public Shelter_listDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    //보호소 전체 조회
    public List<Shelter_listDTO> selectOfAll_Shelter_list() {
        List<Shelter_listDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        IShelter_listDAO mapper = session.getMapper(IShelter_listDAO.class);
        try {
            list = mapper.selectOfAll_Shelter_list();
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

    //보호소 지역 조회
    public List<Shelter_listDTO> selectShelter_list_place(final String shelter_county, final String shelter_city) {
        List<Shelter_listDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        IShelter_listDAO mapper = session.getMapper(IShelter_listDAO.class);
        try {
            list = mapper.selectShelter_list_place(shelter_county, shelter_city);
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

    //보호소 정보 입력
    public void insert_Shelter_list(Shelter_listDTO shelter_listDTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.getMapper(IShelter_listDAO.class).insert_Shelter_list(shelter_listDTO);
            session.commit();
        } finally {
            session.close();
        }
    }

    //보호소 정보 수정
    public void update_Shelter_list(Shelter_listDTO shelter_listDTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.getMapper(IShelter_listDAO.class).update_Shelter_list(shelter_listDTO);
            session.commit();
        }
        finally {
            session.close();
        }
    }

    //보호소 정보 삭제
    public void delete_Shelter_list(Shelter_listDTO shelter_listDTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.getMapper(IShelter_listDAO.class).delete_Shelter_list(shelter_listDTO);
            session.commit();
        }
        finally {
            session.close();
        }
    }
}
