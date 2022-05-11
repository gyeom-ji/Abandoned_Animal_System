package service;

import DB.dao.Abandoned_noticeDAO;
import DB.dao.Missing_noticeDAO;
import DB.dto.Abandoned_noticeDTO;
import DB.dto.Missing_noticeDTO;
import DB.dto.Shelter_listDTO;

import java.util.List;

public class Missing_noticeService {
    private final Missing_noticeDAO missing_noticeDAO;

    public Missing_noticeService(Missing_noticeDAO missing_noticeDAO) {
        this.missing_noticeDAO = missing_noticeDAO;
    }

    //실종공고 생성 기능
    public void create(Missing_noticeDTO missing_noticeDTO) {
        //생성한 보호소 데이터베이스에 저장
        missing_noticeDAO.InsertMissing(missing_noticeDTO);
    }

    //실종공고 수정 기능
    public void update(Missing_noticeDTO missing_noticeDTO) {
        Missing_noticeDTO missing = missing_noticeDAO.FindByPk(missing_noticeDTO.getMissing_notice_pk());

        missing.setMissing_person_name(missing_noticeDTO.getMissing_person_name());
        missing.setMissing_animal_name(missing_noticeDTO.getMissing_animal_name());
        missing.setMissing_email(missing_noticeDTO.getMissing_email());
        missing.setMissing_phone(missing_noticeDTO.getMissing_phone());
        missing.setMissing_date(missing_noticeDTO.getMissing_date());
        missing.setMissing_county(missing_noticeDTO.getMissing_county());
        missing.setMissing_city(missing_noticeDTO.getMissing_city());
        missing.setMissing_address(missing_noticeDTO.getMissing_address());
        missing.setAnimalDTO(missing_noticeDTO.getAnimalDTO());

        //수정된 실종공고 저장
        missing_noticeDAO.UpdateMissing(missing);
    }

    //실종공고 삭제 기능
    public void delete(long missing_notice_pk) {
        missing_noticeDAO.RemoveMissing(missing_notice_pk);
    }

    //실종공고 전체조회
    public Missing_noticeDTO[] select(){
        return Missing_noticeListToDTO(missing_noticeDAO.ReadAll());
    }

    //실종공고 지역 조회
    public Missing_noticeDTO[] select_address(String county, String city){
        return Missing_noticeListToDTO(missing_noticeDAO.FindByOption(county, city));
    }

    private Missing_noticeDTO[] Missing_noticeListToDTO(List<Missing_noticeDTO> list){
        Missing_noticeDTO[] missing_noticeDTOS = new Missing_noticeDTO[list.size()];

        for(int i = 0; i < missing_noticeDTOS.length; i++)
            missing_noticeDTOS[i] = list.get(i);

        return missing_noticeDTOS;
    }
}

