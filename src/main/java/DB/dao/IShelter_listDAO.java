package DB.dao;

import DB.dto.Shelter_listDTO;
import DB.mapper.Shelter_listSql;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface IShelter_listDAO {
    final String getAll = "SELECT * FROM shelter_list";
    @Select(getAll)
    @Results(id = "shelter_listResultSet", value = {
            @Result(property = "shelter_name", column = "shelter_name"),
            @Result(property = "shelter_phone", column = "shelter_phone"),
            @Result(property = "shelter_county", column = "shelter_county"),
            @Result(property = "shelter_city", column = "shelter_city"),
            @Result(property = "shelter_address", column = "shelter_address"),
            @Result(property = "shelter_type", column = "shelter_type"),
            @Result(property = "shelter_open_time", column = "shelter_open_time"),
            @Result(property = "shelter_close_time", column = "shelter_close_time")
    })
    List<Shelter_listDTO> getAll();

    //보호소 전체 조회
    @SelectProvider(type = Shelter_listSql.class, method = "selectOfAll_Shelter_list")
    @ResultMap("shelter_listResultSet")
    List<Shelter_listDTO> selectOfAll_Shelter_list();

    //보호소 지역 조회
    @SelectProvider(type = Shelter_listSql.class, method = "selectShelter_list_place")
    @ResultMap("shelter_listResultSet")
    List<Shelter_listDTO> selectShelter_list_place(@Param("shelter_county") String shelter_county, @Param("shelter_city") String shelter_city);

    //보호소 정보 입력
    @Insert("insert into shelter_list (shelter_name, shelter_phone, shelter_county, shelter_city, shelter_address, shelter_type, shelter_open_time, shelter_close_time) values( #{shelter_name}, #{shelter_phone}, #{shelter_county}, #{shelter_city}, #{shelter_address}, #{shelter_type}, #{shelter_open_time}, #{shelter_close_time} )")
    long insert_Shelter_list(Shelter_listDTO shelter_listDTO);

    //보호소 정보 수정 (
    @Update("update shelter_list set shelter_name = #{shelter_name}, shelter_phone = #{shelter_phone}, shelter_county = #{shelter_county}, shelter_city = #{shelter_city}, shelter_address = #{shelter_address}, shelter_type = #{shelter_type}, shelter_open_time = #{shelter_open_time}, shelter_close_time = #{shelter_close_time} where shelter_list_pk = #{shelter_list_pk} ")
    void update_Shelter_list(Shelter_listDTO shelter_listDTO);

    //보호소 정보 삭제 (
    @Delete("delete from shelter_list where shelter_list_pk = #{shelter_list_pk} ")
    void delete_Shelter_list(long id);

    @Select("select * from shelter_list where shelter_name=#{shelter_name")
    Shelter_listDTO select(String shelter_name);

    //유기동물 공고 insert용 select
    @Select("select * from shelter_list where shelter_name=#{shelter_name and where shelter_phone=#{shelter_phone}")
    Shelter_listDTO select_abandoned(String shelter_name, String shelter_phone);
}
