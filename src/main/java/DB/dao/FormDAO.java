package DB.dao;

import DB.dto.Abandoned_noticeDTO;
import DB.dto.FormDTO;
import DB.dto.RollDTO;
import DB.dto.Shelter_listDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class FormDAO {
    private SqlSessionFactory sqlSessionFactory = null;
    private RollDAO rollDAO;
    private Shelter_listDAO shelter_listDAO;
    private Abandoned_noticeDAO abandoned_noticeDAO;

    public FormDAO(RollDAO rollDAO, Shelter_listDAO shelter_listDAO, Abandoned_noticeDAO abandoned_noticeDAO, SqlSessionFactory sqlSessionFactory) {
        this.rollDAO = rollDAO;
        this.shelter_listDAO = shelter_listDAO;
        this.abandoned_noticeDAO = abandoned_noticeDAO;
        this.sqlSessionFactory = sqlSessionFactory;
    }
    public List<FormDTO> FindByMember(String roll_id)
    {
        List<FormDTO> list = null;
        RollDTO rollDTO = new RollDTO(roll_id);
        SqlSession session = sqlSessionFactory.openSession();
        try{
            list = session.selectList("mapper.FormMapper.FindByMember", rollDTO.getRoll_id());
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
        String id = formDTO.getRollDTOList().get(0).getRoll_id();
        System.out.println("id : " + id);
        RollDTO rollDTO = rollDAO.select(id);
        formDTO.setRoll_pk(rollDTO.getRoll_pk());
        long abandoned = formDTO.getAbandoned_notice_pk();
        formDTO.setAbandoned_notice_pk(abandoned);
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
