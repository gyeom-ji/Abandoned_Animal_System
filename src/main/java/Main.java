import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) {

        String address = "127.0.0.1";
        int port = 3306;
        try {
            Socket socket = new Socket(address, port);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

//            Animal_Client program = new Animal_Client(is, os);
//            program.run();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
