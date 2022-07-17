package inflearn.jdbc.exception.basic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

/**
 * 2가지 문제점을 확인할 수 있다.
 * 1. 복구 불가능한 예외
 * 2. 의존 관계에 대한 문제
 * */

public class UnCheckedAppTest {

    @Test
    void unChecked(){
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(() -> controller.request()).isInstanceOf(Exception.class);
    }

    static class Controller {
        Service service = new Service();

        public void request() throws SQLException, ConnectException {
            service.logic();
        }
    }


    static class Service{
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic(){
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient{
        public void call(){
            throw new RuntimeConnectException("연결 실패");
        }
    }

    static class Repository{
        public void call(){
            try {
                runSQL();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException{
        public RuntimeConnectException(String message){
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException{
        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }
}
