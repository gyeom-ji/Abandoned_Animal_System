package DB.dao;

import DB.dto.Missing_noticeDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class Missing_noticeDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public Missing_noticeDAO(SqlSessionFactory sqlSessionFactory) {this.sqlSessionFactory = sqlSessionFactory;}

    public List<Missing_noticeDTO> ReadAll()
    {
        List<Missing_noticeDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            list = session.selectList("mapper.Missing_noticeMapper.ReadAll");
        } finally {
            session.close();
        }
        return list;
    }

    public List<Missing_noticeDTO> FindByOption(final String county, final String city)
    {
        Missing_noticeDTO missing_noticeDTO = new Missing_noticeDTO();
        missing_noticeDTO.setMissing_county(county);
        missing_noticeDTO.setMissing_city(city);
        List<Missing_noticeDTO> list = null;

        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("mapper.Missing_noticeMapper.FindByOption", missing_noticeDTO);
        } finally {
            session.close();
        }
        return list;
    }

    public Missing_noticeDTO FindByPk(long pk)
    {
        Missing_noticeDTO missing = null;

        SqlSession session = sqlSessionFactory.openSession();
        try {
            missing = session.selectOne("mapper.Missing_noticeMapper.FindByPk", pk);
        } finally {
            session.close();
        }
        return missing;
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


