package client;

import client.LoginService;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class MemberService implements LoginService {
    public static Scanner scanner = new Scanner(System.in);

    private InputStream is;
    private OutputStream os;


    public MemberService(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
    }

    @Override
    public void run() throws Exception {

    }
}
