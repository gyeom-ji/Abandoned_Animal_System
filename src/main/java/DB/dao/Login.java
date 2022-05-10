package DB.dao;

import java.sql.*;

public class Login {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://localhost/abandoned_animal?useSSL=false&amp;allowPublicKeyRetrieval=true";
    private String id = "root";
    private String password = "!smj435821";



    public Login() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        try {
            conn = DriverManager.getConnection(url, id, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public String login(String roll_id, String roll_pw){
        String SQL = "SELECT roll_pw, roll_type FROM roll WHERE roll_id = ?";
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, roll_id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getString(1).equals(roll_pw)) {
                    System.out.println("로그인 성공"); // 로그인 성공
                    conn.commit();
                    switch (rs.getString(2)) {
                        case "관리자" :
                            System.out.println("관리자 로그인 성공");
                            return "0";
                        case "스태프" :
                            System.out.println("스태프 로그인 성공");
                            return "1";
                        case "사용자" :
                            System.out.println("사용자 로그인 성공");
                            return "2";
                    }
                }
                else {
                    System.out.println("비밀번호 불일치"); // 비밀번호 불일치
                    return "3";
                }
            }
            else {
                System.out.println("아이디가 존재하지 않음"); // 아이디가 없음
                return "3";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DB 오류"); // DB 오류
        }
        return "3";
    }
}
