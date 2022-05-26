package DB.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.List;
import java.sql.Time;

@Getter
@Setter
@ToString
public class Shelter_listDTO {
    private long shelter_list_pk;
    private String shelter_name;
    private String shelter_phone;
    private String shelter_county;
    private String shelter_city;
    private String shelter_address;
    private String shelter_type;
    private String shelter_open_time;
    private String shelter_close_time;

    public Shelter_listDTO(){}
}
