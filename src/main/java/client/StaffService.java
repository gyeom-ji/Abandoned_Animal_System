package client;

import DB.dto.*;
import client.LoginService;
import network.Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class StaffService implements LoginService {
    public static Scanner scanner = new Scanner(System.in);

    private static InputStream is;
    private static OutputStream os;

    public StaffService(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
    }

    private static Protocol read() throws IOException {
        byte[] header = new byte[Protocol.LEN_HEADER];
        Protocol pt = new Protocol();
        int totalReceived = 0;
        int readSize;

        is.read(header, 0, Protocol.LEN_HEADER);
        pt.setHeader(header);

        byte[] buf = new byte[pt.getBodyLength()];
        while (totalReceived < pt.getBodyLength()) {
            readSize = is.read(buf, totalReceived, pt.getBodyLength() - totalReceived);
            totalReceived += readSize;
        }
        pt.setBody(buf);
        return pt;
    }

    @Override
    public void run() throws Exception {
        int menu = 0;
        boolean exit = true;
        Popup();
        while (exit) {

            System.out.println("[1]유기공고 생성  [2]보호소 리스트 생성  [3]내 정보 조회 [4]실종동물 공고 조회" +
                    "\n[5]유기동물 공고 조회  [6]유기동물보호소 조회  [7]신청서 조회 [8]내 정보 수정 [9] 유기공고 수정" +
                    "[10]신청서 승인 [11]보호소 리스트 삭제 [12]유기공고 삭제 [13]상품 조회 [14]백신 조회 [15]로그아웃");
            menu = scanner.nextInt();

            switch (menu) {
                case 1:
                    createAbandoned_notice();
                    break;
                case 2:
                    createShelter_list();
                    break;
                case 3:
                    readStaff();
                    break;
                case 4:
                    readMissing_notice();
                    break;
                case 5:
                    readAbandoned_notice();
                    break;
                case 6:
                    readShelter_list();
                    break;
                case 7:
                    readForm();
                    break;
                case 8:
                    updateStaff();
                    break;
                case 9:
                    updateAbandoned_notice();
                    break;
                case 10:
                    approvalForm();
                    break;
                case 11:
                    deleteShelter_list();
                    break;
                case 12:
                    deleteAbandoned_notice();
                    break;
                case 13:
                    readMaterials();
                    break;
                case 14:
                    readVaccine();
                    break;
                case 15:
                    logout();
                    exit = false;
                    break;
                default:
                    System.out.println("잘못된 번호를 입력하셨습니다");
                    break;
            }
        }
    }

    private void readVaccine() throws Exception {
        VaccineDTO[] vaccineDTO;
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        sendPt.setCode(Protocol.T1_CODE_READ);
        sendPt.setEntity(Protocol.ENTITY_VACCINE);
        sendPt.send(os);

        Protocol recvPt = read();
        if (recvPt != null) {
            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                    vaccineDTO = (VaccineDTO[]) recvPt.getObjectArray();
                    for (int i = 0; i < vaccineDTO.length; i++) {
                        System.out.println("pk : " + vaccineDTO[i].getVaccine_pk());
                        System.out.println("name : " + vaccineDTO[i].getVaccine_name());
                        System.out.println("basic : " + vaccineDTO[i].getVaccine_basic());
                        System.out.println("target age : " + vaccineDTO[i].getVaccine_target_age());
                        System.out.println("comment : " + vaccineDTO[i].getVaccine_comment());
                        System.out.println("period : " + vaccineDTO[i].getVaccine_period());
                        System.out.println("animal kind : " + vaccineDTO[i].getVaccine_animal_kind());
                        System.out.println("animal breed : " + vaccineDTO[i].getVaccine_animal_breed());
                        System.out.println("animal age : " + vaccineDTO[i].getVaccine_animal_age());
                    }
                } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                    System.out.println("정보가 없습니다.");
            }
        }
    }

    private void readMaterials() throws Exception {
        Recommend_materialsDTO[] recommend_materialsDTO;
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        sendPt.setCode(Protocol.T1_CODE_READ);
        sendPt.setEntity(Protocol.ENTITY_MATERIALS);
        sendPt.send(os);

        Protocol recvPt = read();
        if (recvPt != null) {
            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                    recommend_materialsDTO = (Recommend_materialsDTO[]) recvPt.getObjectArray();
                    for (int i = 0; i < recommend_materialsDTO.length; i++) {
                        System.out.println("pk : " + recommend_materialsDTO[i].getRecommend_materials_pk());
                        System.out.println("name : " + recommend_materialsDTO[i].getMaterials_name());
                        System.out.println("type : " + recommend_materialsDTO[i].getMaterials_type());
                        System.out.println("url : " + recommend_materialsDTO[i].getMaterials_url());
                        System.out.println("feature : " + recommend_materialsDTO[i].getMaterials_feature());
                        System.out.println("img : " + recommend_materialsDTO[i].getMaterials_img());
                        System.out.println("animal kind : " + recommend_materialsDTO[i].getMaterials_animal_kind());
                        System.out.println("animal breed : " + recommend_materialsDTO[i].getMaterials_animal_breed());
                        System.out.println("animal age : " + recommend_materialsDTO[i].getMaterials_animal_age());
                    }
                } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                    System.out.println("정보가 없습니다.");
            }
        }
    }

    private void createAbandoned_notice() throws IllegalAccessException, IOException {
        Abandoned_noticeDTO abandoned_noticeDTO = new Abandoned_noticeDTO();
        AnimalDTO animalDTO = new AnimalDTO();
        Shelter_listDTO shelter_listDTO = new Shelter_listDTO();
        List<AnimalDTO> animalDTOList = new ArrayList<>(1);
        List<Shelter_listDTO> shelter_listDTOList = new ArrayList<>(1);
        String a = scanner.nextLine();
        System.out.println("animal_kind");
        String animal_kind = scanner.nextLine();
        System.out.println("animal_sex");
        String animal_sex = scanner.nextLine();
        System.out.println("animal_age");
        String animal_age = scanner.nextLine();
        System.out.println("animal_color");
        String animal_color = scanner.nextLine();
        System.out.println("animal_feature");
        String animal_feature = scanner.nextLine();
        System.out.println("animal_breed");
        String animal_breed = scanner.nextLine();
        System.out.println("animal_neuter");
        String animal_neuter = scanner.nextLine();
        System.out.println("animal_img");
        String animal_img = scanner.nextLine();
        System.out.println("shelter_name");
        String shelter_name = scanner.nextLine();
        System.out.println("shelter_phone");
        String shelter_phone = scanner.nextLine();
        System.out.println("abandoned_notice_num");
        String abandoned_notice_num = scanner.nextLine();
        System.out.println("abandoned_receipt_date");
        String abandoned_receipt_date = scanner.nextLine();
        System.out.println("abandoned_place");
        String abandoned_place = scanner.nextLine();
        animalDTO.setAnimal_kind(animal_kind);
        animalDTO.setAnimal_sex(animal_sex);
        animalDTO.setAnimal_age(animal_age);
        animalDTO.setAnimal_color(animal_color);
        animalDTO.setAnimal_feature(animal_feature);
        animalDTO.setAnimal_breed(animal_breed);
        animalDTO.setAnimal_neuter(animal_neuter);
        animalDTO.setAnimal_img(animal_img);
        shelter_listDTO.setShelter_name(shelter_name);
        shelter_listDTO.setShelter_phone(shelter_phone);
        animalDTOList.add(animalDTO);
        abandoned_noticeDTO.setAnimalDTOList(animalDTOList);
        shelter_listDTOList.add(shelter_listDTO);
        abandoned_noticeDTO.setShelter_listDTOList(shelter_listDTOList);
        abandoned_noticeDTO.setAbandoned_notice_num(abandoned_notice_num);
        abandoned_noticeDTO.setAbandoned_receipt_date(abandoned_receipt_date);
        abandoned_noticeDTO.setAbandoned_place(abandoned_place);

        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        sendPt.setObject(abandoned_noticeDTO);
        sendPt.setCode(Protocol.T1_CODE_CREATE);
        sendPt.setEntity(Protocol.ENTITY_ABANDONED_ANIMAL_NOTICE);
        sendPt.send(os);

        Protocol recvPt = read();
        if (recvPt != null) {
            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS)
                    System.out.println("성공");
                else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                    System.out.println("실패");
            }
        }
    }

    private void createShelter_list() throws IllegalAccessException, IOException {
        Shelter_listDTO shelter_listDTO = new Shelter_listDTO();

        String a = scanner.nextLine();
        System.out.println("shelter_name");
        String shelter_name = scanner.nextLine();
        System.out.println("shelter_phone");
        String shelter_phone = scanner.nextLine();
        System.out.println("shelter_county");
        String shelter_county = scanner.nextLine();
        System.out.println("shelter_city");
        String shelter_city = scanner.nextLine();
        System.out.println("shelter_address");
        String shelter_address = scanner.nextLine();
        System.out.println("shelter_type");
        String shelter_type = scanner.nextLine();
        System.out.println("shelter_open_time");
        String shelter_open_time = scanner.nextLine();
        System.out.println("shelter_close_time");
        String shelter_close_time = scanner.nextLine();
        shelter_listDTO.setShelter_name(shelter_name);
        shelter_listDTO.setShelter_phone(shelter_phone);
        shelter_listDTO.setShelter_county(shelter_county);
        shelter_listDTO.setShelter_city(shelter_city);
        shelter_listDTO.setShelter_address(shelter_address);
        shelter_listDTO.setShelter_type(shelter_type);
        shelter_listDTO.setShelter_open_time(shelter_open_time);
        shelter_listDTO.setShelter_close_time(shelter_close_time);

        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        sendPt.setObject(shelter_listDTO);
        sendPt.setCode(Protocol.T1_CODE_CREATE);
        sendPt.setEntity(Protocol.ENTITY_SHELTER_LIST);
        sendPt.send(os);

        Protocol recvPt = read();
        if (recvPt != null) {
            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS)
                    System.out.println("성공");
                else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                    System.out.println("실패");
            }
        }
    }

    private void readStaff() throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        String a = scanner.nextLine();
        System.out.println("roll id");
        String id = scanner.nextLine();
        RollDTO rollDTO = new RollDTO(id);
        sendPt.setObject(rollDTO);
        sendPt.setCode(Protocol.T1_CODE_READ);
        sendPt.setEntity(Protocol.ENTITY_STAFF);
        sendPt.send(os);

        Protocol recvPt = read();
        if (recvPt != null) {
            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                    rollDTO = (RollDTO) recvPt.getObject();
                    System.out.println("id : " + rollDTO.getRoll_id());
                    System.out.println("name : " + rollDTO.getRoll_name());
                    System.out.println("phone : " + rollDTO.getRoll_phone());

                } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                    System.out.println("정보가 없습니다.");
            }
        }
    }

    private void readMissing_notice() throws IOException, IllegalAccessException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        int menu = 0;
        Missing_noticeDTO[] missing_noticeDTO;
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        System.out.println("[1] 전체 조회 [2] 지역별 조회 [3] 뒤로가기");
        menu = scanner.nextInt();
        while (menu != 3) {
            if (menu == 1) {
                sendPt.setCode(Protocol.T1_CODE_READ);
                sendPt.setReadOption(Protocol.READ_ALL);
                sendPt.setEntity(Protocol.ENTITY_MISSING_NOTICE);
                sendPt.send(os);

                Protocol recvPt = read();
                if (recvPt != null) {
                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                            missing_noticeDTO = (Missing_noticeDTO[]) recvPt.getObjectArray();
                            for (int i = 0; i < missing_noticeDTO.length; i++) {  System.out.println("animal pk : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_pk());
                                System.out.println("animal kind : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_kind());
                                System.out.println("animal sex : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_sex());
                                System.out.println("animal age : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_age());
                                System.out.println("animal color : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_color());
                                System.out.println("animal feature : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_feature());
                                System.out.println("animal breed : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_breed());
                                System.out.println("animal neuter : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_neuter());
                                System.out.println("animal img : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_img());
                                System.out.println("missing notice pk : " + missing_noticeDTO[i].getMissing_notice_pk());
                                System.out.println("missing notice person name : " + missing_noticeDTO[i].getMissing_person_name());
                                System.out.println("missing notice animal name : " + missing_noticeDTO[i].getMissing_animal_name());
                                System.out.println("missing notice email : " + missing_noticeDTO[i].getMissing_email());
                                System.out.println("missing notice phone : " + missing_noticeDTO[i].getMissing_phone());
                                System.out.println("missing notice date : " + missing_noticeDTO[i].getMissing_date());
                                System.out.println("missing notice county : " + missing_noticeDTO[i].getMissing_county());
                                System.out.println("missing notice city : " + missing_noticeDTO[i].getMissing_city());
                                System.out.println("missing notice address : " + missing_noticeDTO[i].getMissing_address());
                            }
                        } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                            System.out.println("정보가 없습니다.");
                    }
                }
                break;
            } else if (menu == 2) {
                String option[] = new String[2];
                String a = scanner.nextLine();
                System.out.println("county 입력 : ");
                option[0] = scanner.nextLine();
                System.out.println("city 입력 : ");
                option[1] = scanner.nextLine();
                Shelter_listDTO shelter_listDTO = new Shelter_listDTO();
                Missing_noticeDTO missing_noticeDTO1 = new Missing_noticeDTO();
                missing_noticeDTO1.setMissing_county(option[0]);
                missing_noticeDTO1.setMissing_city(option[1]);
                sendPt.setObject(missing_noticeDTO1);
                sendPt.setCode(Protocol.T1_CODE_READ);
                sendPt.setReadOption(Protocol.READ_BY_OPTION);
                sendPt.setEntity(Protocol.ENTITY_MISSING_NOTICE);
                sendPt.send(os);

                Protocol recvPt = read();
                if (recvPt != null) {
                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                            missing_noticeDTO = (Missing_noticeDTO[]) recvPt.getObjectArray();
                            for (int i = 0; i < missing_noticeDTO.length; i++) {
                                System.out.println("animal pk : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_pk());
                                System.out.println("animal kind : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_kind());
                                System.out.println("animal sex : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_sex());
                                System.out.println("animal age : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_age());
                                System.out.println("animal color : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_color());
                                System.out.println("animal feature : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_feature());
                                System.out.println("animal breed : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_breed());
                                System.out.println("animal neuter : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_neuter());
                                System.out.println("animal img : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_img());
                                System.out.println("missing notice pk : " + missing_noticeDTO[i].getMissing_notice_pk());
                                System.out.println("missing notice person name : " + missing_noticeDTO[i].getMissing_person_name());
                                System.out.println("missing notice animal name : " + missing_noticeDTO[i].getMissing_animal_name());
                                System.out.println("missing notice email : " + missing_noticeDTO[i].getMissing_email());
                                System.out.println("missing notice phone : " + missing_noticeDTO[i].getMissing_phone());
                                System.out.println("missing notice date : " + missing_noticeDTO[i].getMissing_date());
                                System.out.println("missing notice county : " + missing_noticeDTO[i].getMissing_county());
                                System.out.println("missing notice city : " + missing_noticeDTO[i].getMissing_city());
                                System.out.println("missing notice address : " + missing_noticeDTO[i].getMissing_address());
                            }
                        } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                            System.out.println("정보가 없습니다.");
                    }
                }
                break;
            } else System.out.println("잘못 입력 하셨습니다.");
        }
    }

    private void readAbandoned_notice() throws Exception {
        Abandoned_noticeDTO[] abandoned_noticeDTO;
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        System.out.println("[1] 전체 조회 [2] 지역별 조회 [3] 뒤로가기");
        int menu = scanner.nextInt();
        while (menu != 3) {
            if (menu == 1) {
                sendPt.setCode(Protocol.T1_CODE_READ);
                sendPt.setReadOption(Protocol.READ_ALL);
                sendPt.setEntity(Protocol.ENTITY_ABANDONED_ANIMAL_NOTICE);
                sendPt.send(os);

                Protocol recvPt = read();
                if (recvPt != null) {
                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                            abandoned_noticeDTO = (Abandoned_noticeDTO[]) recvPt.getObjectArray();
                            for (int i = 0; i < abandoned_noticeDTO.length; i++) {
                                System.out.println("animal pk : " + abandoned_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_pk());
                                System.out.println("animal kind : " + abandoned_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_kind());
                                System.out.println("animal sex : " + abandoned_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_sex());
                                System.out.println("animal age : " + abandoned_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_age());
                                System.out.println("animal color : " + abandoned_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_color());
                                System.out.println("animal feature : " + abandoned_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_feature());
                                System.out.println("animal breed : " + abandoned_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_breed());
                                System.out.println("animal neuter : " + abandoned_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_neuter());
                                System.out.println("animal img : " + abandoned_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_img());
                                System.out.println("abandoned notice pk : " + abandoned_noticeDTO[i].getAbandoned_notice_pk());
                                System.out.println("abandoned notice num : " + abandoned_noticeDTO[i].getAbandoned_notice_num());
                                System.out.println("abandoned notice receipt date : " + abandoned_noticeDTO[i].getAbandoned_receipt_date());
                                System.out.println("abandoned notice place : " + abandoned_noticeDTO[i].getAbandoned_place());
                                System.out.println("shelter name : " + abandoned_noticeDTO[i].getShelter_listDTOList().get(0).getShelter_name());
                                System.out.println("shelter phone : " + abandoned_noticeDTO[i].getShelter_listDTOList().get(0).getShelter_phone());
                                System.out.println("shelter county : " + abandoned_noticeDTO[i].getShelter_listDTOList().get(0).getShelter_county());
                                System.out.println("shelter city : " + abandoned_noticeDTO[i].getShelter_listDTOList().get(0).getShelter_city());
                                System.out.println("shelter address : " + abandoned_noticeDTO[i].getShelter_listDTOList().get(0).getShelter_address());
                                System.out.println("shelter type : " + abandoned_noticeDTO[i].getShelter_listDTOList().get(0).getShelter_type());
                                System.out.println("shelter open time : " + abandoned_noticeDTO[i].getShelter_listDTOList().get(0).getShelter_open_time());
                                System.out.println("shelter close time : " + abandoned_noticeDTO[i].getShelter_listDTOList().get(0).getShelter_close_time());
                            }
                        } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                            System.out.println("정보가 없습니다.");
                    }
                }
                break;
            } else if (menu == 2) {
                List<Shelter_listDTO> shelter_listDTOList = new ArrayList<>(1);
                String a = scanner.nextLine();
                String option[] = new String[2];
                System.out.println("county 입력 : ");
                option[0] = scanner.nextLine();
                System.out.println("city 입력 : ");
                option[1] = scanner.nextLine();
                Shelter_listDTO shelter_listDTO = new Shelter_listDTO();
                shelter_listDTO.setShelter_county(option[0]);
                shelter_listDTO.setShelter_city(option[1]);
                shelter_listDTOList.add(shelter_listDTO);
                Abandoned_noticeDTO abandoned_noticeDTO1 = new Abandoned_noticeDTO();
                abandoned_noticeDTO1.setShelter_listDTOList(shelter_listDTOList);
                sendPt.setObject(abandoned_noticeDTO1);
                sendPt.setCode(Protocol.T1_CODE_READ);
                sendPt.setReadOption(Protocol.READ_BY_OPTION);
                sendPt.setEntity(Protocol.ENTITY_ABANDONED_ANIMAL_NOTICE);
                sendPt.send(os);

                Protocol recvPt = read();
                if (recvPt != null) {
                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                            abandoned_noticeDTO = (Abandoned_noticeDTO[]) recvPt.getObjectArray();
                            for (int i = 0; i < abandoned_noticeDTO.length; i++) {
                                System.out.println("animal pk : " + abandoned_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_pk());
                                System.out.println("animal kind : " + abandoned_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_kind());
                                System.out.println("animal sex : " + abandoned_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_sex());
                                System.out.println("animal age : " + abandoned_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_age());
                                System.out.println("animal color : " + abandoned_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_color());
                                System.out.println("animal feature : " + abandoned_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_feature());
                                System.out.println("animal breed : " + abandoned_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_breed());
                                System.out.println("animal neuter : " + abandoned_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_neuter());
                                System.out.println("animal img : " + abandoned_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_img());
                                System.out.println("abandoned notice pk : " + abandoned_noticeDTO[i].getAbandoned_notice_pk());
                                System.out.println("abandoned notice num : " + abandoned_noticeDTO[i].getAbandoned_notice_num());
                                System.out.println("abandoned notice receipt date : " + abandoned_noticeDTO[i].getAbandoned_receipt_date());
                                System.out.println("abandoned notice place : " + abandoned_noticeDTO[i].getAbandoned_place());
                                System.out.println("shelter name : " + abandoned_noticeDTO[i].getShelter_listDTOList().get(0).getShelter_name());
                                System.out.println("shelter phone : " + abandoned_noticeDTO[i].getShelter_listDTOList().get(0).getShelter_phone());
                                System.out.println("shelter county : " + abandoned_noticeDTO[i].getShelter_listDTOList().get(0).getShelter_county());
                                System.out.println("shelter city : " + abandoned_noticeDTO[i].getShelter_listDTOList().get(0).getShelter_city());
                                System.out.println("shelter address : " + abandoned_noticeDTO[i].getShelter_listDTOList().get(0).getShelter_address());
                                System.out.println("shelter type : " + abandoned_noticeDTO[i].getShelter_listDTOList().get(0).getShelter_type());
                                System.out.println("shelter open time : " + abandoned_noticeDTO[i].getShelter_listDTOList().get(0).getShelter_open_time());
                                System.out.println("shelter close time : " + abandoned_noticeDTO[i].getShelter_listDTOList().get(0).getShelter_close_time());
                            }
                            break;
                        } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                            System.out.println("정보가 없습니다.");
                    }
                }
            }
        }
    }

    private void readShelter_list() throws Exception {
        Shelter_listDTO[] shelter_listDTO;
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        System.out.println("[1] 전체 조회 [2] 지역별 조회 [3] 뒤로가기");
        int menu = scanner.nextInt();
        while (menu != 3) {
            if (menu == 1) {
                sendPt.setCode(Protocol.T1_CODE_READ);
                sendPt.setReadOption(Protocol.READ_ALL);
                sendPt.setEntity(Protocol.ENTITY_SHELTER_LIST);
                sendPt.send(os);

                Protocol recvPt = read();
                if (recvPt != null) {
                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                            shelter_listDTO = (Shelter_listDTO[]) recvPt.getObjectArray();
                            for (int i = 0; i < shelter_listDTO.length; i++) {
                                System.out.println("pk : " + shelter_listDTO[i].getShelter_list_pk());
                                System.out.println("name : " + shelter_listDTO[i].getShelter_name());
                                System.out.println("phone : " + shelter_listDTO[i].getShelter_phone());
                                System.out.println("county : " + shelter_listDTO[i].getShelter_county());
                                System.out.println("city : " + shelter_listDTO[i].getShelter_city());
                                System.out.println("address : " + shelter_listDTO[i].getShelter_address());
                                System.out.println("type : " + shelter_listDTO[i].getShelter_type());
                                System.out.println("open time : " + shelter_listDTO[i].getShelter_open_time());
                                System.out.println("close time : " + shelter_listDTO[i].getShelter_close_time());
                            }
                        } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                            System.out.println("정보가 없습니다.");
                    }
                }
                break;
            } else if (menu == 2) {
                String option[] = new String[2];
                String a = scanner.nextLine();
                System.out.println("county 입력 : ");
                option[0] = scanner.nextLine();
                System.out.println("city 입력 : ");
                option[1] = scanner.nextLine();
                Shelter_listDTO shelter_listDTO1 = new Shelter_listDTO();
                shelter_listDTO1.setShelter_county(option[0]);
                shelter_listDTO1.setShelter_city(option[1]);
                sendPt.setObject(shelter_listDTO1);
                sendPt.setCode(Protocol.T1_CODE_READ);
                sendPt.setReadOption(Protocol.READ_BY_OPTION);
                sendPt.setEntity(Protocol.ENTITY_SHELTER_LIST);
                sendPt.send(os);

                Protocol recvPt = read();
                if (recvPt != null) {
                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                            shelter_listDTO = (Shelter_listDTO[]) recvPt.getObjectArray();
                            for (int i = 0; i < shelter_listDTO.length; i++) {
                                System.out.println("pk : " + shelter_listDTO[i].getShelter_list_pk());
                                System.out.println("name : " + shelter_listDTO[i].getShelter_name());
                                System.out.println("phone : " + shelter_listDTO[i].getShelter_phone());
                                System.out.println("county : " + shelter_listDTO[i].getShelter_county());
                                System.out.println("city : " + shelter_listDTO[i].getShelter_city());
                                System.out.println("address : " + shelter_listDTO[i].getShelter_address());
                                System.out.println("type : " + shelter_listDTO[i].getShelter_type());
                                System.out.println("open time : " + shelter_listDTO[i].getShelter_open_time());
                                System.out.println("close time : " + shelter_listDTO[i].getShelter_close_time());
                            }
                        } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                            System.out.println("정보가 없습니다.");
                    }
                }
                break;
            }
        }
    }

    private void readForm() throws Exception {
        FormDTO[] formDTO;
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        String a = scanner.nextLine();
        System.out.println("보호소 이름 입력 : ");
        String name = scanner.nextLine();
        Shelter_listDTO shelter_listDTO = new Shelter_listDTO();
        shelter_listDTO.setShelter_name(name);
        sendPt.setObject(shelter_listDTO);
        sendPt.setCode(Protocol.T1_CODE_READ);
        sendPt.setEntity(Protocol.ENTITY_FORM);
        sendPt.send(os);

        Protocol recvPt = read();
        if (recvPt != null) {
            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                    formDTO = (FormDTO[]) recvPt.getObjectArray();
                    for (int i = 0; i < formDTO.length; i++) {
                        System.out.println("form_pk : " + formDTO[i].getForm_pk());
                        System.out.println("form_type : " + formDTO[i].getForm_type());
                        System.out.println("form_approval : " + formDTO[i].getForm_approval());
//                        System.out.println("roll_pk : " + formDTO[i].getRoll_pk());
//                        System.out.println("roll_id : " + formDTO[i].getRollDTO().getRoll_id());
//                        System.out.println("roll_pw : " + formDTO[i].getRollDTO().getRoll_pw());
//                        System.out.println("roll_name : " + formDTO[i].getRollDTO().getRoll_name());
//                        System.out.println("roll_phone : " + formDTO[i].getRollDTO().getRoll_phone());
//                        System.out.println("roll_type : " + formDTO[i].getRollDTO().getRoll_type());
//                        System.out.println("shelter_list_pk : " + formDTO[i].getAbandoned_noticeDTO().getShelter_listDTO().getShelter_list_pk());
//                        System.out.println("shelter_name : " + formDTO[i].getShelter_listDTO().getShelter_name());
//                        System.out.println("shelter_phone : " + formDTO[i].getShelter_listDTO().getShelter_phone());
//                        System.out.println("shelter_county : " + formDTO[i].getShelter_listDTO().getShelter_county());
//                        System.out.println("shelter_city : " + formDTO[i].getShelter_listDTO().getShelter_city());
//                        System.out.println("shelter_address : " + formDTO[i].getShelter_listDTO().getShelter_address());
//                        System.out.println("shelter_type : " + formDTO[i].getShelter_listDTO().getShelter_type());
//                        System.out.println("shelter_open_time : " + formDTO[i].getShelter_listDTO().getShelter_open_time());
//                        System.out.println("shelter_close_time : " + formDTO[i].getShelter_listDTO().getShelter_close_time());
//                        System.out.println("abandoned_notice_pk : " + formDTO[i].getAbandoned_notice_pk());
//                        System.out.println("abandoned_animal_pk : " + formDTO[i].getAbandoned_noticeDTO().getAbandoned_animal_pk());
//                        System.out.println("abandoned_notice_num : " + formDTO[i].getAbandoned_noticeDTO().getAbandoned_notice_num());
//                        System.out.println("abandoned_receipt_date : " + formDTO[i].getAbandoned_noticeDTO().getAbandoned_receipt_date());
//                        System.out.println("abandoned_place : " + formDTO[i].getAbandoned_noticeDTO().getAbandoned_place());
                    }
                } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                    System.out.println("정보가 없습니다.");
            }
        }

    }

    private void updateStaff() throws IllegalAccessException, IOException {
        RollDTO rollDTO = new RollDTO();

        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);

        System.out.println("[1] 전화번호, 이름 수정, [2] 비밀번호 수정, [3] 나가기");
        int menu = scanner.nextInt();
        while (menu != 3) {
            if (menu == 1) {
                String a = scanner.nextLine();
                System.out.println("id : ");
                String id = scanner.nextLine();
                System.out.println("전화번호 : ");
                String num = scanner.nextLine();
                System.out.println("이름 : ");
                String name = scanner.nextLine();
                rollDTO.setRoll_id(id);
                rollDTO.setRoll_name(name);
                rollDTO.setRoll_phone(num);

                sendPt.setObject(rollDTO);
                sendPt.setCode(Protocol.T1_CODE_UPDATE);
                sendPt.setReadOption(Protocol.UPDATE_ROLL);
                sendPt.setEntity(Protocol.ENTITY_STAFF);
                sendPt.send(os);

                Protocol recvPt = read();
                if (recvPt != null) {
                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS)
                            System.out.println("성공");
                        else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                            System.out.println("실패");
                    }
                }
                break;
            } else if (menu == 2) {
                String a = scanner.nextLine();
                System.out.println("id : ");
                String id = scanner.nextLine();
                System.out.println("비밀번호 : ");
                String num = scanner.nextLine();
                rollDTO.setRoll_pw(num);
                rollDTO.setRoll_id(id);

                sendPt.setObject(rollDTO);
                sendPt.setCode(Protocol.T1_CODE_UPDATE);
                sendPt.setReadOption(Protocol.UPDATE_PASSWORD);
                sendPt.setEntity(Protocol.ENTITY_STAFF);
                sendPt.send(os);

                Protocol recvPt = read();
                if (recvPt != null) {
                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS)
                            System.out.println("성공");
                        else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                            System.out.println("실패");
                    }
                }
                break;
            }
        }
    }

    private void updateAbandoned_notice() throws IllegalAccessException, IOException {
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        String a = scanner.nextLine();
        System.out.println("abandoned_notice_num : ");
        String abandoned_notice_num = scanner.nextLine();
        System.out.println("abandoned_receipt_date : ");
        String abandoned_receipt_date = scanner.nextLine();
        System.out.println("abandoned_notice_place : ");
        String abandoned_notice_place = scanner.nextLine();
        Abandoned_noticeDTO abandoned_noticeDTO = new Abandoned_noticeDTO();
        abandoned_noticeDTO.setAbandoned_notice_num(abandoned_notice_num);
        abandoned_noticeDTO.setAbandoned_receipt_date(abandoned_receipt_date);
        abandoned_noticeDTO.setAbandoned_place(abandoned_notice_place);

        sendPt.setObject(abandoned_noticeDTO);
        sendPt.setCode(Protocol.T1_CODE_UPDATE);
        sendPt.setEntity(Protocol.ENTITY_ABANDONED_ANIMAL_NOTICE);
        sendPt.send(os);

        Protocol recvPt = read();
        if (recvPt != null) {
            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS)
                    System.out.println("성공");
                else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                    System.out.println("실패");
            }
        }

    }

    private void approvalForm() throws IllegalAccessException, IOException {
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        String a = scanner.nextLine();
        System.out.println("form_pk : ");
        long form_pk = scanner.nextLong();
        String b = scanner.nextLine();
        System.out.println("form_approval : ");
        String form_approval = scanner.nextLine();
        FormDTO formDTO = new FormDTO();
        formDTO.setForm_pk(form_pk);
        formDTO.setForm_approval(form_approval);

        sendPt.setObject(formDTO);
        sendPt.setCode(Protocol.T1_CODE_UPDATE);
        sendPt.setEntity(Protocol.ENTITY_FORM_APPROVAL);
        sendPt.send(os);

        Protocol recvPt = read();
        if (recvPt != null) {
            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS)
                    System.out.println("성공");
                else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                    System.out.println("실패");
            }
        }
    }

    private void deleteShelter_list() throws IllegalAccessException, IOException {
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        String a = scanner.nextLine();
        System.out.println("pk : ");
        long pk = scanner.nextLong();
        Shelter_listDTO shelter_listDTO = new Shelter_listDTO();
        shelter_listDTO.setShelter_list_pk(pk);

        sendPt.setObject(shelter_listDTO);
        sendPt.setCode(Protocol.T1_CODE_DELETE);
        sendPt.setEntity(Protocol.ENTITY_SHELTER_LIST);
        sendPt.send(os);

        Protocol recvPt = read();
        if (recvPt != null) {
            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS)
                    System.out.println("성공");
                else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                    System.out.println("실패");
            }
        }

    }

    private void deleteAbandoned_notice() throws IllegalAccessException, IOException {
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        String a = scanner.nextLine();
        System.out.println("notice num : ");
        String pk = scanner.nextLine();

        Abandoned_noticeDTO abandoned_noticeDTO = new Abandoned_noticeDTO();
        abandoned_noticeDTO.setAbandoned_notice_num(pk);

        sendPt.setObject(abandoned_noticeDTO);
        sendPt.setCode(Protocol.T1_CODE_DELETE);
        sendPt.setEntity(Protocol.ENTITY_ABANDONED_ANIMAL_NOTICE);
        sendPt.send(os);

        Protocol recvPt = read();
        if (recvPt != null) {
            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS)
                    System.out.println("성공");
                else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                    System.out.println("실패");
            }
        }

    }

    private void logout() throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        sendPt.setCode(Protocol.T1_CODE_LOGOUT);
        sendPt.send(os);

        Protocol recvPt = read();
        if (recvPt != null) {
            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS)
                    System.out.println("성공");
                else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                    System.out.println("실패");
            }
        }
    }

    private void Popup() throws  Exception{
        Random random = new Random();

        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        String option[] = new String[2];
        System.out.println("county 입력 : ");
        option[0] = scanner.nextLine();
        System.out.println("city 입력 : ");
        option[1] = scanner.nextLine();
        Missing_noticeDTO missing_noticeDTO1 = new Missing_noticeDTO();
        missing_noticeDTO1.setMissing_county(option[0]);
        missing_noticeDTO1.setMissing_city(option[1]);
        sendPt.setCode(Protocol.T1_CODE_READ);
        sendPt.setObject(missing_noticeDTO1);
        sendPt.setReadOption(Protocol.READ_BY_OPTION);
        sendPt.setEntity(Protocol.ENTITY_MISSING_NOTICE);
        sendPt.send(os);


        Protocol recvPt = read();
        if (recvPt != null) {
            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                    Missing_noticeDTO[] missing_noticeDTO = (Missing_noticeDTO[]) recvPt.getObjectArray();
                    int i = random.nextInt(missing_noticeDTO.length);

                    System.out.println("animal pk : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_pk());
                    System.out.println("animal kind : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_kind());
                    System.out.println("animal sex : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_sex());
                    System.out.println("animal age : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_age());
                    System.out.println("animal color : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_color());
                    System.out.println("animal feature : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_feature());
                    System.out.println("animal breed : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_breed());
                    System.out.println("animal neuter : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_neuter());
                    System.out.println("animal img : " + missing_noticeDTO[i].getAnimalDTOList().get(0).getAnimal_img());
                    System.out.println("missing notice pk : " + missing_noticeDTO[i].getMissing_notice_pk());
                    System.out.println("missing notice person name : " + missing_noticeDTO[i].getMissing_person_name());
                    System.out.println("missing notice animal name : " + missing_noticeDTO[i].getMissing_animal_name());
                    System.out.println("missing notice email : " + missing_noticeDTO[i].getMissing_email());
                    System.out.println("missing notice phone : " + missing_noticeDTO[i].getMissing_phone());
                    System.out.println("missing notice date : " + missing_noticeDTO[i].getMissing_date());
                    System.out.println("missing notice county : " + missing_noticeDTO[i].getMissing_county());
                    System.out.println("missing notice city : " + missing_noticeDTO[i].getMissing_city());
                    System.out.println("missing notice address : " + missing_noticeDTO[i].getMissing_address());
                }
            } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                System.out.println("정보가 없습니다.");
        }
    }

}