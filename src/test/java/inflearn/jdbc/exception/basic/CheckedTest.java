package inflearn.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Exception 을 상속받은 예외는 체크 예외가 된다
 * 체크 예외는 RuntimeException 클래스를 상속받지 않은 예외 클래스들이다. 체크 예외는 복구 가능성이 있는 예외 이므로 반드시 예외를
 * 처리하는 코드를 함께 작성해야 한다. 대포적으로 IOEXCEPTION, SQLException 등이 있으며, 예외를 처리하기 위해서는 catch 문으로 잡거나 throws
 * 를 통해 메소드 밖으로 던질 수 있다. 만약 예외를 처리하지 않으면 컴파일 에러가 발생한다.
 *
 * 정리
 * 1. Exception 예외를 받은 예외는 체크 예외가 된다
 * 2. MyCheckedException 는 Exception 을 상속 받았다. 그러므로 Exception 을 상속 받으면 체크 예외가 된다.
 * 3. 'RunTimeException' 을 상속받으면 언체크 예외가 된다.
 * 4. 'catch' 에 예외를 지정하면 해당 예외와 그 하위 타입 예외를 모두 잡아준다.
 * 5. 단순하게 throws Exception 은 굉장히 안 좋은 코드다 왜냐하면 모든 예외를 던진다는 기본적으로 깔고 들어간다
 * 그래서 내가 자세하게 던질 예외만 선언하는게 맞다
 *
 * 체크 예외의 장단점
 * 체크 예외는 예외를 잡아서 처리할 수 없을 때, 예외를 밖으로 던지는 'throws 예외' 를 필수로 선언해야 한다. 그렇지 않으면
 * 컴파일 오류가 발생한다. 이것 때문에 장단점이 동시에 존재한다.
 * 장점 : 개발자가 실수로 예외를 누락하지 않도록 컴파일러를 통해 문제를 잡아주는 훌륭한 안전 장치이다.
 * 단점 : 하지만 실제로는 개발자가 모든 체크 예외를 반드시 잡거나 던지도록 처리해야 하기 때문에, 너무 번거롭다. 크게 신경쓰고 싶지 않은
 * 예외까지 모두 챙겨야 한다.
 * */

@Slf4j
public class CheckedTest {

    @Test
    void checked_catch(){
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void checked_throw(){
        Service service = new Service();

        Assertions.assertThatThrownBy(()->service.callThrow())
                .isInstanceOf(MyCheckedException.class);
    }

    /**
     * Exception 을 상속받은 체크 예외가 된다
     * */
    static class MyCheckedException extends Exception {
        public MyCheckedException(String message) {
            super(message);
        }
    }
    /**
     * Checked 예외는
     * 예외를 잡아서 처리하거나, 던지거나 둘중 하나를 필수로 선택해야 한다.
     * */

    static class Service{
        Repository repository = new Repository();

        /**
         * 예외를 잡아서 처리하는 코드
         * */

        public void callCatch(){
            try {
                repository.call();
            } catch (MyCheckedException e) {
                log.info("예외 처리, message={}", e.getMessage(), e);
            }
        }

        /**
         * 체크 예외를 밖으로 던지는 코드
         * 체크 예외는 예외를 잡지 않고 밖으로 던지려면 throws 예외를 메서드에 필수로 선언해야 한다
         * @Throws myCheckedException
         * */
        public void callThrow() throws MyCheckedException{
            repository.call();
        }
    }

    static class Repository {
        public void call() throws MyCheckedException {
            throw new MyCheckedException("ex");
        }
    }
}
