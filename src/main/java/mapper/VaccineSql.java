package mapper;

import org.apache.ibatis.jdbc.SQL;

public class VaccineSql {
    //해당 동물 백신 정보 조회
    public String selectVaccine(){
        SQL sql = new SQL()
                .SELECT("vaccine.vaccine_name, vaccine.vaccine_basic, vaccine.vaccine_target_age, vaccine.vaccine_comment, vaccine.vaccine_period, vaccine.vaccine_animal_kind, vaccine.vaccine_animal_breed, vaccine.vaccine_animal_age")
                .FROM("vaccine, form, abandoned_notice, animal")
                .WHERE("vaccine_animal_kind = animal_kind")
                .AND()
                .WHERE("vaccine_animal_breed = animal_breed")
                .AND()
                .WHERE("vaccine_animal_age = animal_age")
                .AND()
                .WHERE("form.abandoned_notice_pk = abandoned_notice.abandoned_notice_pk")
                .AND()
                .WHERE("abandoned_animal_pk = animal_pk");

        return sql.toString();
    }
}
