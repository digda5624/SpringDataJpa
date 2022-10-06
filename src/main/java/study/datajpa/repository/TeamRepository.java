package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Team;

/**
 * 구현체가 없는데 어떻게 존재하냐
 *
 * 상속 받으면 spring 이 인식해서 구현체를 만들고 Repository 임을 인식한다.
 */
public interface TeamRepository extends JpaRepository<Team, Long> {
}
