package DB.dao;

import DB.dto.Abandoned_noticeDTO;
import DB.dto.FormDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class FormDAO {

    private SqlSessionFactory sqlSessionFactory = null;

    public FormDAO(SqlSessionFactory sqlSessionFactory) {this.sqlSessionFactory = sqlSessionFactory;}

    public List<FormDTO> FindByMember(String roll_id)
    {
        List<FormDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            list = session.selectList("mapper.FormMapper.FindByMember", roll_id);
        } finally {
            session.close();
        }
        return list;
    }

    public List<FormDTO> FindByStaff(String shelter_name)
    {
        List<FormDTO> list = null;

        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("mapper.FormMapper.FindByStaff", shelter_name);
        } finally {
            session.close();
        }
        return list;
    }

    //승인 수정
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

    //(회원) 타입 수정
    public void UpdateType(FormDTO formDTO)
    {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            session.update("mapper.FormMapper.UpdateType", formDTO);
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

    public FormDTO FindByForm_id(long id){
        FormDTO formDTO = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            formDTO = session.selectOne("mapper.FormMapper.FindByForm_id", id);
        }
        finally{
            session.close();
        }
        return formDTO;
    }
}
