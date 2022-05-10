package service;

import DB.dao.VaccineDAO;
import DB.dto.FormDTO;
import DB.dto.Recommend_materialsDTO;
import DB.dto.VaccineDTO;

import java.util.List;

public class VaccineService {
    private final VaccineDAO vaccineDAO;

    public VaccineService(VaccineDAO vaccineDAO) {
        this.vaccineDAO = vaccineDAO;
    }

    //백신 생성 기능
    public void create(VaccineDTO vaccineDTO) {
        //생성한 백신 데이터베이스에 저장
        vaccineDAO.insert_Vaccine(vaccineDTO);
    }

    //백신 수정 기능
    public void update_vaccine(VaccineDTO vaccineDTO) {
        VaccineDTO vaccine = vaccineDAO.select(vaccineDTO.getVaccine_pk());
        //받은 정보로 해당 백신 수정
        vaccine.setVaccine_name(vaccineDTO.getVaccine_name());
        vaccine.setVaccine_basic(vaccineDTO.getVaccine_basic());
        vaccine.setVaccine_target_age(vaccineDTO.getVaccine_target_age());
        vaccine.setVaccine_comment(vaccineDTO.getVaccine_comment());
        vaccine.setVaccine_period(vaccineDTO.getVaccine_period());
        vaccine.setVaccine_animal_kind(vaccineDTO.getVaccine_name());
        vaccine.setVaccine_name(vaccineDTO.getVaccine_animal_breed());
        vaccine.setVaccine_animal_age(vaccineDTO.getVaccine_animal_age());

        //수정된 백신 저장
        vaccineDAO.update_Vaccine(vaccine);
    }

    //백신 삭제 기능
    public void delete(long id) {
        vaccineDAO.delete_Vaccine(id);
    }

    //백신 전체조회
    public VaccineDTO[] selectAll(){
        return vaccineListToDTO(vaccineDAO.selectAll());
    }

    //백신 부분조회
    public VaccineDTO[] select_type(FormDTO formDTO){
        return vaccineListToDTO(vaccineDAO.selectVaccine(formDTO));
    }

    private VaccineDTO[] vaccineListToDTO(List<VaccineDTO> list){
        VaccineDTO[] vaccineDTOS = new VaccineDTO[list.size()];

        for(int i = 0; i < vaccineDTOS.length; i++)
            vaccineDTOS[i] = list.get(i);

        return vaccineDTOS;
    }

}
