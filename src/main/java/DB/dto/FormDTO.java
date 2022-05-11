package DB.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FormDTO {
    private long form_pk;
    private long roll_pk;
    private long abandoned_notice_pk;
    private String form_type;
    private String form_approval;
    private RollDTO rollDTO;
    private Shelter_listDTO shelter_listDTO;
    private Abandoned_noticeDTO abandoned_noticeDTO;

}
