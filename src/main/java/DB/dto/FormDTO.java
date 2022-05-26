package DB.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class FormDTO {
    private long form_pk;
    private long roll_pk;
    private long abandoned_notice_pk;
    private String form_type;
    private String form_approval;
    public List<Abandoned_noticeDTO> abandonedNoticeDTOList;
    public List<RollDTO> rollDTOList;
    public List<Shelter_listDTO> shelter_listDTOList;
}
