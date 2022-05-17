package network;

import java.sql.*;

public class Abandoned_AnimalDB {
    private Connection conn = null;
    private Statement state = null;
    private ResultSet rs = null;

    //DB 연결
    public Abandoned_AnimalDB (String url, String id, String pw) throws SQLException, ClassNotFoundException {
        conn = null;
        state = null;
        rs = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, id, pw);
            state = conn.createStatement();
        } catch (ClassNotFoundException e) {
            System.out.println("드라이버 로딩 실패");
        } catch (SQLException e) {
            System.out.println("에러: " + e);
        }
    }
}
