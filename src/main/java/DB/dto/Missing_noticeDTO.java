package DB.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Missing_noticeDTO {
    private AnimalDTO animalDTO;

    private long missing_notice_pk;
    private long missing_animal_pk;
    private String missing_person_name;
    private String missing_animal_name;
    private String missing_email;
    private String missing_phone;
    private String missing_date;
    private String missing_county;
    private String missing_city;
    private String missing_address;

}
