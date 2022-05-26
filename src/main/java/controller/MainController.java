package controller;

import DB.dao.*;
import network.Protocol;
import network.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

//사용자의 요청이 들어왔을 받아서 하위 Controller로
//요청을 넘기는 객체
public class MainController extends Thread {
    // USER 구분
    public static final int USER_UNDEFINED = 0;
    public static final int STAFF_TYPE = 1;
    public static final int MEMBER_TYPE= 2;
    public static final int ADMIN_TYPE = 3;
    private int userType;

    private int clientID;   // client port 번호
    private Socket socket;
    private InputStream is;
    private OutputStream os;

    private boolean running;    // run() 메소드의 while문 flag
    private DefinedController myController;

    private final Abandoned_noticeDAO abandoned_noticeDAO;
    private final AnimalDAO animalDAO;
    private final FormDAO formDAO;
    private final Missing_noticeDAO missing_noticeDAO;
    private final Recommend_materialsDAO recommend_materialsDAO;
    private final RollDAO rollDAO;
    private final Shelter_listDAO shelter_listDAO;
    private final VaccineDAO vaccineDAO;

    // 스레드 생성자
    public MainController(
            Abandoned_noticeDAO abandoned_noticeDAO, AnimalDAO animalDAO, FormDAO formDAO,
            Missing_noticeDAO missing_noticeDAO, Recommend_materialsDAO recommend_materialsDAO, RollDAO rollDAO,
            Shelter_listDAO shelter_listDAO, VaccineDAO vaccineDAO,Socket socket){
        this.abandoned_noticeDAO = abandoned_noticeDAO;
        this.animalDAO = animalDAO;
        this.formDAO = formDAO;
        this.missing_noticeDAO = missing_noticeDAO;
        this.recommend_materialsDAO = recommend_materialsDAO;
        this.rollDAO = rollDAO;
        this.shelter_listDAO = shelter_listDAO;
        this.vaccineDAO = vaccineDAO;
        this.socket = socket;
        clientID = socket.getPort();
        this.socket = socket;
        try{
            is = socket.getInputStream();
            os = socket.getOutputStream();
        }catch(IOException e){
            e.getStackTrace();
        }
    }

    @Override
    public void run() {
        running = true;
        while (running) {    // thread 종료 메소드 호출될 시 running은 fasle가 됨
            try {
                System.out.println("main controller entry");
                Protocol pt = new Protocol();
                handler(pt.read(is)); // 클라이언트로부터 받은 패킷을 읽고 handler로 전달
            } catch (Exception e) {
                e.printStackTrace();
                exit();     // thread 종료 메소드 호출
            }
        }
        System.out.println("thread 종료");
    }

    public int getClientID() {
        return clientID;
    }

    public void handler(Protocol pt) throws Exception {
        System.out.println("handler entry");
        switch(userType){
            //로그인하기전 요청 처리
            case USER_UNDEFINED:
                UndefinedController undefinedController = new UndefinedController(
                        socket, is, os, clientID, rollDAO
                );
                userType = undefinedController.handler(pt);
                setMyController();
                break;
            //로그인 후의 요청처리
            case ADMIN_TYPE:
                AdminController adminController = new AdminController(
                        abandoned_noticeDAO, animalDAO, formDAO, missing_noticeDAO, recommend_materialsDAO,
                        rollDAO,shelter_listDAO,vaccineDAO,is, os
                );
                userType = adminController.handler(pt);
                break;
            case STAFF_TYPE:
                StaffController staffController = new StaffController(
                        abandoned_noticeDAO, animalDAO, formDAO, missing_noticeDAO,
                        recommend_materialsDAO, rollDAO, shelter_listDAO, vaccineDAO
                        , is, os);
                userType = staffController.handler(pt);
                break;
            case MEMBER_TYPE:
                MemberController memberController = new MemberController(
                        abandoned_noticeDAO, animalDAO, formDAO, missing_noticeDAO,
                        recommend_materialsDAO, rollDAO, shelter_listDAO, vaccineDAO
                        , is, os);
                System.out.println("membertype");
                userType = memberController.handler(pt);
                break;
        }
    }


    // 사용자 종류가 지정되면 해당 사용자 종류 전용 controller 생성
    private void setMyController() {
        switch (userType){
            case ADMIN_TYPE:
                if(myController==null){     // 지정된 controller가 없을 때만 새로 생성
                    myController = new AdminController(
                            abandoned_noticeDAO, animalDAO, formDAO, missing_noticeDAO,
                            recommend_materialsDAO, rollDAO, shelter_listDAO, vaccineDAO
                            , is, os);
                }
                break;
            case STAFF_TYPE:
                if(myController==null){
                    myController = new StaffController(
                            abandoned_noticeDAO, animalDAO, formDAO, missing_noticeDAO,
                            recommend_materialsDAO, rollDAO, shelter_listDAO, vaccineDAO
                            , is, os);
                }
                break;
            case MEMBER_TYPE:
                if(myController==null){
                    myController = new MemberController(
                            abandoned_noticeDAO, animalDAO, formDAO, missing_noticeDAO,
                            recommend_materialsDAO, rollDAO, shelter_listDAO, vaccineDAO
                            , is, os);
                    System.out.println("membertype");
                }
                break;
            case USER_UNDEFINED:
                myController = null;
                break;
        }
    }

    // 소켓 종료 및 스레드 종료
    private void exit() {
        Server.removeThread(clientID);
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e);
        }
        running = false;
    }
}
