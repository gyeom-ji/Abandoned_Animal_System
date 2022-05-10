package dao;

import dto.Recommend_materialsDTO;
import dto.Shelter_listDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface IRecommend_materialsDAO {
    final String getAll = "SELECT * FROM recommend_materials";
    @Select(getAll)
    @Results(id = "recommend_materialsResultSet", value = {
            @Result(property = "materials_name", column = "materials_name"),
            @Result(property = "materials_type", column = "materials_type"),
            @Result(property = "materials_url", column = "materials_url"),
            @Result(property = "materials_feature", column = "materials_feature"),
            @Result(property = "materials_img", column = "materials_img"),
            @Result(property = "materials_form_pk", column = "materials_form_pk"),
            @Result(property = "materials_animal_kind", column = "materials_animal_kind"),
            @Result(property = "materials_animal_breed", column = "materials_animal_breed"),
            @Result(property = "materials_animal_age", column = "materials_animal_age")
    })
    List<Recommend_materialsDTO> getAll();

    //추천상품 정보 입력
    @Insert("insert into recommend_materials (materials_name, materials_type, materials_url, materials_feature, materials_img, materials_animal_kind,materials_animal_breed,materials_animal_age,materials_form_pk) " +
            "values( #{materials_name}, #{materials_type}, #{materials_url}, #{materials_feature}, #{materials_img},  #{materials_animal_kind}, #{materials_animal_breed}, #{materials_animal_age}, #{materials_form_pk} )")
    void insert_Recommend_materials(Recommend_materialsDTO recommend_materialsDTO);

    //추천상품 정보 수정
    @Update("update recommend_materials set materials_name = #{materials_name}, materials_type = #{materials_type}, materials_url = #{materials_url}, materials_feature = #{materials_feature}, materials_img = #{materials_img}, materials_form_pk = #{materials_form_pk}, materials_animal_kind = #{materials_animal_kind}, materials_animal_breed = #{materials_animal_breed}, materials_animal_age = #{materials_animal_age} where recommend_materials_pk = #{recommend_materials_pk} ")
    void update_Recommend_materials(Recommend_materialsDTO recommend_materialsDTO);

    //추천상품 삭제
    @Delete("delete from recommend_materials where recommend_materials_pk = #{recommend_materials_pk} ")
    void delete_Recommend_materials(long id);

    //해당 동물 추천상품 조회
    @SelectProvider(type = mapper.Recommend_materialsSql.class, method = "selectRecommend_materials")
    @ResultMap("recommend_materialsResultSet")
    List<Recommend_materialsDTO> selectRecommend_materials();

}
