package DB.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class Shelter_listSql {

    //보호소 전체 조회
    public String selectOfAll_Shelter_list(){
        SQL sql = new SQL()
                .SELECT("*")
                .FROM("shelter_list");
        return sql.toString();
    }

    //보호소 부분 조회
    public String selectShelter_list_place(@Param("shelter_county") final String shelter_county, @Param("shelter_city") final String shelter_city){
        SQL sql = new SQL()
                .SELECT("*")
                .FROM("shelter_list")
                .WHERE("shelter_county=#{shelter_county}")
                .AND()
                .WHERE("shelter_city=#{shelter_city}");
        return sql.toString();
    }
}
