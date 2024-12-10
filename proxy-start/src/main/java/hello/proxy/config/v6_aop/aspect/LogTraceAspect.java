package hello.proxy.config.v6_aop.aspect;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Method;

@Slf4j
@Aspect // 해당 어노테이션이 있으면 어드바이저로 변환해준다
public class LogTraceAspect {

    private final LogTrace trace;

    public LogTraceAspect(LogTrace trace) {
        this.trace = trace;
    }

    // 어드바이저 구현
    @Around("execution(* hello.proxy.app..*(..)) && !execution(* hello.proxy.app..noLog(..))") // 포인트컷 (표현식 사용)
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable { // 어드바이스 로직
        TraceStatus status = null;
        try {
            String message = joinPoint.getSignature().toShortString();
            status = trace.begin(message);

            // 로직 호출
            Object result = joinPoint.proceed();

            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
