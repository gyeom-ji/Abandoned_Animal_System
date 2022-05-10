package service;

import DB.dao.FormDAO;
import DB.dto.FormDTO;

import java.util.List;

public class FormService {
    private final FormDAO formDAO;

    public FormService(FormDAO formDAO) {
        this.formDAO = formDAO;
    }

    //폼 생성 기능
    public void create(FormDTO formDTO) {
        //생성한 form 데이터베이스에 저장
        formDAO.InsertForm(formDTO);
    }

    //폼(회원) 수정 기능
    public void update_member(FormDTO formDTO) {
        FormDTO form = formDAO.FindByForm_id(formDTO.getForm_pk());
        //받은 정보로 해당 form 수정
        form.setForm_type(formDTO.getForm_type());

        //수정된 form 저장
        formDAO.UpdateType(form);
    }

    //폼(승인) 수정 기능
    public void update_approval(long form_pk) {
        FormDTO form = formDAO.FindByForm_id(form_pk);
        //받은 정보로 해당 form 수정
        form.setForm_approval(form.getForm_approval());

        //수정된 form 저장
        formDAO.UpdateForm(form);
    }

    //폼 삭제 기능
    public void delete(long id) {
        formDAO.RemoveForm(id);
    }

    //폼 회원 조회
    public FormDTO[] FindByMember(String roll_id){
        return formDTO(formDAO.FindByMember(roll_id));
    }

    //폼 스태프 조회
    public FormDTO[]  FindByStaff(String shelter_name) { return formDTO(formDAO.FindByStaff(shelter_name)); }

    private FormDTO[] formDTO(List<FormDTO> list){
        FormDTO[] formDTOS = new FormDTO[list.size()];

        for(int i = 0; i < formDTOS.length; i++)
            formDTOS[i] = list.get(i);

        return formDTOS;
    }
}
