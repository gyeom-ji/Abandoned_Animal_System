package DB.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FormDTO {
    private RollDTO rollDTO;
    private Abandoned_noticeDTO abandoned_noticeDTO;

    private long form_pk;
    private long roll_pk;
    private long abandoned_notice_pk;
    private String form_type;
    private String form_approval;

}
