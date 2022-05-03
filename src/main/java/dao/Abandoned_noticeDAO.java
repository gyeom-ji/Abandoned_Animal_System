package dao;

import dto.Abandoned_noticeDTO;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import java.util.List;

public class Abandoned_noticeDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public Abandoned_noticeDAO(SqlSessionFactory sqlSessionFactory) {this.sqlSessionFactory = sqlSessionFactory;}

    public List<Abandoned_noticeDTO> ReadAll()
    {
        List<Abandoned_noticeDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            list = session.selectList("mapper.Abandoned_noticeMapper.ReadAll");
        } finally {
            session.close();
        }
        return list;
    }

    public List<Abandoned_noticeDTO> FindByOption(Abandoned_noticeDTO abandoned_noticeDTO)
    {
        List<Abandoned_noticeDTO> list = null;

        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("mapper.Abandoned_noticeMapper.FindByOption", abandoned_noticeDTO);
        } finally {
            session.close();
        }
        return list;
    }

    public void UpdateAbandoned(Abandoned_noticeDTO abandoned_noticeDTO)
    {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            session.update("mapper.Abandoned_noticeMapper.UpdateAbandoned", abandoned_noticeDTO);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void InsertAbandoned(Abandoned_noticeDTO abandoned_noticeDTO)
    {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession(true);
            session.insert("mapper.Abandoned_noticeMapper.InsertAbandoned", abandoned_noticeDTO);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void RemoveAbandoned(long id) {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession(true);
            session.delete("mapper.Abandoned_noticeMapper.RemoveAbandoned", id);
            session.commit();
        } finally {
            session.close();
        }

    }
}
