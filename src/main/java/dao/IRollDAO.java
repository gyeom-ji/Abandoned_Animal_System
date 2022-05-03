package dao;

import dto.RollDTO;
import dto.Shelter_listDTO;
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
    void insert_Roll(RollDTO rollDTO);

    //계정 정보 수정 (
    @Update("update roll set roll_id = #{roll_id}, roll_pw = #{roll_pw}, roll_name = #{roll_name}, roll_phone = #{roll_phone}, roll_type = #{roll_type} where roll_pk = #{roll_pk} ")
    void update_Roll(RollDTO rollDTO);

    //계정 정보 삭제 (
    @Delete("delete from roll where roll_pk = #{roll_pk} ")
    void delete_Roll(RollDTO rollDTO);
}
