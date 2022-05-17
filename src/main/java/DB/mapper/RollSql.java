package DB.mapper;

import org.apache.ibatis.jdbc.SQL;

public class RollSql {
    //계정 id로 정보 조회
    public String select_Roll(String roll_id){
        SQL sql = new SQL()
                .SELECT("*")
                .FROM("roll")
                .WHERE("roll_id=#{roll_id}");
        return sql.toString();
    }

    //계정 전체 조회
    public String selectOfAll_Roll(){
        SQL sql = new SQL()
                .SELECT("*")
                .FROM("roll");
        return sql.toString();
    }

    //계정 type 조회
    public String select_type(String roll_type){
        SQL sql = new SQL()
                .SELECT("*")
                .FROM("roll")
                .WHERE("roll_type=#{roll_type}");
        return sql.toString();
    }

    //계정 type 조회
    public String select(String roll_id){
        SQL sql = new SQL()
                .SELECT("*")
                .FROM("roll")
                .WHERE("roll_id=#{roll_id}");
        return sql.toString();
    }
}
