package DB.dao;

import DB.dto.RollDTO;
import DB.dto.Shelter_listDTO;
import DB.mapper.RollSql;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface IRollDAO {
    final String getAll = "SELECT * FROM roll";
    @Select(getAll)
    @Results(id = "rollResultSet", value = {
            @Result(property = "roll_id", column = "roll_id"),
            @Result(property = "roll_pw", column = "roll_pw"),
            @Result(property = "roll_name", column = "roll_name"),
            @Result(property = "roll_phone", column = "roll_phone"),
            @Result(property = "roll_type", column = "roll_type")
    })
    List<RollDTO> getAll();

    //계정 정보 입력
    @Insert("insert into roll (roll_id, roll_pw, roll_name, roll_phone, roll_type) values( #{roll_id}, #{roll_pw}, #{roll_name}, #{roll_phone}, #{roll_type})")
    @SelectKey(keyColumn="roll_pk", keyProperty="roll_pk", resultType=long.class, before=false, statement="select last_insert_id()")
    long insert_Roll(RollDTO rollDTO);

    //계정 정보 수정 (
    @Update("update roll set roll_pw = #{roll_pw}, roll_name = #{roll_name}, roll_phone = #{roll_phone} where roll_id = #{roll_id} ")
    void update_Roll(RollDTO rollDTO);

    //계정 정보 삭제 (
    @Delete("delete from roll where roll_id = #{roll_id} ")
    void delete_Roll(String id);

    @SelectProvider(type = RollSql.class, method = "select_Roll")
    @ResultMap("rollResultSet")
    List<RollDTO> select_Roll(String roll_id);

    @Select("select * from roll where roll_id=#{roll_id")
    RollDTO select(String roll_id);


    //계정 type으로 정보 조회
    @SelectProvider(type = RollSql.class, method = "select_type")
    @ResultMap("rollResultSet")
    List<RollDTO> select_type(String roll_type);

    //계정 전체 조회
    @SelectProvider(type = RollSql.class, method = "selectOfAll_Roll")
    @ResultMap("rollResultSet")
    List<RollDTO> selectOfAll_Roll();
}
