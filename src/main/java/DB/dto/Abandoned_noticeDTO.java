package DB.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Getter
@Setter
@ToString
public class Abandoned_noticeDTO {
    private long abandoned_notice_pk;
    private long abandoned_animal_pk;
    private long shelter_list_pk;
    private String abandoned_notice_num;
    private String abandoned_receipt_date;
    private String abandoned_place;

    public List<AnimalDTO> animalDTOList;
    public List<Shelter_listDTO> shelter_listDTOList;
}
