package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
//@Transactional
public class TestRepository {

    @Autowired
    private EntityManager em;

    @Transactional
    void test(){
        System.out.println("em = " + em);
        List<Member> select_m_from_member_m = em.createQuery("select m from Member m", Member.class)
                .getResultList();
        System.out.println("=====");
        System.out.println("em = " + System.identityHashCode(em));
    }

}
