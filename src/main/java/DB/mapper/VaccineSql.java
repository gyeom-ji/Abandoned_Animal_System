package DB.mapper;

import DB.dto.FormDTO;
import org.apache.ibatis.jdbc.SQL;

public class VaccineSql {
    //해당 동물 백신 정보 조회
    public String selectVaccine(FormDTO formDTO){
        SQL sql = new SQL()
                .SELECT("vaccine.vaccine_name, vaccine.vaccine_basic, vaccine.vaccine_target_age, vaccine.vaccine_comment, vaccine.vaccine_period, vaccine.vaccine_animal_kind, vaccine.vaccine_animal_breed, vaccine.vaccine_animal_age")
                .FROM("vaccine, form, abandoned_notice, animal")
                .WHERE("vaccine.vaccine_animal_kind = animal.animal_kind")
                .AND()
                .WHERE("vaccine.vaccine_animal_breed = animal.animal_breed")
                .AND()
                .WHERE("vaccine.vaccine_animal_age = animal.animal_age")
                .AND()
                .WHERE("vaccine.vaccine_form_pk = form.form_pk")
                .AND()
                .WHERE("form.abandoned_notice_pk = abandoned_notice.abandoned_notice_pk")
                .AND()
                .WHERE("abandoned_notice.abandoned_animal_pk = abandoned_notice.animal_pk");

        return sql.toString();
    }

    //해당 동물 백신 전체조회
    public String selectAll(){
        SQL sql = new SQL()
                .SELECT("*")
                .FROM("vaccine");

        return sql.toString();
    }

    public String select(long vaccine_pk){
        SQL sql = new SQL()
                .SELECT("*")
                .FROM("vaccine")
                .WHERE("vaccine_pk=#{vaccine_pk}");
        return sql.toString();
    }
}
