package DB.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RollDTO {
    private long roll_pk;
    private String roll_id;
    private String roll_pw;
    private String roll_name;
    private String roll_phone;
    private String roll_type;

    public RollDTO(String roll_id, String roll_pw) {
        this.roll_id = roll_id;
        this.roll_pw = roll_pw;
    }

    public RollDTO(String roll_id, String roll_pw, String roll_name, String roll_phone) {
        this.roll_id = roll_id;
        this.roll_pw = roll_pw;
        this.roll_name = roll_name;
        this.roll_phone = roll_phone;
    }

    public RollDTO(int roll_pk, String roll_id, String roll_pw, String roll_name, String roll_phone, String roll_type) {
        this.roll_pk = roll_pk;
        this.roll_id = roll_id;
        this.roll_pw = roll_pw;
        this.roll_name = roll_name;
        this.roll_phone = roll_phone;
        this.roll_type = roll_type;
    }

}
