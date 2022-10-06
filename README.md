# Spring Data Jpa

https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation

Spring.data 프로젝트에서 나오게 되는 

Spring.data.jpa.repository 이다.

유사 메서드로 편리한 개발을 할 수 있게 만들어 준다.

구현체가 없는데 어떻게 존재하냐 <br>
=> 상속 받으면 spring 이 인식해서 구현체를 만들고 Repository 임을 인식한다.

## 주요 메서드
save()
findById()
getById()

## 문제점
그렇다면 결국에 상속을 받은 MemberRepository도 interface 인데 spring에서 제공하는 공통
메서드를 제외하고서는 어떻게 구현을 해야 할까?

* 쿼리 메서드 기능 
   * 메서드 이름으로 쿼리 생성 => 강력한 기능 : 컴파일 오류로 확인 가능
   * NamedQuery 사용하기 => 어플리케이션 로딩 시점에 쿼리를 파싱할 수 있어 버그 잡기 가능
   * @Query 를 사용해서 메서드에 쿼리 정의하 (dto로 조회 할 때는 패키지 명 까지 적을 것)
     * 파라미터 바인딩
     * 반환타입의 자유로움 -> 단건일 경우 주로 Optional 사용된다.
     * Dto 직접 조회 제공


## 오류의 변환
repository 계층의 예외는 DB 접근 기술에 따라서 다를 수 있다.

따라서 Spring 에서는 각 DB 접근 기술들의 다른 예외 처리를 공통화 하기 위해서 예외들을 추상화 하게 된다.

개발자 입장에서는 Spring이 추상화한 Exception 만을 가지고 오류를 처리하면 되겠다.

## 페이징과 정렬
spring 은 모든 data 접근 기술과 관련하여 페이징과 정렬 을 공통화 시켜놓았다.

패키지의 중요성이 여기서 나온다. 패키지를 상세히 보자

* 파라미터 타입: Pageable, Sort  
* 반환 타입: Page, Slice
  * Page : total count query 발생 => countQuery 를 따로 지정해서 성능 최적화
  * Slice : count query 발생 x limit + 1 쿼리 
  * 주의 할 점 : 페이징을 통해서 Entity 바로 반환은 별로 map 을 사용해서 Dto 변환후 사용하자


## 벌크 연산
벌크연산은 기본적으로 영속성 컨텍스트를 사용하는 것이 아닌 db와 직접 통신, 물론 EntityManager를 통해서 query 를 execute 하지만
데이터 베이스와 영속성 컨텍스트와의 정합성을 보장하지 않는다.

따라서 추가적으로 DB 커넥션을 유지하면서 작성하려면 벌크연산전 flush 외에도, clear 를 하는 것이 중요하다고 생각한다.

## Entity Graph
jpa 의 fetch join 을 쉽게 지원하고자 사용한다.

@EntityGraph 사용하여 작성

## JPA hint, Lock => 성능 최적화와 동시성 관련
JPA hibernate 구현체에게 제공하는 hint 이다. 예를 들면 readOnly true를 할 경우 dirty checking 을 하지 않는다.
즉 영속성 컨텍스트에 스냅샷을 만들지 않게 된다. 

하지만 성능상의 큰 이점을 보기는 힘들다.

> @QueryHints(value = { @QueryHint(name = "org.hibernate.readOnly", value = "true")}, forCounting = true)

트랜잭션은 ACID 한 성질을 가지고 있고 이로 인해서 동시성을 어느정도는 해결 할 수 있다.

하지만 트랜잭션은 자원을 수정하는 critical section에 대해서는 해결하지 못하는 현상이 있다 (이를 테면 갱신 분실 문제)

따라서 우리는 데이터베이스에서 제공하는 Lock을 이용하여 시스템의 안정성을 도모할 수 있다.
1. 낙관적 락 => 버전 관리를 통해서 수정 여부를 확인한다.
2. 비관적 락 => 데이터베이스 row에 

## Custom 기능 지원
이를 테면 Spring Data Jpa 를 사용하는데 복잡한 쿼리, 동적 쿼리 등을 사용하기 위해서 따로 Query Method 가 필요한 경우가 있다.
만약 Spring Data Jpa 를 사용한다면 JpaRepository 인터페이스를 상속받아서 사용을 하고 있을 텐데 사용자 정의 repo 클래스를 사용하고
싶다면 기존 repo 인터페이스를 상속받고 모든것을 override 해서 사용해야 한다.

따라서 Spring Data Jpa 는 이름명시와 인터페이스로 커스텀 기능을 제공한다.
1. 사용자 정의 repo 를 위한 인터페이스를 만든다 (관례상 xxxRepositoryCustom 을 이용한다.)
2. 1번의 인터페이스를 상속한 구현체를 작성한다. 여기서 주의 할 점이 있는데 꼭 (xxxRepositoryImpl, 사옹자인터페이스Impl) 로 작성해야하는 것이다.
    물론 따로 이름 설정을 할 수 있으나 굳이? 싶다
3. spring data jpa 는 이를 인식하고 스프링 빈으로 등록하게 된다.

## Auditing 기능
이벤트 리스너를 활용하여 jpa 이벤트 발생시 Listener 가 동작하여 작동하게 된다.
jpa를 사용할 땐 @PrePersist, @PostPersist ...

spring data jpa d에서는 간단하게 AuditingEntityListener 를 사용해 (미리 설정 끝나있음) 쉽게 사용 가능하다.

