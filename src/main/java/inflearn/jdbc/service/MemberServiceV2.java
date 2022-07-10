package inflearn.jdbc.service;

import inflearn.jdbc.domain.Member;
import inflearn.jdbc.repository.MemberRepositoryV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 파라미터 연동, 풀을 고려한 종료
 * */
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV1 memberRepositoryV1;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        Connection con = dataSource.getConnection();
        try {
            con.setAutoCommit(false); // 트랜잭션 시작
            // 비즈니스 로직
            Member fromMember = memberRepositoryV1.findById(fromId);
            Member toMember = memberRepositoryV1.findById(toId);

            memberRepositoryV1.update(fromId, fromMember.getMoney() - money);
            validation(toMember);
            memberRepositoryV1.update(toId, toMember.getMoney() + money);

            con.commit();   // 성공시 커밋
        } catch (Exception e) {
            con.rollback(); // rollback
            throw new IllegalStateException(e);
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);    // connection pool 다시 true 로 복귀
                    con.close();
                } catch (Exception e) {
                    log.info("error",e);
                }
            }
        }

    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
