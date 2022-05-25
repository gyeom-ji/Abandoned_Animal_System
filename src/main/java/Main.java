import client.Login;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) {

        String address = "127.0.0.1";
        int port = 3000;
        try {
            Socket socket = new Socket(address, port);
            Login login = new Login();
            Login.run(address,port);
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
