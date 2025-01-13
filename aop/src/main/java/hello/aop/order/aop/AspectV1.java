package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class AspectV1 {

    /**
     * @Around 의 값은 포인트컷, @Around 의 메서드는 어드바이스가 된다
     */

    @Around("execution(* hello.aop.order ..*(..))") // 적용할 패키지와 그 하위 패키지들 (AspectJ의 포인트컷 표현식)
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature()); // join point 시그니처
        return joinPoint.proceed();
    }
}
