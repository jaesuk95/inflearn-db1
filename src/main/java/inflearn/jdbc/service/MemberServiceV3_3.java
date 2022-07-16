package inflearn.jdbc.service;

import inflearn.jdbc.domain.Member;
import inflearn.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * 트랜잭션 - @Transaction AOP
 * */
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV3_3 {

    //    private final DataSource dataSource;
    // 이전에는 DataSource 를 직접 사용했지만
    // 현재 PlatformTransactionManager' 추상화된

    /*
    * 동작 순서
    * 1. 클라이언트가 요청을 한다 (비즈니스 로직 시작)
    * 2. 비즈니스 로직에서 transactionManager.getTransaction() 트랜잭션 시작
    * 3. 트랜잭션 매니저는 내부에서 DataSource 를 사용해서 커넥션을 먼저 생성한다.
    * 4. con.setAutoCommit(false) 으로 설정되며 트랜잭션을 시작한다
    * 5. 그리고 시작한 트랜잭션 매니저는 '트랜잭션 동기화 매니저' 에 보관을 해 놓는다
    * 6. 트랜잭션 동기화 매니저는 쓰레드 로컬에 커넥션을 보관한다. 따라서 멀티 쓰레드 환경에 안전하게 커넥션을 보관할 수 있다
    * 7. 멀티 쓰레드도 안전하게 보관된다
    *
    * 8. 트랜잭션 동기화 매니저에 커넥션이 보관된 이후, 이제 비즈니스 로직을 시작한다
    * 9. 이제 Repository 에서는 커넥션이 필요하면 '트랜잭션 동기화 매니저' 에서 가져와서 사용한다 (DataSourceUtils)
    * 10. 이 과정을 통해 자연스럽게 같은 커넥션을 사용하고, 트랜잭션도 유지된다.
    * 11. 마지막으로 데이터베이스에 접근하며 비즈니스 로직을 수행한다.
    *
    * // 커넥션 종료
    * 12. 비즈니스 로직이 끝나고 트랜잭션이 종료된다. 커밋 또는 롤백으로 트랜잭션 종료
    * 13. 트랜잭션을 종료하려면 동기화된 커넥션이 필요하다 (트랜잭션 동기화 매니저에서) 그리고 트랜잭션 동기화 매니저에서 꺼내온다 (커넥션을) 커밋으로 아니면 롤백으로 인해서
    * 14. 그리고 전체 리소스를 정리한다.
    * */
    // 트랜잭션 추상화 덕분에 서비스 코드는 이제 JDBC 기술에 의존하지 않는다. 이제 PlatformTransactionManager 에 의존하게 된다. (스프링꺼)
    // 만약 JPA 를 사용할 경우, DataSourceTransactionManager 를 'JpaTransactionManager' 로 변경해준면 된다.

    // 트랜잭션 템플릿 사용
    // 반복되는 try,catch,finally 없애자

    /*
    * 순수한 비즈니스 로직만 남기고, 트랜잭션 관련 코드는 모두 제거.
    * 스프링이 제공하는 트랜잭션 AOP 를 적용하기 위해 @Transactional 추가
    * springBootTest : 스프링 AOP 를 적용하려면 컨테이너가 필요하다. 이 어노테이션이 있으면 테스트시 스프링 부트를 통해
    * 스프링 컨테이너를 생성한다. 그리고 테스트에서 @AutoWired 등을 통해 스프링 컨테이너가 관리하는 빈들을 사용할 수 있다 .
    * */

    private final MemberRepositoryV3 memberRepositoryV3;

    @Transactional      // transaction 걸고 시작하겠다는 의미
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        bizLogic(fromId,toId,money);
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepositoryV3.findById(fromId);
        Member toMember = memberRepositoryV3.findById(toId);

        memberRepositoryV3.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepositoryV3.update(toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}


