package study.datajpa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

@SpringBootTest
public class EntityManagerTest {

    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    DataSource dataSource;

    @Test
    @DisplayName("[sucess] 엔티티메니저_생명주기_테스트")
    public void 엔티티메니저_생명주기_테스트(){
        // given
        DataSource dataSource1 = dataSource;
        System.out.println("dataSource = " + dataSource);
        Member member = new Member("test");
        memberRepository.save(member);
        em.find(Member.class, member.getId());
        Map<Object, Object> resourceMap = TransactionSynchronizationManager.getResourceMap();
        em.find(Member.class, member.getId());
        Map<Object, Object> resourceMap1 = TransactionSynchronizationManager.getResourceMap();
        memberRepository.findById(member.getId());
        Map<Object, Object> resourceMap2 = TransactionSynchronizationManager.getResourceMap();
        System.out.println("done = ");
    }
}
