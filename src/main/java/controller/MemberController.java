package controller;

import DB.dao.*;
import DB.dto.*;
import network.Protocol;
import service.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MemberController implements DefinedController {
    public static final int USER_UNDEFINED = 0;
    public static final int MEMBER_TYPE = 2;

    private final Abandoned_noticeDAO abandoned_noticeDAO;
    private final AnimalDAO animalDAO;
    private final FormDAO formDAO;
    private final Missing_noticeDAO missing_noticeDAO;
    private final Recommend_materialsDAO recommend_materialsDAO;
    private final RollDAO rollDAO;
    private final Shelter_listDAO shelter_listDAO;
    private final VaccineDAO vaccineDAO;

    private final RollService rollService;
    private final Shelter_listService shelter_listService;
    private final Abandoned_noticeService abandoned_noticeService;
    private final Missing_noticeService missing_noticeService;
    private final AnimalService animalService;
    private final FormService formService;
    private final Recommend_materialsService recommend_materialsService;
    private final VaccineService vaccineService;

    private final InputStream is;
    private final OutputStream os;

    public MemberController(
            Abandoned_noticeDAO abandoned_noticeDAO, AnimalDAO animalDAO,
            FormDAO formDAO, Missing_noticeDAO missing_noticeDAO,
            Recommend_materialsDAO recommend_materialsDAO, RollDAO rollDAO,
            Shelter_listDAO shelter_listDAO, VaccineDAO vaccineDAO,
            InputStream is, OutputStream os) {
        this.abandoned_noticeDAO = abandoned_noticeDAO;
        this.animalDAO = animalDAO;
        this.formDAO = formDAO;
        this.missing_noticeDAO = missing_noticeDAO;
        this.recommend_materialsDAO = recommend_materialsDAO;
        this.rollDAO = rollDAO;
        this.shelter_listDAO = shelter_listDAO;
        this.vaccineDAO = vaccineDAO;
        this.is = is;
        this.os = os;

        //각 기능을 수행할 (서비스)객체
        rollService = new RollService(rollDAO);
        shelter_listService = new Shelter_listService(shelter_listDAO);
        abandoned_noticeService = new Abandoned_noticeService(abandoned_noticeDAO);
        missing_noticeService = new Missing_noticeService(missing_noticeDAO);
        animalService = new AnimalService(animalDAO);
        formService = new FormService(formDAO);
        recommend_materialsService = new Recommend_materialsService(recommend_materialsDAO);
        vaccineService = new VaccineService(vaccineDAO);
    }

    @Override
    public int handler(Protocol recvPt) throws Exception {
        System.out.println("member handler");
        // recvPt는 클라이언트로부터 받은 packet
        switch (recvPt.getCode()) { // code로 분류
            case Protocol.T1_CODE_CREATE: // 등록
                createReq(recvPt);
                break;
            case Protocol.T1_CODE_READ:   // 조회
                readReq(recvPt);
                break;
            case Protocol.T1_CODE_UPDATE:  // 변경
                updateReq(recvPt);
                break;
            case Protocol.T1_CODE_DELETE:  // 삭제
                deleteReq(recvPt);
                break;
            case Protocol.T1_CODE_LOGOUT:   // 로그아웃
                logoutReq();
                return USER_UNDEFINED;  // 로그아웃요청이 왔을 땐 userType을 USER_UNDEFINED로 지정
            default:
        }
        return MEMBER_TYPE;
    }

    // 로그아웃 요청이 왔을 때 수행할 일
    private void logoutReq() throws IOException {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.send(os);
    }

    // 생성 요청 받았을 때 수행할 일
    private void createReq(Protocol recvPt) throws Exception {
        // entity로 분류
        switch (recvPt.getEntity()) {
            case Protocol.ENTITY_MISSING_NOTICE:
                createMissingNotice(recvPt);  // 실종공고 생성 요청
                break;
            case Protocol.ENTITY_FORM:
                createForm(recvPt);  // 신청서 생성 요청
                break;
            default:
        }
    }

    // 조회 요청
    private void readReq(Protocol recvPt) throws Exception {
        System.out.println("readreq");
        switch (recvPt.getEntity()) {
            case Protocol.ENTITY_MEMBER:
                readMember(recvPt);       // 계정 조회 요청
                break;
            case Protocol.ENTITY_MISSING_NOTICE:
                readMissingNotice(recvPt);    // 실종공고 조회 요청
                break;
            case Protocol.ENTITY_ABANDONED_ANIMAL_NOTICE:
                readAbandonedAnimalNotice(recvPt);  // 유기공고 조회 요청
                break;
            case Protocol.ENTITY_SHELTER_LIST:
                readShelterList(recvPt);  // 보호소 조회 요청
                break;
            case Protocol.ENTITY_FORM:
                readForm(recvPt);  // 신청서 조회 요청
                break;
            case Protocol.ENTITY_MATERIALS:
                readMaterials(recvPt);  // 상품 조회 요청
                break;
            case Protocol.ENTITY_VACCINE:
                readVaccine(recvPt);  // 백신 조회 요청
                break;
            default:
        }
    }


    // 변경 요청
    private void updateReq(Protocol recvPt) throws Exception {
        switch (recvPt.getEntity()) {
            case Protocol.ENTITY_MEMBER:
                updateMember(recvPt);  // 회원 정보 수정 요청
                break;
            case Protocol.ENTITY_MISSING_NOTICE:
                updateMissingNotice(recvPt);    // 실종공고 수정 요청
                break;
            case Protocol.ENTITY_FORM:
                updateForm(recvPt);   // 신청서 수정 요청
                break;
            default:
        }
    }

    // 삭제 요청
    private void deleteReq(Protocol recvPt) throws Exception {
        switch (recvPt.getEntity()) {
            case Protocol.ENTITY_MISSING_NOTICE:
                deleteMissingNotice(recvPt);   // 실종공고 삭제 요청
                break;
            case Protocol.ENTITY_FORM:
                deleteForm(recvPt);  // 신청서 삭제 요청
                break;
            default:
        }
    }

    private void createMissingNotice(Protocol recvPt) throws Exception {
        Missing_noticeDTO missing_noticeDTO = (Missing_noticeDTO) recvPt.getObject();
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try {
            missing_noticeService.create(missing_noticeDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        } catch (IllegalArgumentException e) {
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    private void createForm(Protocol recvPt) throws Exception {
        FormDTO formDTO = (FormDTO) recvPt.getObject();
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try {
            formService.create(formDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        } catch (IllegalArgumentException e) {
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    private void readVaccine(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        FormDTO formDTO = (FormDTO) recvPt.getObject();
        try {
            VaccineDTO[] vaccineDTOS = vaccineService.select_type(formDTO);
            sendPt.setObjectArray(vaccineDTOS);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        } catch (IllegalArgumentException e) {
            sendPt.setCode(Protocol.T2_CODE_FAIL);

            sendPt.send(os);
        }
    }

    private void readMaterials(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        FormDTO formDTO = (FormDTO) recvPt.getObject();
        try {
            Recommend_materialsDTO[] recommend_materialsDTOS = recommend_materialsService.select_type(formDTO);
            sendPt.setObjectArray(recommend_materialsDTOS);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        } catch (IllegalArgumentException e) {
            sendPt.setCode(Protocol.T2_CODE_FAIL);

            sendPt.send(os);
        }
    }

    private void readMissingNotice(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        switch (recvPt.getReadOption()) {
            case Protocol.READ_ALL: {    // 전체조회
                try {
                    Missing_noticeDTO[] missing_noticeDTOS = missing_noticeService.select();
                    sendPt.setObjectArray(missing_noticeDTOS);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);
                } catch (IllegalArgumentException e) {
                    sendPt.setCode(Protocol.T2_CODE_FAIL);

                    sendPt.send(os);
                }
                break;
            }
            case Protocol.READ_BY_OPTION: {
                try {
                    Missing_noticeDTO options = (Missing_noticeDTO) recvPt.getObject();
                    Missing_noticeDTO[] missing_noticeDTOS = missing_noticeService.select_address(options);
                    sendPt.setObjectArray(missing_noticeDTOS);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);
                } catch (IllegalArgumentException e) {
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.send(os);
                }
                break;
            }
        }
    }

    private void readAbandonedAnimalNotice(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        switch (recvPt.getReadOption()) {
            case Protocol.READ_ALL: {    // 전체조회
                try {
                    Abandoned_noticeDTO[] abandoned_noticeDTOS = abandoned_noticeService.select();
                    sendPt.setObjectArray(abandoned_noticeDTOS);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);
                } catch (IllegalArgumentException e) {
                    sendPt.setCode(Protocol.T2_CODE_FAIL);

                    sendPt.send(os);
                }
                break;
            }
            case Protocol.READ_BY_OPTION: {  // 지역으로 조회
                try {
                    Abandoned_noticeDTO options = (Abandoned_noticeDTO) recvPt.getObject();
                    System.out.println("option");
                    Abandoned_noticeDTO[] abandoned_noticeDTOS = abandoned_noticeService.select_address(options.getShelter_listDTOList().get(0).getShelter_county(), options.getShelter_listDTOList().get(0).getShelter_city());
                    sendPt.setObjectArray(abandoned_noticeDTOS);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);
                } catch (IllegalArgumentException e) {
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.send(os);
                }
                break;
            }
        }
    }

    private void readShelterList(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        switch (recvPt.getReadOption()) {
            case Protocol.READ_ALL: {    // 전체 조회
                try {
                    Shelter_listDTO[] shelter_listDTOS = shelter_listService.selectAll();
                    sendPt.setObjectArray(shelter_listDTOS);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);
                } catch (IllegalArgumentException e) {
                    sendPt.setCode(Protocol.T2_CODE_FAIL);

                    sendPt.send(os);
                }
                break;
            }
            case Protocol.READ_BY_OPTION: {  // 옵션으로 조회
                try {
                    Shelter_listDTO options = (Shelter_listDTO) recvPt.getObject();
                    Shelter_listDTO[] shelter_listDTOS = shelter_listService.select_address(options.getShelter_county(), options.getShelter_city());
                    sendPt.setObjectArray(shelter_listDTOS);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);
                } catch (IllegalArgumentException e) {
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.send(os);
                }
                break;
            }
        }
    }

    private void readMember(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try {
            System.out.println("read member");
            RollDTO id = ( RollDTO)recvPt.getObject();
            RollDTO roll = rollService.selectByID(id.getRoll_id());
            System.out.println(roll.getRoll_name());
            sendPt.setObject(roll);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        } catch (IllegalArgumentException e) {
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    private void readForm(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        RollDTO roll_id = (RollDTO) recvPt.getObject();
        System.out.println(roll_id.getRoll_id());
        try {
            FormDTO[] formDTOS = formService.FindByMember(roll_id.getRoll_id());
            sendPt.setObjectArray(formDTOS);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        } catch (IllegalArgumentException e) {
            sendPt.setCode(Protocol.T2_CODE_FAIL);

            sendPt.send(os);
        }
    }

    private void updateMember(Protocol recvPt) throws Exception {
        RollDTO rollDTO = (RollDTO) recvPt.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        switch (recvPt.getReadOption()) {
            case Protocol.UPDATE_ROLL: {    // 전화번호, 이름 수정
                try {
                    rollService.update(rollDTO);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);

                } catch (IllegalArgumentException e) {
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.send(os);
                }
                break;
            }
            case Protocol.UPDATE_PASSWORD: {  // 비밀번호 수정
                try {
                    rollService.changePassword(rollDTO);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);

                } catch (IllegalArgumentException e) {
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.send(os);
                }
                break;
            }
        }
    }

    private void updateMissingNotice(Protocol recvPt) throws Exception {
        Missing_noticeDTO missing_noticeDTO = (Missing_noticeDTO) recvPt.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        try {
            missing_noticeService.update(missing_noticeDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) {
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    private void updateForm(Protocol recvPt) throws Exception {
        FormDTO formDTO = (FormDTO) recvPt.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        try {
            formService.update_member(formDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) {
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    private void deleteMissingNotice(Protocol recvPt) throws Exception {
        Missing_noticeDTO missing_notice_pk = (Missing_noticeDTO) recvPt.getObject();
        System.out.println(missing_notice_pk.getMissing_notice_pk());
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        try {
            missing_noticeService.delete(missing_notice_pk.getMissing_notice_pk());
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) {
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    private void deleteForm(Protocol recvPt) throws Exception {
        long form_pk = (long) recvPt.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        try {
            formService.delete(form_pk);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) {
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }
}
