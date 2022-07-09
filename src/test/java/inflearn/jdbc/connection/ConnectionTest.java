package inflearn.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static inflearn.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {
    @Test
    void driverManager() throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection connection1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        log.info("connection={}, class={}", connection, connection.getClass());
        log.info("connection={}, class={}", connection1, connection1.getClass());
    }

    @Test
    void dataSourceDriverManager() throws SQLException {
        // DriverManagerDataSource - always new connection
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);// provided by Spring
        userDataSource(dataSource);
    }

    private void userDataSource(DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();
        Connection connection1 = dataSource.getConnection();

        log.info("connection={}, class={}", connection, connection.getClass());
        log.info("connection={}, class={}", connection1, connection1.getClass());
    }

    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        // connection Pool
        HikariDataSource hikariDataSource = new HikariDataSource(); // hikari is from hikari, automatically imports from spring
        hikariDataSource.setJdbcUrl(URL);
        hikariDataSource.setUsername(USERNAME);
        hikariDataSource.setPassword(PASSWORD);
        hikariDataSource.setMaximumPoolSize(10);
        hikariDataSource.setPoolName("hikariPool");

        userDataSource(hikariDataSource);
        Thread.sleep(1000);

        /*
15:58:59.987 [hikariPool connection adder] DEBUG com.zaxxer.hikari.pool.HikariPool - hikariPool - Added connection conn2: url=jdbc:h2:~/test user=SA
15:58:59.987 [hikariPool connection adder] DEBUG com.zaxxer.hikari.pool.HikariPool - hikariPool - Added connection conn3: url=jdbc:h2:~/test user=SA
15:58:59.987 [hikariPool connection adder] DEBUG com.zaxxer.hikari.pool.HikariPool - hikariPool - Added connection conn4: url=jdbc:h2:~/test user=SA
15:58:59.988 [hikariPool connection adder] DEBUG com.zaxxer.hikari.pool.HikariPool - hikariPool - Added connection conn5: url=jdbc:h2:~/test user=SA
15:58:59.988 [hikariPool connection adder] DEBUG com.zaxxer.hikari.pool.HikariPool - hikariPool - Added connection conn6: url=jdbc:h2:~/test user=SA
15:58:59.988 [hikariPool connection adder] DEBUG com.zaxxer.hikari.pool.HikariPool - hikariPool - Added connection conn7: url=jdbc:h2:~/test user=SA
15:58:59.989 [hikariPool connection adder] DEBUG com.zaxxer.hikari.pool.HikariPool - hikariPool - Added connection conn8: url=jdbc:h2:~/test user=SA
15:58:59.990 [hikariPool connection adder] DEBUG com.zaxxer.hikari.pool.HikariPool - hikariPool - Added connection conn9: url=jdbc:h2:~/test user=SA
15:58:59.991 [hikariPool connection adder] DEBUG com.zaxxer.hikari.pool.HikariPool - hikariPool - After adding stats (total=10, active=2, idle=8, waiting=0)
        여기서 보면 풀 을 보여준다. 그리고 현제 데이터베이스에 연결되어 있는 커넥션은 2이다 (connection, connection1). 그러므로 사용 가능한 커넥션 풀은 8개
        * */
    }
}
