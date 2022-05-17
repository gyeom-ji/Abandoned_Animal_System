package client;

import java.io.*;

import DB.dto.RollDTO;
import network.Protocol;

import java.net.Socket;
import java.util.Scanner;

public class Login {
    private static Socket socket;
    private static InputStream is;
    private static OutputStream os;
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

    public static void run(String address, int portNumber) throws Exception {
        BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
        socket = new Socket(address, portNumber);
        is = socket.getInputStream();
        os = socket.getOutputStream();
        System.out.println("[1]로그인 [2]계정 생성 [3]종료");
        int menu = userIn.read();
        while(menu !=3){
            if(menu == 1)
                login();
            else if(menu == 2)
                createRoll();
            else
                System.out.println("잘못 입력 하셨습니다.");
        }
        RollDTO rollDTO = login();

    }

    public static RollDTO login() throws Exception {
        while (true) {
            BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("아이디 : ");
            String id = userIn.readLine();
            System.out.print("비밀번호 : ");
            String pw = userIn.readLine();
            RollDTO rollDTO = new RollDTO(id, pw);
            Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
            sendPt.setObject(rollDTO);
            sendPt.setCode(Protocol.T1_CODE_LOGIN);
            sendPt.send(os);

            Protocol recvPt = read();
            if (recvPt != null) {
                if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                    if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                        System.out.println("성공");
                        rollDTO = (RollDTO) recvPt.getObject();
                        if (rollDTO.getRoll_id().charAt(0) == '#') {
                            LoginService service = createService("admin", is, os);
                            service.run();
                        }
                        else  if (rollDTO.getRoll_id().charAt(0) == '@') {
                            LoginService service = createService("staff", is, os);
                            service.run();
                        }
                        else {
                            LoginService service = createService("member", is, os);
                            service.run();
                        }
                    }
                    else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                        System.out.println("실패");
                }
            }
        }
    }

    public static RollDTO createRoll() throws Exception {
        while (true) {
            BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("아이디 : ");
            String id = userIn.readLine();
            System.out.print("비밀번호 : ");
            String pw = userIn.readLine();
            System.out.print("이름 : ");
            String name = userIn.readLine();
            System.out.print("전화번호 : ");
            String phone = userIn.readLine();
            RollDTO rollDTO = new RollDTO(id, pw,name,phone);

            Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
            sendPt.setObject(rollDTO);
            sendPt.setCode(Protocol.T1_CODE_CREATE);
            sendPt.send(os);

            Protocol recvPt = read();
            if (recvPt != null) {
                if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                    if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                        System.out.println("성공");
                    }
                    else if (recvPt.getCode() == Protocol.T2_CODE_FAIL)
                        System.out.println("실패");
                }
            }
        }
    }

    private static LoginService createService(String authority, InputStream is, OutputStream os) {
        if (authority.equals("admin")) {
            return new AdminService(is, os);
        } else if (authority.equals("staff")) {
            return new StaffService(is, os);
        } else if (authority.equals("member")) {
            return new MemberService(is, os);
        } else {
            return null;
        }
    }
}
