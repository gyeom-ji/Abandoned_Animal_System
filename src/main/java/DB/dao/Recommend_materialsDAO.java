package DB.dao;

import DB.dto.FormDTO;
import DB.dto.Recommend_materialsDTO;
import DB.dto.Shelter_listDTO;
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
        long form_pk = recommend_materialsDTO.getFormDTO().getForm_pk();
        try {
            recommend_materialsDTO.getFormDTO().setForm_pk(form_pk);
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
    public List<Recommend_materialsDTO> selectRecommend_materials(FormDTO formDTO) {
        List<Recommend_materialsDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        IRecommend_materialsDAO mapper = session.getMapper(IRecommend_materialsDAO.class);
        try {
            list = mapper.selectRecommend_materials(formDTO);
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

    //해당 동물 추천상품 전체 정보 조회
    public List<Recommend_materialsDTO> selectAll() {
        List<Recommend_materialsDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        IRecommend_materialsDAO mapper = session.getMapper(IRecommend_materialsDAO.class);
        try {
            list = mapper.selectAll();
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

    public Recommend_materialsDTO select(long shelter_list_pk){
        Recommend_materialsDTO recommend_materialsDTO = null;
        SqlSession session = sqlSessionFactory.openSession();
        IRecommend_materialsDAO mapper = session.getMapper(IRecommend_materialsDAO.class);
        try{
            recommend_materialsDTO = mapper.select(shelter_list_pk);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        }
        finally{
            session.close();
        }
        return recommend_materialsDTO;
    }
}