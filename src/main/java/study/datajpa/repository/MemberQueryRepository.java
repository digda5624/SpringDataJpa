package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@Repository // JPA 의 예외를 spring에서 제공하는 예외로 변환시켜주는 역할도 한다.
@RequiredArgsConstructor
// 핵심 비즈니스 로직을 다루는 repository와 화면 맞춤형 쿼리를 다루는 repository를 분리하자
public class MemberQueryRepository {

    private final EntityManager em;

    List<Member> findAllMembers() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
