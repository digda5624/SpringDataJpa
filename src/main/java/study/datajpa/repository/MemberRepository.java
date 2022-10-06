package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.annotation.PreDestroy;
import javax.persistence.Entity;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

//    Named 쿼리 사용하기
//    @Query(name = "Member.findByUsername") -> 없어도 관례상으로는 찾아가 진다. 1. nameQuery로 찾기 그후에 2. 쿼리이름으로 메서드 사용
//    List<Member> findByUsername(@Param("username") String username);

    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    // 리스트 반환 타입
    List<Member> findListByUsername(String username);
    // Member 단건 반환 타입
    Member findMemberByUsername(String username);
    // Optional<Member> 단건 반환 타입
    Optional<Member> findOptionalByUsername(String username);

    // count 쿼리를 따로 분리하여 가져갈 수도 있음
    @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m left join m.team t")
    Slice<Member> findByAgeSlice(int age, Pageable pageable);

    @Modifying(clearAutomatically = true) // update 쿼리일땐 넣어줘야됨 (벌크연산일경우 clear 해줘야됨)
    @Query("update Member m set m.age = m.age+1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    // EntityGraph 객체 그래프를 한번에 가져온다.
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    // 쿼리 짜면서 해도 된다.
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

//    @EntityGraph(attributePaths = {"team"}) // EntityGraph 사용
    @EntityGraph("Member.all") // NamedEntityGraph 사용
    List<Member> findEntityGraphByUsername(String username);

    // 성능 최적화 관련 스냅샷을 만들지 않는다. hint 를 통해서 성능 최적화를 기대할 수 있다. => 하지만 큰 성능 최적화를 기대하기는 힘들다.
    // 성능 테스트 해보고 결정해야 한다. 근데 사실상 redis 를 쓰는 것이 좋다.
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    // 락을 적용하는 내용 DB 에 따라서 다르지만 h2 의 경우 for update 쿼리가 나간다.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

    <T> List<T> findProjectionsByUsername(String username, Class<T> type);

    @Query(value = "select * from member where username = ?", nativeQuery = true)
    Member findByNativeQuery(String username); // 이름 아무거나 상관 없음

    @Query(value = "select m.member_id as id, m.username, t.name as teamName" +
            " from member m left join team t",
            countQuery = "select count(*) from member",
            nativeQuery = true)
    Page<MemberProjection> findByNativeProjection(Pageable pageable); // 이름 아무거나 상관없음
}
