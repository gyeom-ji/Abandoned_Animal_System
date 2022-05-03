package dao;

import dto.Abandoned_noticeDTO;
import dto.FormDTO;
import dto.Missing_noticeDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class Missing_noticeDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public Missing_noticeDAO(SqlSessionFactory sqlSessionFactory) {this.sqlSessionFactory = sqlSessionFactory;}

    public List<Abandoned_noticeDTO> ReadAll()
    {
        List<Abandoned_noticeDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            list = session.selectList("mapper.Missing_noticeMapper.ReadAll");
        } finally {
            session.close();
        }
        return list;
    }

    public List<Missing_noticeDTO> FindByOption(Missing_noticeDTO missing_noticeDTO)
    {
        List<Missing_noticeDTO> list = null;

        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("mapper.Missing_noticeMapper.FindByOption", missing_noticeDTO);
        } finally {
            session.close();
        }
        return list;
    }

    public void UpdateMissing(Missing_noticeDTO missing_noticeDTO)
    {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            session.update("mapper.Missing_noticeMapper.UpdateMissing", missing_noticeDTO);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void InsertMissing(Missing_noticeDTO missing_noticeDTO)
    {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession(true);
            session.insert("mapper.Missing_noticeMapper.InsertMissing", missing_noticeDTO);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void RemoveMissing(long id) {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession(true);
            session.delete("mapper.Missing_noticeMapper.RemoveMissing", id);
            session.commit();
        } finally {
            session.close();
        }

    }
}


