package study.datajpa.repository;

import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    // spring data jpa 가 제공하는 기능이 아닌 나만의 기능을 사용하고 싶다.
    List<Member> findMemberCustom();
}
