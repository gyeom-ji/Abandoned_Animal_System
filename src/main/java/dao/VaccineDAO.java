package dao;

import dto.Recommend_materialsDTO;
import dto.VaccineDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class VaccineDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public VaccineDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    //백신 정보 입력
    public void insert_Vaccine(VaccineDTO vaccineDTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.getMapper(IVaccineDAO.class).insert_Vaccine(vaccineDTO);
            session.commit();
        } finally {
            session.close();
        }
    }

    //백신 정보 수정
    public void update_Vaccine(VaccineDTO vaccineDTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.getMapper(IVaccineDAO.class).update_Vaccine(vaccineDTO);
            session.commit();
        } finally {
            session.close();
        }
    }

    //백신 정보 삭제
    public void delete_Vaccine(VaccineDTO vaccineDTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.getMapper(IVaccineDAO.class).delete_Vaccine(vaccineDTO);
            session.commit();
        } finally {
            session.close();
        }
    }

    //해당 동물 백신 정보 조회
    public List<VaccineDTO> selectVaccine() {
        List<VaccineDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        IVaccineDAO mapper = session.getMapper(IVaccineDAO.class);
        try {
            list = mapper.selectVaccine();
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
