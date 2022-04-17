package dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class adminDTO {
    private int admin_pk;
    private String admin_id;
    private String admin_pw;
    private String admin_name;
    private String admin_phone;
    private String admin_email;
}
