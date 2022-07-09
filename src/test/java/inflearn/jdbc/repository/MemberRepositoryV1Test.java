package inflearn.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;

import inflearn.jdbc.connection.ConnectionConst;
import inflearn.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 repository;

    @BeforeEach
    void beforeEach() {
        //기본 DriverManager - 항상 새로운 커넥션을 획득
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        //커넥션 풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(ConnectionConst.URL);
        dataSource.setUsername(ConnectionConst.USERNAME);
        dataSource.setPoolName(ConnectionConst.PASSWORD);
        repository = new MemberRepositoryV1(dataSource);
    }

    @Test
    void crud() throws SQLException {
        //save
        Member member = new Member("memberV12", 10000);
        repository.save(member);

        //findById
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember={}", findMember);
        assertThat(findMember).isEqualTo(member);

        //update: money: 10000 -> 20000
        repository.update(member.getMemberId(), 20000);
        Member updatedMember = repository.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        //delete
        repository.delete(member.getMemberId());
        assertThatThrownBy(() -> repository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    // 16:30:09.955 [Test worker] INFO inflearn.jdbc.repository.MemberRepositoryV1 - get connection=HikariProxyConnection@1008612116 wrapping conn0: url=jdbc:h2:~/test user=SA, class=class com.zaxxer.hikari.pool.HikariProxyConnection
    //16:30:09.956 [Test worker] INFO inflearn.jdbc.repository.MemberRepositoryV1 - get connection=HikariProxyConnection@1779914089 wrapping conn0: url=jdbc:h2:~/test user=SA, class=class com.zaxxer.hikari.pool.HikariProxyConnection
    //16:30:09.958 [Test worker] INFO inflearn.jdbc.repository.MemberRepositoryV1 - get connection=HikariProxyConnection@254955665 wrapping conn0: url=jdbc:h2:~/test user=SA, class=class com.zaxxer.hikari.pool.HikariProxyConnection
    //16:30:10.010 [ housekeeper] DEBUG com.zaxxer.hikari.pool.HikariPool -  - Pool stats (total=1, active=0, idle=1, waiting=0)
    // 여기서 conn0 이유는 커넥션이 끝나고 close 하기 때문에 같은 conn0 번만 사용하게 되는 것이다.
}