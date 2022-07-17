package inflearn.jdbc.exception.basic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

/**
 * 2가지 문제점을 확인할 수 있다.
 * 1. 복구 불가능한 예외
 * 2. 의존 관계에 대한 문제
 *
 * checked exception - throws exception 방법은 사용하면 안된다. 모든 예외를 던진다고 선언하는 것인데, 결과적으로 어떤
 * 예외를 잡고 어떤 예외를 던지는지 알 수 없기 때문이다. 체크 예외를 사용한다면 잡을 건 잡고 던질 예외는 명확하게 던지도록 선언해야 한다.
 *
 * JPA 기술도 런타임 예외를 사용한다. 스프링도 대부분 런타임 예외를 제공한다.
 * 런타임 예외도 필요하면 잡을 수 있기 때문에 필요한 경우에는 잡아서 처리하고, 그렇지 않으면 자연스럽게 던지도록 둔다. 그리고 예외를 공통으로 처리하는
 * 부분을 앞에 만들어서 처리하면 된다.
 * */

public class CheckedAppTest {

    @Test
    void checked(){
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

        public void logic() throws ConnectException, SQLException {
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient{
        public void call() throws ConnectException {
            throw new ConnectException("연결 실패");
        }
    }

    static class Repository{
        public void call() throws SQLException {
            throw new SQLException("ex");
        }
    }
}
