package service;

import DB.dao.Recommend_materialsDAO;
import DB.dto.FormDTO;
import DB.dto.Recommend_materialsDTO;
import DB.dto.Shelter_listDTO;

import java.util.List;

public class Recommend_materialsService {
    private final Recommend_materialsDAO recommend_materialsDAO;

    public Recommend_materialsService(Recommend_materialsDAO recommend_materialsDAO) {
        this.recommend_materialsDAO = recommend_materialsDAO;
    }

    //추천상품 생성 기능
    public void create(Recommend_materialsDTO recommend_materialsDTO) {
        //생성한 추천상품 데이터베이스에 저장
        recommend_materialsDAO.insert_Recommend_materials(recommend_materialsDTO);
    }

    //추천상품 수정 기능
    public void update_recommend(Recommend_materialsDTO recommend_materialsDTO) {
        Recommend_materialsDTO recommend = recommend_materialsDAO.select(recommend_materialsDTO.getRecommend_materials_pk());
        //받은 정보로 해당 추천상품 수정
        recommend.setMaterials_name(recommend_materialsDTO.getMaterials_name());
        recommend.setMaterials_type(recommend_materialsDTO.getMaterials_type());
        recommend.setMaterials_url(recommend_materialsDTO.getMaterials_url());
        recommend.setMaterials_feature(recommend_materialsDTO.getMaterials_feature());
        recommend.setMaterials_img(recommend_materialsDTO.getMaterials_img());
        recommend.setMaterials_animal_kind(recommend_materialsDTO.getMaterials_animal_kind());
        recommend.setMaterials_animal_breed(recommend_materialsDTO.getMaterials_animal_breed());
        recommend.setMaterials_animal_age(recommend_materialsDTO.getMaterials_animal_age());

        //수정된 추천상품 저장
        recommend_materialsDAO.update_Recommend_materials(recommend);
    }

    //추천상품 삭제 기능
    public void delete(long id) {
        recommend_materialsDAO.delete_Recommend_materials(id);
    }

    //추천상품 전체조회
    public Recommend_materialsDTO[] selectAll(){
        return recommendListToDTO(recommend_materialsDAO.selectAll());
    }

    //추천상품 부분조회
    public Recommend_materialsDTO[] select_type(FormDTO formDTO){
        return recommendListToDTO(recommend_materialsDAO.selectRecommend_materials(formDTO));
    }

    private Recommend_materialsDTO[] recommendListToDTO(List<Recommend_materialsDTO> list){
        Recommend_materialsDTO[] recommend_materialsDTOS = new Recommend_materialsDTO[list.size()];

        for(int i = 0; i < recommend_materialsDTOS.length; i++)
            recommend_materialsDTOS[i] = list.get(i);

        return recommend_materialsDTOS;
    }


}
