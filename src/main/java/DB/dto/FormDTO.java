package DB.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FormDTO {
    private int form_pk;
    private int roll_pk;
    private int abandoned_notice_pk;
    private String form_type;
    private String form_approval;

}
