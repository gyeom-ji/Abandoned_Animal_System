package dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class abandoned_noticeDTO {
    private int abandoned_notice_pk;
    private String abandoned_notice_num;
    private Date abandoned_receipt_date;
    private String abandoned_place;
}
