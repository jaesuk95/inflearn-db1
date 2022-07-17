package inflearn.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * 체크 vs 언체크 예외
 * 체크 예외: 예외를 잡아서 처리하지 않으면 항상 Throws 에 던지는 예외를 선언해야 한다
 * 언체크 예외: 예외를 잡아서 처리하지 않아도 throws 를 생략할 수 있다.
 *
 * 정리
 * RuntimeException 과 그 하위 예외는 언체크 예외로 분류된다.
 * 언체크 예외는 말 그대로 컴파일러가 예외를 체크하지 않는다는 뜻이다.
 *
 * 언체크 예외는 체크 예외와 기본적으로 동일하다. 차이가 있다면 예외를 던지면 throws 를 선언하지 않고, 생략할 수 있다.
 * 이 경우 자동으로 예외를 던진다.
 *
 * Unchecked 장단점
 * 언체크 예외는 예외를 잡아서 처리할 수 없을 때, 예외를 밖으로 던지는 Throws 예외 를 생략할 수 있다. 이것 때문에 장점과 단점이
 * 동시에 존재한다.
 * 장점 : 신경쓰고 싶지 않은 언체크 예외를 무시할 수 있다. 체크 예외의 경우 처리할 수 없는 예외를 밖으로 던지려면 항상 Throws 예외 를
 * 선언해야 하지만, 언체크 예외는 이 부분을 생량할 수 있다. 이후에 설명하겠지만, 신경쓰로 싶지 않은 예외의 의존관계를 참조하지 않아도
 * 되는 장점이 있다.
 * 단점 : 언체크 예외는 개발자가 실수로 예외를 누락할 수 있다. 반면에 체크 예외는 컴파일러를 통해 예외 누락을 잡아준다.
 *
 * 정리 : 체크 예외와 언체크 예외의 차이는 사실 예외를 처리할 수 없을 때 예외를 밖으로 던지는 부분이 있다. 이 부분을 필수로 선언해야 하는가
 * 생략할 수 있는가의 차이다.
 * */

@Slf4j
public class UncheckedTest {

    @Test
    void unchecked_catch(){
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void unchecked_throw(){
        Service service = new Service();
        Assertions.assertThatThrownBy(() -> service.callThrow())
                .isInstanceOf(MyUncheckedException.class);
    }

    /**
     * RuntimeException 을 상속받은 예외는 언체크 예외가 된다.
     * */
    static class MyUncheckedException extends RuntimeException {
        public MyUncheckedException(String message) {
            super(message);
        }
    }

    /**
     * Unchecked 예외는
     * 예외를 잡거나, 던지지 않아도 된다.
     * 예외를 잡지 않으면 자동으로 밖으로 던진다
     * */
    static class Service{
        Repository repository = new Repository();

        /**
         * 필요한 경우 예외를 잡아서 처리하면 된다
         * */
        public void callCatch() {
            try {
                repository.call();

            } catch (MyUncheckedException e) {
                log.info("예외처리, message ={}",e.getMessage(),e);
            }
        }

        /**
         * 예외를 잡지 않아도 된다. 자연스럽게 상위로 넘어간다
         * 체크 예외와 다르게 throws 예외 선언을 하지 않아도 된다.
         * */
        public void callThrow(){
            repository.call();
        }

    }

    // throws 생략
    static class Repository{
        public void call() {
            throw new MyUncheckedException("ex");
        }
    }
}
