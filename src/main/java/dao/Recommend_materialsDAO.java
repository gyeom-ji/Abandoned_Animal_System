package dao;

import dto.Recommend_materialsDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class Recommend_materialsDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public Recommend_materialsDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    //추천상품 정보 입력
    public void insert_Recommend_materials(Recommend_materialsDTO recommend_materialsDTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.getMapper(IRecommend_materialsDAO.class).insert_Recommend_materials(recommend_materialsDTO);
            session.commit();
        } finally {
            session.close();
        }
    }

    //추천상품 정보 수정
    public void update_Recommend_materials(Recommend_materialsDTO recommend_materialsDTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.getMapper(IRecommend_materialsDAO.class).update_Recommend_materials(recommend_materialsDTO);
            session.commit();
        } finally {
            session.close();
        }
    }

    //추천상품 정보 삭제
    public void delete_Recommend_materials(long id) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.getMapper(IRecommend_materialsDAO.class).delete_Recommend_materials(id);
            session.commit();
        } finally {
            session.close();
        }
    }

    //해당 동물 추천상품 정보 조회
    public List<Recommend_materialsDTO> selectRecommend_materials() {
        List<Recommend_materialsDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        IRecommend_materialsDAO mapper = session.getMapper(IRecommend_materialsDAO.class);
        try {
            list = mapper.selectRecommend_materials();
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