package DB.dao;

import DB.dto.FormDTO;
import DB.dto.VaccineDTO;
import DB.mapper.VaccineSql;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface IVaccineDAO {
    final String getAll = "SELECT * FROM vaccine";
    @Select(getAll)
    @Results(id = "vaccineResultSet", value = {
            @Result(property = "vaccine_name", column = "vaccine_name"),
            @Result(property = "vaccine_basic", column = "vaccine_basic"),
            @Result(property = "vaccine_target_age", column = "vaccine_target_age"),
            @Result(property = "vaccine_comment", column = "vaccine_comment"),
            @Result(property = "vaccine_period", column = "vaccine_period"),
            @Result(property = "vaccine_animal_kind", column = "vaccine_animal_kind"),
            @Result(property = "vaccine_animal_breed", column = "vaccine_animal_breed"),
            @Result(property = "vaccine_animal_age", column = "vaccine_animal_age"),
            @Result(property = "vaccine_form_pk", column = "vaccine_form_pk"),

    })
    List<VaccineDTO> getAll();

    //백신 정보 입력
    @Insert("insert into vaccine (vaccine_name, vaccine_basic, vaccine_target_age, vaccine_comment, vaccine_period, vaccine_animal_kind, vaccine_animal_breed, vaccine_animal_age, vaccine_form_pk) values( #{vaccine_name}, #{vaccine_basic}, #{vaccine_target_age}, #{vaccine_comment}, #{vaccine_period}, #{vaccine_animal_kind}, #{vaccine_animal_breed}, #{vaccine_animal_age}, #{vaccine_form_pk} )")
    void insert_Vaccine(VaccineDTO vaccineDTO);

    //백신 정보 수정
    @Update("update vaccine set vaccine_name = #{vaccine_name}, vaccine_basic = #{vaccine_basic}, vaccine_target_age = #{vaccine_target_age}, vaccine_comment = #{vaccine_comment}, vaccine_period = #{vaccine_period}, vaccine_animal_kind = #{vaccine_animal_kind}, vaccine_animal_breed = #{vaccine_animal_breed}, vaccine_animal_age = #{vaccine_animal_age}, vaccine_form_pk=#{vaccine_form_pk} where vaccine_pk = #{vaccine_pk} ")
    void update_Vaccine(VaccineDTO vaccineDTO);

    //백신 삭제
    @Delete("delete from vaccine where vaccine_pk = #{vaccine_pk} ")
    void delete_Vaccine(long id);

    //해당 동물 백신 정보 조회
    @SelectProvider(type = VaccineSql.class, method = "selectVaccine")
    @ResultMap("vaccineResultSet")
    List<VaccineDTO> selectVaccine(FormDTO formDTO);

    //해당 동물 백신 전체조회
    @SelectProvider(type = VaccineSql.class, method = "selectAll")
    @ResultMap("vaccineResultSet")
    List<VaccineDTO> selectAll();

    @SelectProvider(type = VaccineSql.class, method = "select")
    @ResultMap("vaccineResultSet")
    VaccineDTO select(long vaccine_pk);
}
