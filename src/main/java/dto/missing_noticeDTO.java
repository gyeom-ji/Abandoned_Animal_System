package dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class missing_noticeDTO {
    private int missing_notice_pk;
    private int missing_animal_pk;
    private String missing_person_name;
    private String missing_animal_name;
    private String missing_email;
    private String missing_phone;
    private Date missing_date;
    private String missing_place;
}
