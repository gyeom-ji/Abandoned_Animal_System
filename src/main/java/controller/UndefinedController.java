package controller;

import DB.dao.RollDAO;
import DB.dto.RollDTO;
import network.Protocol;
import service.RollService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

//로그인전에 요청을 수행하는 객체
public class UndefinedController {
    public static final int USER_UNDEFINED = 0;
    public static final int STAFF_TYPE = 1;
    public static final int MEMBER_TYPE= 2;
    public static final int ADMIN_TYPE = 3;

    private RollDAO rollDAO;
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private int clientID;

    public UndefinedController(Socket socket, InputStream is, OutputStream os,
                               int clientID, RollDAO rollDAO){
        this.socket = socket;
        this.is = is;
        this.os = os;
        this.clientID = clientID;
        this.rollDAO = rollDAO;
    }

    public int handler(Protocol recvPt){
        try{
            switch (recvPt.getCode()) {
                case Protocol.T1_CODE_LOGIN:   // 로그인 요청
                    return loginReq(recvPt);
                case Protocol.T1_CODE_CREATE: // 생성 요청
                    createRoll(recvPt);
                    return USER_UNDEFINED;
                case Protocol.T1_CODE_LOGOUT:  // 로그아웃 요청
                    logoutReq();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return USER_UNDEFINED;
    }

    // 로그인
    private int loginReq(Protocol recvPt) throws Exception{
        System.out.println("login entry");
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        RollDTO rollDTO = (RollDTO) recvPt.getObject();
        try{
            RollService rollService = new RollService(rollDAO);
            RollDTO loginRollDTO= rollService.login(rollDTO);

            // 로그인 성공 - 성공 메시지 전송
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.setObject(loginRollDTO);
            sendPt.send(os);

            if(loginRollDTO.getRoll_id().charAt(0) == '#'){
                return ADMIN_TYPE;
            }else if(loginRollDTO.getRoll_id().charAt(0) == '@'){
                return STAFF_TYPE;
            }else{
                return MEMBER_TYPE;
            }

        }catch(IllegalArgumentException e){
            // 로그인 실패 - 실패 메시지 전송
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.setObject(e.getMessage());
            sendPt.send(os);
        }
        return USER_UNDEFINED;
    }

    // 계정 생성
    private void createRoll(Protocol recvPt) throws Exception{
        RollService rollService = new RollService(rollDAO);
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try{
            RollDTO dto = (RollDTO) recvPt.getObject();
            if(dto.getRoll_id().charAt(0) == '#'){
                dto.setRoll_type("관리자");
            }else if(dto.getRoll_id().charAt(0) == '@'){
                dto.setRoll_type("직원");
            }else{
                dto.setRoll_type("회원");
            }
            rollService.create(dto);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        }catch(IllegalArgumentException e){
            // 계정 생성 실패
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.setObject(e.getMessage());
            sendPt.send(os);
        }
    }

    // 로그아웃
    private void logoutReq() throws IOException {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.send(os);
    }
}