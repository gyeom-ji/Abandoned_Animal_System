package client;

import DB.dto.*;
import network.Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class AdminService implements LoginService {
    public static Scanner scanner = new Scanner(System.in);

    private static InputStream is;
    private static OutputStream os;


    public AdminService(InputStream is, OutputStream os) {
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

    public void run() throws Exception {
        int menu = 0;
        boolean exit = true;
        while (exit) {
            System.out.println("[1] 상품 생성  [2]백신 생성  [3]내 정보  [4]실종동물 공고 조회" +
                    "\n[5]유기동물 공고 조회  [6]유기동물보호소 조회  [7]상품 조회 [8]백신 조회" +
                    "\n[9]내 정보 변경 [10]상품 정보 변경 [11]백신 정보 변경 [12]백신 삭제" +
                    "\n[13]상품 삭제 [14]실종공고 삭제 [15]유기동물 공고 삭제 [16]로그아웃");
            menu = scanner.nextInt();
            switch (menu) {
                case 1:
                    createMaterials();
                    break;
                case 2:
                    createVaccine();
                    break;
                case 3:
                    readAdmin();
                    break;
                case 4:
                    readMissingNotice();
                    break;
                case 5:
                    readAbandonedAnimalNotice();
                    break;
                case 6:
                    readShelterList();
                    break;
                case 7:
                    readMaterials();
                    break;
                case 8:
                    readVaccine();
                    break;
                case 9:
                    updateAdmin();
                    break;
                case 10:
                    updateMaterials();
                    break;
                case 11:
                    updateVaccine();
                    break;
                case 12:
                    deleteVaccine();
                    break;
                case 13:
                    deleteMaterials();
                    break;
                case 14:
                    deleteMissingNotice();
                    break;
                case 15:
                    deleteAbandonedAnimalNotice();
                    break;
                case 16:
                    logout();
                    exit = false;
                    break;
                default:
                    System.out.println("잘못된 번호를 입력하셨습니다");
                    break;
            }
        }
    }

    private void createMaterials() throws Exception {
        Recommend_materialsDTO recommend_materialsDTO = new Recommend_materialsDTO();
        System.out.println("name");
        String name = scanner.nextLine();
        System.out.println("type");
        String type = scanner.nextLine();
        System.out.println("url");
        String url = scanner.nextLine();
        System.out.println("feature");
        String feature = scanner.nextLine();
        System.out.println("img");
        String img = scanner.nextLine();
        System.out.println("animal kind");
        String kind = scanner.nextLine();
        System.out.println("animal breed");
        String breed = scanner.nextLine();
        System.out.println("animal age");
        String age = scanner.nextLine();

        recommend_materialsDTO.setMaterials_name(name);
        recommend_materialsDTO.setMaterials_type(type);
        recommend_materialsDTO.setMaterials_url(url);
        recommend_materialsDTO.setMaterials_feature(feature);
        recommend_materialsDTO.setMaterials_img(img);
        recommend_materialsDTO.setMaterials_animal_kind(kind);
        recommend_materialsDTO.setMaterials_animal_breed(breed);
        recommend_materialsDTO.setMaterials_animal_age(age);
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        sendPt.setObject(recommend_materialsDTO);
        sendPt.setCode(Protocol.T1_CODE_CREATE);
        sendPt.setEntity(Protocol.ENTITY_MATERIALS);
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

    private void createVaccine() throws IllegalAccessException, IOException {
        VaccineDTO vaccineDTO = new VaccineDTO();
        System.out.println("name");
        String name = scanner.nextLine();
        System.out.println("basic");
        String basic = scanner.nextLine();
        System.out.println("target age");
        String target_age = scanner.nextLine();
        System.out.println("comment");
        String comment = scanner.nextLine();
        System.out.println("period");
        String period = scanner.nextLine();
        System.out.println("animal kind");
        String kind = scanner.nextLine();
        System.out.println("animal breed");
        String breed = scanner.nextLine();
        System.out.println("animal age");
        String age = scanner.nextLine();

        vaccineDTO.setVaccine_name(name);
        vaccineDTO.setVaccine_basic(basic);
        vaccineDTO.setVaccine_target_age(target_age);
        vaccineDTO.setVaccine_comment(comment);
        vaccineDTO.setVaccine_period(period);
        vaccineDTO.setVaccine_animal_kind(kind);
        vaccineDTO.setVaccine_animal_breed(breed);
        vaccineDTO.setVaccine_animal_age(age);
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        sendPt.setObject(vaccineDTO);
        sendPt.setCode(Protocol.T1_CODE_CREATE);
        sendPt.setEntity(Protocol.ENTITY_VACCINE);
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

    private void readAdmin() throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        System.out.println("[1] 전체 조회 [2] ID 조회 [3] 타입 조회 [4] 뒤로가기");
        int menu = scanner.nextInt();

        while (menu != 4) {
            if (menu == 1) {
                sendPt.setCode(Protocol.T1_CODE_READ);
                sendPt.setReadOption(Protocol.READ_ALL);
                sendPt.setEntity(Protocol.ENTITY_ADMIN);
                sendPt.send(os);

                Protocol recvPt = read();
                if (recvPt != null) {
                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                            RollDTO[] rollDTO = (RollDTO[]) recvPt.getObjectArray();
                            for (int i = 0; i < rollDTO.length; i++) {
                                System.out.println("id : " + rollDTO[i].getRoll_id());
                                System.out.println("name : " + rollDTO[i].getRoll_name());
                                System.out.println("phone : " + rollDTO[i].getRoll_phone());
                            }
                        } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                            System.out.println("정보가 없습니다.");
                    }
                }
            } else if (menu == 2) {
                System.out.println("ID 입력 : ");
                String id = scanner.nextLine();
                sendPt.setObject(id);
                sendPt.setCode(Protocol.T1_CODE_READ);
                sendPt.setReadOption(Protocol.READ_BY_ID);
                sendPt.setEntity(Protocol.ENTITY_ADMIN);
                sendPt.send(os);

                Protocol recvPt = read();
                if (recvPt != null) {
                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                            RollDTO rollDTO = (RollDTO) recvPt.getObject();
                            System.out.println("id : " + rollDTO.getRoll_id());
                            System.out.println("name : " + rollDTO.getRoll_name());
                            System.out.println("phone : " + rollDTO.getRoll_phone());
                        } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                            System.out.println("정보가 없습니다.");
                    }
                }
            } else if (menu == 3) {
                System.out.println("type 입력 : ");
                String type = scanner.nextLine();
                sendPt.setObject(type);
                sendPt.setCode(Protocol.T1_CODE_READ);
                sendPt.setReadOption(Protocol.READ_BY_OPTION);
                sendPt.setEntity(Protocol.ENTITY_ADMIN);
                sendPt.send(os);

                Protocol recvPt = read();
                if (recvPt != null) {
                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                            RollDTO[] rollDTO = (RollDTO[]) recvPt.getObject();
                            for (int i = 0; i < rollDTO.length; i++) {
                                System.out.println("id : " + rollDTO[i].getRoll_id());
                                System.out.println("name : " + rollDTO[i].getRoll_name());
                                System.out.println("phone : " + rollDTO[i].getRoll_phone());
                            }
                        } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                            System.out.println("정보가 없습니다.");
                    }
                }
            } else System.out.println("잘못 입력 하셨습니다.");
        }
    }

    private void readMissingNotice() throws Exception {
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
                            for (int i = 0; i < missing_noticeDTO.length; i++) {
                                System.out.println("animal pk : " + missing_noticeDTO[i].getAnimalDTO().getAnimal_pk());
                                System.out.println("animal kind : " + missing_noticeDTO[i].getAnimalDTO().getAnimal_kind());
                                System.out.println("animal sex : " + missing_noticeDTO[i].getAnimalDTO().getAnimal_sex());
                                System.out.println("animal age : " + missing_noticeDTO[i].getAnimalDTO().getAnimal_age());
                                System.out.println("animal color : " + missing_noticeDTO[i].getAnimalDTO().getAnimal_color());
                                System.out.println("animal feature : " + missing_noticeDTO[i].getAnimalDTO().getAnimal_feature());
                                System.out.println("animal breed : " + missing_noticeDTO[i].getAnimalDTO().getAnimal_breed());
                                System.out.println("animal img : " + missing_noticeDTO[i].getAnimalDTO().getAnimal_img());
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
            } else if (menu == 2) {
                String option[] = new String[2];
                System.out.println("county 입력 : ");
                option[0] = scanner.nextLine();
                System.out.println("city 입력 : ");
                option[1] = scanner.nextLine();
                sendPt.setObjectArray(option);
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
                                System.out.println("animal pk : " + missing_noticeDTO[i].getAnimalDTO().getAnimal_pk());
                                System.out.println("animal kind : " + missing_noticeDTO[i].getAnimalDTO().getAnimal_kind());
                                System.out.println("animal sex : " + missing_noticeDTO[i].getAnimalDTO().getAnimal_sex());
                                System.out.println("animal age : " + missing_noticeDTO[i].getAnimalDTO().getAnimal_age());
                                System.out.println("animal color : " + missing_noticeDTO[i].getAnimalDTO().getAnimal_color());
                                System.out.println("animal feature : " + missing_noticeDTO[i].getAnimalDTO().getAnimal_feature());
                                System.out.println("animal breed : " + missing_noticeDTO[i].getAnimalDTO().getAnimal_breed());
                                System.out.println("animal img : " + missing_noticeDTO[i].getAnimalDTO().getAnimal_img());
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
            } else System.out.println("잘못 입력 하셨습니다.");
        }
    }

    private void readAbandonedAnimalNotice() throws Exception {
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
                            abandoned_noticeDTO = (Abandoned_noticeDTO[]) recvPt.getObject();
                            for (int i = 0; i < abandoned_noticeDTO.length; i++) {
                                System.out.println("animal pk : " + abandoned_noticeDTO[i].getAnimalDTO().getAnimal_pk());
                                System.out.println("animal kind : " + abandoned_noticeDTO[i].getAnimalDTO().getAnimal_kind());
                                System.out.println("animal sex : " + abandoned_noticeDTO[i].getAnimalDTO().getAnimal_sex());
                                System.out.println("animal age : " + abandoned_noticeDTO[i].getAnimalDTO().getAnimal_age());
                                System.out.println("animal color : " + abandoned_noticeDTO[i].getAnimalDTO().getAnimal_color());
                                System.out.println("animal feature : " + abandoned_noticeDTO[i].getAnimalDTO().getAnimal_feature());
                                System.out.println("animal breed : " + abandoned_noticeDTO[i].getAnimalDTO().getAnimal_breed());
                                System.out.println("animal neuter : " + abandoned_noticeDTO[i].getAnimalDTO().getAnimal_neuter());
                                System.out.println("animal img : " + abandoned_noticeDTO[i].getAnimalDTO().getAnimal_img());
                                System.out.println("abandoned notice pk : " + abandoned_noticeDTO[i].getAbandoned_notice_pk());
                                System.out.println("abandoned notice num : " + abandoned_noticeDTO[i].getAbandoned_notice_num());
                                System.out.println("abandoned notice receipt date : " + abandoned_noticeDTO[i].getAbandoned_receipt_date());
                                System.out.println("abandoned notice place : " + abandoned_noticeDTO[i].getAbandoned_place());
                                System.out.println("shelter pk : " + abandoned_noticeDTO[i].getShelter_listDTO().getShelter_list_pk());
                                System.out.println("shelter name : " + abandoned_noticeDTO[i].getShelter_listDTO().getShelter_name())
                                ;
                                System.out.println("shelter phone : " + abandoned_noticeDTO[i].getShelter_listDTO().getShelter_phone())
                                ;
                                System.out.println("shelter county : " + abandoned_noticeDTO[i].getShelter_listDTO().getShelter_county())
                                ;
                                System.out.println("shelter city : " + abandoned_noticeDTO[i].getShelter_listDTO().getShelter_city())
                                ;
                                System.out.println("shelter address : " + abandoned_noticeDTO[i].getShelter_listDTO().getShelter_address())
                                ;
                                System.out.println("shelter type : " + abandoned_noticeDTO[i].getShelter_listDTO().getShelter_type())
                                ;
                                System.out.println("shelter open time : " + abandoned_noticeDTO[i].getShelter_listDTO().getShelter_open_time())
                                ;
                                System.out.println("shelter close time : " + abandoned_noticeDTO[i].getShelter_listDTO().getShelter_close_time())
                                ;
                            }
                        } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                            System.out.println("정보가 없습니다.");
                    }
                }
            } else if (menu == 2) {
                String option[] = new String[2];
                System.out.println("county 입력 : ");
                option[0] = scanner.nextLine();
                System.out.println("city 입력 : ");
                option[1] = scanner.nextLine();
                sendPt.setObjectArray(option);
                sendPt.setCode(Protocol.T1_CODE_READ);
                sendPt.setReadOption(Protocol.READ_BY_OPTION);
                sendPt.setEntity(Protocol.ENTITY_ABANDONED_ANIMAL_NOTICE);
                sendPt.send(os);

                Protocol recvPt = read();
                if (recvPt != null) {
                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                            abandoned_noticeDTO = (Abandoned_noticeDTO[]) recvPt.getObject();
                            for (int i = 0; i < abandoned_noticeDTO.length; i++) {
                                System.out.println("animal pk : " + abandoned_noticeDTO[i].getAnimalDTO().getAnimal_pk());
                                System.out.println("animal kind : " + abandoned_noticeDTO[i].getAnimalDTO().getAnimal_kind());
                                System.out.println("animal sex : " + abandoned_noticeDTO[i].getAnimalDTO().getAnimal_sex());
                                System.out.println("animal age : " + abandoned_noticeDTO[i].getAnimalDTO().getAnimal_age());
                                System.out.println("animal color : " + abandoned_noticeDTO[i].getAnimalDTO().getAnimal_color());
                                System.out.println("animal feature : " + abandoned_noticeDTO[i].getAnimalDTO().getAnimal_feature());
                                System.out.println("animal breed : " + abandoned_noticeDTO[i].getAnimalDTO().getAnimal_breed());
                                System.out.println("animal neuter : " + abandoned_noticeDTO[i].getAnimalDTO().getAnimal_neuter());
                                System.out.println("animal img : " + abandoned_noticeDTO[i].getAnimalDTO().getAnimal_img());
                                System.out.println("abandoned notice pk : " + abandoned_noticeDTO[i].getAbandoned_notice_pk());
                                System.out.println("abandoned notice num : " + abandoned_noticeDTO[i].getAbandoned_notice_num());
                                System.out.println("abandoned notice receipt date : " + abandoned_noticeDTO[i].getAbandoned_receipt_date());
                                System.out.println("abandoned notice place : " + abandoned_noticeDTO[i].getAbandoned_place());
                                System.out.println("shelter pk : " + abandoned_noticeDTO[i].getShelter_listDTO().getShelter_list_pk());
                                System.out.println("shelter name : " + abandoned_noticeDTO[i].getShelter_listDTO().getShelter_name());
                                System.out.println("shelter phone : " + abandoned_noticeDTO[i].getShelter_listDTO().getShelter_phone());
                                System.out.println("shelter county : " + abandoned_noticeDTO[i].getShelter_listDTO().getShelter_county());
                                System.out.println("shelter city : " + abandoned_noticeDTO[i].getShelter_listDTO().getShelter_city());
                                System.out.println("shelter address : " + abandoned_noticeDTO[i].getShelter_listDTO().getShelter_address());
                                System.out.println("shelter type : " + abandoned_noticeDTO[i].getShelter_listDTO().getShelter_type());
                                System.out.println("shelter open time : " + abandoned_noticeDTO[i].getShelter_listDTO().getShelter_open_time());
                                System.out.println("shelter close time : " + abandoned_noticeDTO[i].getShelter_listDTO().getShelter_close_time());
                            }
                        } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                            System.out.println("정보가 없습니다.");
                    }
                }
            }
        }
    }

    private void readShelterList() throws Exception {
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
                            shelter_listDTO = (Shelter_listDTO[]) recvPt.getObject();
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
            } else if (menu == 2) {
                String option[] = new String[2];
                System.out.println("county 입력 : ");
                option[0] = scanner.nextLine();
                System.out.println("city 입력 : ");
                option[1] = scanner.nextLine();
                sendPt.setObjectArray(option);
                sendPt.setCode(Protocol.T1_CODE_READ);
                sendPt.setReadOption(Protocol.READ_BY_OPTION);
                sendPt.setEntity(Protocol.ENTITY_SHELTER_LIST);
                sendPt.send(os);

                Protocol recvPt = read();
                if (recvPt != null) {
                    if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                        if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                            shelter_listDTO = (Shelter_listDTO[]) recvPt.getObject();
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
                    recommend_materialsDTO = (Recommend_materialsDTO[]) recvPt.getObject();
                    for (int i = 0; i < recommend_materialsDTO.length; i++) {
                        System.out.println("pk : " + recommend_materialsDTO[i].getRecommended_materials_pk());
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
                    vaccineDTO = (VaccineDTO[]) recvPt.getObject();
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

    private void updateVaccine() throws Exception {

        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        System.out.println("name : ");
        String name = scanner.nextLine();
        System.out.println("basic : ");
        String basic = scanner.nextLine();
        System.out.println("target age : ");
        String target_age = scanner.nextLine();
        System.out.println("comment : ");
        String comment = scanner.nextLine();
        System.out.println("period : ");
        String period = scanner.nextLine();
        System.out.println("animal kind : ");
        String kind = scanner.nextLine();
        System.out.println("animal breed : ");
        String breed = scanner.nextLine();
        System.out.println("animal age : ");
        String age = scanner.nextLine();
        VaccineDTO vaccineDTO = new VaccineDTO();
        vaccineDTO.setVaccine_name(name);
        vaccineDTO.setVaccine_basic(basic);
        vaccineDTO.setVaccine_target_age(target_age);
        vaccineDTO.setVaccine_comment(comment);
        vaccineDTO.setVaccine_period(period);
        vaccineDTO.setVaccine_animal_kind(kind);
        vaccineDTO.setVaccine_animal_breed(breed);
        vaccineDTO.setVaccine_animal_age(age);
        sendPt.setObject(vaccineDTO);
        sendPt.setCode(Protocol.T1_CODE_UPDATE);
        sendPt.setEntity(Protocol.ENTITY_VACCINE);
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

    private void updateMaterials() throws Exception {
        Recommend_materialsDTO recommend_materialsDTO = new Recommend_materialsDTO();
        System.out.println("name");
        String name = scanner.nextLine();
        System.out.println("type");
        String type = scanner.nextLine();
        System.out.println("url");
        String url = scanner.nextLine();
        System.out.println("feature");
        String feature = scanner.nextLine();
        System.out.println("img");
        String img = scanner.nextLine();
        System.out.println("animal kind");
        String kind = scanner.nextLine();
        System.out.println("animal breed");
        String breed = scanner.nextLine();
        System.out.println("animal age");
        String age = scanner.nextLine();

        recommend_materialsDTO.setMaterials_name(name);
        recommend_materialsDTO.setMaterials_type(type);
        recommend_materialsDTO.setMaterials_url(url);
        recommend_materialsDTO.setMaterials_feature(feature);
        recommend_materialsDTO.setMaterials_img(img);
        recommend_materialsDTO.setMaterials_animal_kind(kind);
        recommend_materialsDTO.setMaterials_animal_breed(breed);
        recommend_materialsDTO.setMaterials_animal_age(age);
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        sendPt.setObject(recommend_materialsDTO);
        sendPt.setCode(Protocol.T1_CODE_UPDATE);
        sendPt.setEntity(Protocol.ENTITY_MATERIALS);
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

    private void updateAdmin() throws Exception {
        RollDTO rollDTO = new RollDTO();

        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);

        System.out.println("[1] 전화번호, 이름 수정, [2] 비밀번호 수정, [3] 나가기");
        int menu = scanner.nextInt();
        while (menu != 3) {
            if (menu == 1) {
                System.out.println("전화번호 : ");
                String num = scanner.nextLine();
                System.out.println("이름 : ");
                String name = scanner.nextLine();
                rollDTO.setRoll_name(name);
                rollDTO.setRoll_phone(num);

                sendPt.setObject(rollDTO);
                sendPt.setCode(Protocol.T1_CODE_UPDATE);
                sendPt.setReadOption(Protocol.UPDATE_ROLL);
                sendPt.setEntity(Protocol.ENTITY_ADMIN);
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
            } else if (menu == 2) {
                System.out.println("비밀번호 : ");
                String num = scanner.nextLine();
                rollDTO.setRoll_pw(num);

                sendPt.setObject(rollDTO);
                sendPt.setCode(Protocol.T1_CODE_UPDATE);
                sendPt.setReadOption(Protocol.UPDATE_PASSWORD);
                sendPt.setEntity(Protocol.ENTITY_ADMIN);
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
        }
    }

    private void deleteVaccine() throws IllegalAccessException, IOException {
        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        System.out.println("pk : ");
        long pk = scanner.nextLong();

        sendPt.setObject(pk);
        sendPt.setCode(Protocol.T1_CODE_DELETE);
        sendPt.setEntity(Protocol.ENTITY_VACCINE);
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

    private void deleteMaterials() throws Exception {
        System.out.println("pk : ");
        long pk = scanner.nextLong();

        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        sendPt.setObject(pk);
        sendPt.setCode(Protocol.T1_CODE_DELETE);
        sendPt.setEntity(Protocol.ENTITY_MATERIALS);
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

    private void deleteMissingNotice() throws Exception {
        System.out.println("pk : ");
        long pk = scanner.nextLong();

        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        sendPt.setObject(pk);
        sendPt.setCode(Protocol.T1_CODE_DELETE);
        sendPt.setEntity(Protocol.ENTITY_MISSING_NOTICE);
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

    private void deleteAbandonedAnimalNotice() throws Exception {
        System.out.println("notice num : ");
        String notice_num = scanner.nextLine();

        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        sendPt.setObject(notice_num);
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

}
