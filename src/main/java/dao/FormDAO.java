package dao;

import dto.Abandoned_noticeDTO;
import dto.FormDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class FormDAO {

    private SqlSessionFactory sqlSessionFactory = null;

    public FormDAO(SqlSessionFactory sqlSessionFactory) {this.sqlSessionFactory = sqlSessionFactory;}

    public List<FormDTO> FindByMember(long id)
    {
        List<FormDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            list = session.selectList("mapper.FormMapper.FindByMember", id);
        } finally {
            session.close();
        }
        return list;
    }

    public List<FormDTO> FindByStaff(long id)
    {
        List<FormDTO> list = null;

        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("mapper.FormMapper.FindByStaff", id);
        } finally {
            session.close();
        }
        return list;
    }

    public void UpdateForm(FormDTO formDTO)
    {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            session.update("mapper.FormMapper.UpdateForm", formDTO);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void InsertForm(FormDTO formDTO)
    {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession(true);
            session.insert("mapper.FormMapper.InsertForm", formDTO);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void RemoveForm(long id) {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession(true);
            session.delete("mapper.FormMapper.RemoveForm", id);
            session.commit();
        } finally {
            session.close();
        }

    }
}
