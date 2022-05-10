package service;

import DB.dao.RollDAO;
import DB.dto.RollDTO;

import java.util.List;

public class RollService {
    private RollDAO rollDAO;

    public RollService(RollDAO rollDAO) {
        this.rollDAO = rollDAO;
    }

    public RollDTO login(RollDTO rollDTO) throws IllegalArgumentException{
        //TODO : 해당아이디 없을때 예외처리필요
        RollDTO roll = rollDAO.select(rollDTO.getRoll_id());

        if(!roll.getRoll_pw().equals(rollDTO.getRoll_pw())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return roll;
    }

    public void changePassword(RollDTO rollDTO){
        //TODO : 해당아이디 없을때 예외처리필요
        RollDTO roll = rollDAO.select(rollDTO.getRoll_id());
        roll.setRoll_pw(rollDTO.getRoll_pw());
        rollDAO.update_Roll(roll);
    }

    public void create(RollDTO rollDTO){
        //통신을통해 받은 정보로 계 생성
        rollDAO.insert_Roll(rollDTO);
    }

    public void update(RollDTO rollDTO) throws IllegalArgumentException {
        RollDTO roll = rollDAO.select(rollDTO.getRoll_id());
        roll.setRoll_name(rollDTO.getRoll_name());
        roll.setRoll_phone(rollDTO.getRoll_phone());
        rollDAO.update_Roll(roll);
    }

    public void delete(RollDTO rollDTO) {
        rollDAO.delete_Roll(rollDTO.getRoll_id());
    }

    public RollDTO selectByID(String id) {
        return rollDAO.select(id);
    }

    public RollDTO[] selectByType(String type) {
        return rollListToDTO(rollDAO.select_type(type));
    }

    public RollDTO[] selectAll() {
        return rollListToDTO(rollDAO.selectOfAll_Roll());
    }

    private RollDTO[] rollListToDTO(List<RollDTO> list){
        RollDTO[] rollDTOS = new RollDTO[list.size()];

        for(int i = 0; i < rollDTOS.length; i++)
            rollDTOS[i] = list.get(i);

        return rollDTOS;
    }
}
