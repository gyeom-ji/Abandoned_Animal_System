package dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RollDTO {
    private int roll_pk;
    private String roll_id;
    private String roll_pw;
    private String roll_name;
    private String roll_phone;
    private String roll_type;
}
