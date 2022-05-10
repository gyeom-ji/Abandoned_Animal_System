package dao;

import dto.RollDTO;
import dto.Shelter_listDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.*;

public class RollDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public RollDAO(SqlSessionFactory sqlSessionFactory) {this.sqlSessionFactory = sqlSessionFactory;}

    //계정 정보 입력
    public void insert_Roll(RollDTO rollDTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.getMapper(IRollDAO.class).insert_Roll(rollDTO);
            session.commit();
        } finally {
            session.close();
        }
    }

    //계정 정보 수정
    public void update_Roll(RollDTO rollDTO) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.getMapper(IRollDAO.class).update_Roll(rollDTO);
            session.commit();
        }
        finally {
            session.close();
        }
    }

    //계정 정보 삭제
    public void delete_Roll(long id) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.getMapper(IRollDAO.class).delete_Roll(id);
            session.commit();
        }
        finally {
            session.close();
        }
    }



}
