package DB.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VaccineDTO {
    private  FormDTO formDTO;
    private long vaccine_pk;
    private String vaccine_name;
    private String vaccine_basic;
    private String vaccine_target_age;
    private String vaccine_comment;
    private String vaccine_period;
    private long vaccine_form_pk;
    private String vaccine_animal_kind;
    private String vaccine_animal_breed;
    private String vaccine_animal_age;


}
