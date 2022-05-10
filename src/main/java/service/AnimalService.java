package service;

import DB.dao.AnimalDAO;
import DB.dao.Shelter_listDAO;
import DB.dto.Abandoned_noticeDTO;
import DB.dto.AnimalDTO;
import DB.dto.Shelter_listDTO;

import java.util.List;

public class AnimalService {
    private final AnimalDAO animalDAO;

    public AnimalService(AnimalDAO animalDAO) {
        this.animalDAO = animalDAO;
    }

    //animal 생성 기능
    public void create(AnimalDTO animalDTO) {
        //생성한 animal 데이터베이스에 저장
        animalDAO.InsertAnimal(animalDTO);
    }

    //animal 수정 기능
    public void update(AnimalDTO animalDTO) {
        AnimalDTO animal = animalDAO.SelectAnimalAbandoned(animalDTO.getAnimal_pk());
        //받은 정보로 해당 animal 수정

        animal.setAnimal_sex(animalDTO.getAnimal_sex());
        animal.setAnimal_age(animalDTO.getAnimal_age());
        animal.setAnimal_color(animalDTO.getAnimal_color());
        animal.setAnimal_feature(animalDTO.getAnimal_feature());
        animal.setAnimal_breed(animalDTO.getAnimal_breed());
        animal.setAnimal_neuter(animalDTO.getAnimal_neuter());
        animal.setAnimal_img(animalDTO.getAnimal_img());

        //수정된 animal 저장
        animalDAO.UpdateAnimal(animal);
    }

    //animal 삭제 기능
    public void delete(long id) {
        animalDAO.RemoveAnimal(id);
    }

    public AnimalDTO selectAnimalAbandoned(long pk) {
        return animalDAO.SelectAnimalAbandoned(pk);
    }

    public AnimalDTO SelectAnimalMissing(long pk) {
        return animalDAO.SelectAnimalMissing(pk);
    }

    private AnimalDTO[] animalListToDTO(List<AnimalDTO> list){
        AnimalDTO[] animalDTOS = new AnimalDTO[list.size()];

        for(int i = 0; i < animalDTOS.length; i++)
            animalDTOS[i] = list.get(i);

        return animalDTOS;
    }

}
