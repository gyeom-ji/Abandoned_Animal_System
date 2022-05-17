package client;

import java.io.*;

import DB.dto.RollDTO;
import network.Protocol;

import java.net.Socket;

public class Login {
    private static Socket socket;
    private static InputStream is;
    private static OutputStream os;
    public static void run(String address, int portNumber) throws Exception {
        socket = new Socket(address, portNumber);
        is = socket.getInputStream();
        os = socket.getOutputStream();
        RollDTO rollDTO = login();
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

    private static RollDTO login() throws IOException {
        while (true) {
            BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("아이디 : ");
            String id = userIn.readLine();
            System.out.print("비밀번호 : ");
            String pw = userIn.readLine();
            RollDTO rollDTO = new RollDTO(id, pw);
            Protocol protocol = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_CREATE, Protocol.ENTITY_ACCOUNT);

            os.write(protocol.getPacket());

            //TODO 로그인 정보 생성 및 패킷 전송
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
