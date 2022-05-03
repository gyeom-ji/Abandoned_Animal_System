package dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Time;

@Getter
@Setter
@ToString
public class Shelter_listDTO {
    private int shelter_list_pk;
    private String shelter_name;
    private String shelter_phone;
    private String shelter_county;
    private String shelter_city;
    private String shelter_address;
    private String shelter_type;
    private Time shelter_open_time;
    private Time shelter_close_time;
}
