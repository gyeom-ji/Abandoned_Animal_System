package service;

import DB.dao.Shelter_listDAO;
import DB.dto.RollDTO;
import DB.dto.Shelter_listDTO;

import java.util.List;

public class Shelter_listService {
    private final Shelter_listDAO shelter_listDAO;

    public Shelter_listService(Shelter_listDAO shelter_listDAO) {
        this.shelter_listDAO = shelter_listDAO;
    }

    //shelter_list 생성 기능
    public void create(Shelter_listDTO shelter_listDTO) {
        //생성한 보호소 데이터베이스에 저장
        shelter_listDAO.insert_Shelter_list(shelter_listDTO);
    }

    //shelter_list 수정 기능
    public void update(Shelter_listDTO shelter_listDTO) {
        Shelter_listDTO shelter = shelter_listDAO.select(shelter_listDTO.getShelter_name());
        //받은 정보로 해당 보호소 수정
        shelter.setShelter_name(shelter_listDTO.getShelter_name());
        shelter.setShelter_phone(shelter_listDTO.getShelter_phone());
        shelter.setShelter_county(shelter_listDTO.getShelter_county());
        shelter.setShelter_city(shelter_listDTO.getShelter_city());
        shelter.setShelter_address(shelter_listDTO.getShelter_address());
        shelter.setShelter_type(shelter_listDTO.getShelter_open_time());
        shelter.setShelter_close_time(shelter_listDTO.getShelter_close_time());

        //수정된 보호소 저장
        shelter_listDAO.update_Shelter_list(shelter);
    }

    //shelter_list 삭제 기능
    public void delete(Shelter_listDTO shelter_listDTO) {
        shelter_listDAO.delete_Shelter_list(shelter_listDTO.getShelter_list_pk());
    }

    //shelter_list 전체조회
    public Shelter_listDTO[] selectAll(){
       return shelterListToDTO(shelter_listDAO.selectOfAll_Shelter_list());
    }

    //shelter_list 지역 조회
    public Shelter_listDTO[]  select_address(String county, String city){
        return shelterListToDTO(shelter_listDAO.selectShelter_list_place(county, city));
    }

    private Shelter_listDTO[] shelterListToDTO(List<Shelter_listDTO> list){
        Shelter_listDTO[] shelter_listDTOS = new Shelter_listDTO[list.size()];

        for(int i = 0; i < shelter_listDTOS.length; i++)
            shelter_listDTOS[i] = list.get(i);

        return shelter_listDTOS;
    }

}
