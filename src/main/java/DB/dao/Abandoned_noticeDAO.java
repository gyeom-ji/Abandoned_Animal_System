package DB.dao;

import DB.dto.Abandoned_noticeDTO;
import DB.dto.Shelter_listDTO;
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

    public List<Abandoned_noticeDTO> FindByOption(final String shelter_county, final String shelter_city)
    {
        Shelter_listDTO shelter_listDTO = new Shelter_listDTO();
        shelter_listDTO.setShelter_county(shelter_county);
        shelter_listDTO.setShelter_city(shelter_city);
        List<Abandoned_noticeDTO> list = null;

        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("mapper.Abandoned_noticeMapper.FindByOption", shelter_listDTO);
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

    public void RemoveAbandoned(String abandoned_notice_num) {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession(true);
            session.delete("mapper.Abandoned_noticeMapper.RemoveAbandoned", abandoned_notice_num);
            session.commit();
        } finally {
            session.close();
        }
    }

    public Abandoned_noticeDTO FindByAbandoned_notice_num(String abandoned_notice_num){
        Abandoned_noticeDTO abandoned_noticeDTO = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            abandoned_noticeDTO = session.selectOne("mapper.Abandoned_noticeMapper.FindByAbandoned_notice_num", abandoned_notice_num);
        }
        finally{
            session.close();
        }
        return abandoned_noticeDTO;
    }

}
