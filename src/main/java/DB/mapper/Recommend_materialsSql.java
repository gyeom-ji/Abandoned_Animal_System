package DB.mapper;

import DB.dto.FormDTO;
import org.apache.ibatis.jdbc.SQL;

public class Recommend_materialsSql {
    //해당 동물 추천상품 조회
    public String selectRecommend_materials(FormDTO formDTO){
        SQL sql = new SQL()
                .SELECT("recommend_materials.materials_name, recommend_materials.materials_type, recommend_materials.materials_url, recommend_materials.materials_feature, recommend_materials.materials_img, recommend_materials.materials_animal_kind, recommend_materials.materials_animal_breed, recommend_materials.materials_animal_age")
                .FROM("recommend_materials, form, abandoned_notice, animal")
                .WHERE("recommend_materials.materials_animal_kind = animal.animal_kind")
                .AND()
                .WHERE("recommend_materials.materials_animal_breed = animal.animal_breed")
                .AND()
                .WHERE("recommend_materials.materials_animal_age = animal.animal_age")
                .AND()
                .WHERE("form.abandoned_notice_pk = abandoned_notice.abandoned_notice_pk")
                .AND()
                .WHERE("abandoned_animal_pk = animal_pk");

        return sql.toString();
    }

    //해당 동물 추천상품 전체조회
    public String selectAll(){
        SQL sql = new SQL()
                .SELECT("*")
                .FROM("recommend_materials");

        return sql.toString();
    }


    public String select(long recommend_materials_pk){
        SQL sql = new SQL()
                .SELECT("*")
                .FROM("recommend_materials")
                .WHERE("recommend_materials_pk=#{recommend_materials_pk}");
        return sql.toString();
    }
}
