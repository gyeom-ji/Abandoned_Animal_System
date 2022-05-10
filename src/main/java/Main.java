
import DB.dao.Shelter_listDAO;
import DB.dto.Shelter_listDTO;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {

        Shelter_listDAO shelter_listDAO = new Shelter_listDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        Shelter_listDTO shelter_listDTO = new Shelter_listDTO();

        ArrayList<Shelter_listDTO> list = new ArrayList<Shelter_listDTO>();

        BufferedReader in = new BufferedReader(new FileReader("/Users/yungyeomji/Downloads/shelter_list.csv"));

        String str;
        while(true) {
            str = in.readLine();
            if(str == null) break;

            StringTokenizer tokens = new StringTokenizer(str,",");

            while(tokens.hasMoreElements()) {
                shelter_listDTO.setShelter_name(tokens.nextToken());
                shelter_listDTO.setShelter_phone(tokens.nextToken());
                tokens.nextToken(); //관할구역 넘김
                shelter_listDTO.setShelter_type(tokens.nextToken());
                String div_address = tokens.nextToken(); // 주소분할
                String[] addressArr = div_address.split(" ", 3);
                shelter_listDTO.setShelter_county(addressArr[0]);
                shelter_listDTO.setShelter_city(addressArr[1]);
                shelter_listDTO.setShelter_address(addressArr[2]);

                //시간
                boolean open = tokens.hasMoreElements();
                if(open == true){
                    shelter_listDTO.setShelter_open_time(tokens.nextToken());
                }
                else{
                    shelter_listDTO.setShelter_open_time(null);
                }
                boolean close = tokens.hasMoreElements();
                if(close == true){
                    shelter_listDTO.setShelter_close_time(tokens.nextToken());
                }
                else{
                    shelter_listDTO.setShelter_close_time(null);
                }



//                shelter_listDTO.setShelter_open_time(tokens.nextToken());
//                shelter_listDTO.setShelter_close_time(tokens.nextToken());

                shelter_listDAO.insert_Shelter_list(shelter_listDTO);

                System.out.println(shelter_listDTO);

                list.add(shelter_listDTO);
            }
        }



    }
}
