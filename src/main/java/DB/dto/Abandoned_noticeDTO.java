package DB.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Abandoned_noticeDTO {
    private int abandoned_notice_pk;
    private int abandoned_animal_pk;
    private int shelter_list_pk;
    private String abandoned_notice_num;
    private String abandoned_receipt_date;
    private String abandoned_place;
    private String abandoned_period;

}
