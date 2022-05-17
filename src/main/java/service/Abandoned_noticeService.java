package service;

import DB.dao.Abandoned_noticeDAO;
import DB.dao.Shelter_listDAO;
import DB.dto.Abandoned_noticeDTO;
import DB.dto.Shelter_listDTO;

import java.util.List;

public class Abandoned_noticeService {
    private final Abandoned_noticeDAO abandoned_noticeDAO;

    public Abandoned_noticeService(Abandoned_noticeDAO abandoned_noticeDAO) {
        this.abandoned_noticeDAO = abandoned_noticeDAO;
    }

    //유기공고 생성 기능
    public void create(Abandoned_noticeDTO abandoned_noticeDTO) {
        //생성한 보호소 데이터베이스에 저장
        abandoned_noticeDAO.InsertAbandoned(abandoned_noticeDTO);
    }

    //유기공고 수정 기능
    public void update(Abandoned_noticeDTO abandoned_noticeDTO) {
        Abandoned_noticeDTO abandoned = abandoned_noticeDAO.FindByAbandoned_notice_num(abandoned_noticeDTO.getAbandoned_notice_num());
        //받은 정보로 해당 유기공고 수정

        abandoned.setAbandoned_notice_num(abandoned_noticeDTO.getAbandoned_notice_num());
        abandoned.setAbandoned_receipt_date(abandoned_noticeDTO.getAbandoned_receipt_date());
        abandoned.setAbandoned_place(abandoned_noticeDTO.getAbandoned_place());

        //수정된 유기공고 저장
        abandoned_noticeDAO.UpdateAbandoned(abandoned);
    }

    //유기공고 삭제 기능
    public void delete(String abandoned_notice_num) {
        abandoned_noticeDAO.RemoveAbandoned(abandoned_notice_num);
    }

    //유기공고 전체조회
    public Abandoned_noticeDTO[] select(){
        return Abandoned_noticeListToDTO(abandoned_noticeDAO.ReadAll());
    }

    //유기공고 지역 조회
    public Abandoned_noticeDTO[] select_address(String county, String city){
        return Abandoned_noticeListToDTO(abandoned_noticeDAO.FindByOption(county, city));
    }

    private Abandoned_noticeDTO[] Abandoned_noticeListToDTO(List<Abandoned_noticeDTO> list){
        Abandoned_noticeDTO[] abandoned_noticeDTOS = new Abandoned_noticeDTO[list.size()];

        for(int i = 0; i < abandoned_noticeDTOS.length; i++)
            abandoned_noticeDTOS[i] = list.get(i);

        return abandoned_noticeDTOS;
    }
}
