
import dao.Abandoned_noticeDAO;
import dao.AnimalDAO;
import dao.FormDAO;
import dao.Missing_noticeDAO;
import dto.Abandoned_noticeDTO;
import dto.AnimalDTO;
import dto.FormDTO;
import dto.Missing_noticeDTO;

import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Abandoned_noticeDAO abandoned_noticeDAO = new Abandoned_noticeDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        AnimalDAO animalDAO = new AnimalDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        FormDAO formDAO = new FormDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        Missing_noticeDAO missing_noticeDAO = new Missing_noticeDAO(MyBatisConnectionFactory.getSqlSessionFactory());

        Abandoned_noticeDTO abandoned_noticeDTO = new Abandoned_noticeDTO();
        AnimalDTO animalDTO = new AnimalDTO();
        FormDTO formDTO = new FormDTO();
        Missing_noticeDTO missing_noticeDTO = new Missing_noticeDTO();
//        missing_noticeDTO.setMissing_address("용산");
        missing_noticeDTO.setMissing_city("달서");
//        missing_noticeDTO.setMissing_date(new Date(20210502));
//        missing_noticeDTO.setMissing_email("ruawl12@naver.com");
//        missing_noticeDTO.setMissing_animal_pk(3);
//        missing_noticeDTO.setMissing_phone("010-3333-3333");
//        missing_noticeDTO.setMissing_person_name("겸지");
        missing_noticeDTO.setMissing_county("대구");
//        missing_noticeDTO.setMissing_animal_name("뚱");
//        missing_noticeDAO.InsertMissing(missing_noticeDTO);
        List<Missing_noticeDTO> posts = missing_noticeDAO.FindByOption(missing_noticeDTO);
        String str = posts.get(0).toString();
        System.out.println(str);

        String target = "pk";
        int target_num = str.indexOf(target);
        String result = str.substring(target_num+3,(str.substring(target_num).indexOf(',')+target_num));
        System.out.println(result);

        missing_noticeDAO.RemoveMissing(Integer.parseInt(result));
    }

}
