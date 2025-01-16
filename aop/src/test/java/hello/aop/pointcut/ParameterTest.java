package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(ParameterTest.ParameterAspect.class)
@SpringBootTest
public class ParameterTest {

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService Proxy={}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ParameterAspect {
        @Pointcut("execution(* hello.aop.member..*.*(..))")
        private void allMember() {}

        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object arg1 = joinPoint.getArgs()[0];
            log.info("[logArgs1] {}, arg={}", joinPoint.getSignature(), arg1);
            return joinPoint.proceed();
        }

        @Around("allMember() && args(arg, ..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, String arg) throws Throwable {
            log.info("[logArgs2] {}, arg={}", joinPoint.getSignature(), arg);
            return joinPoint.proceed();
        }

        // 파라미터 정보를 넘겨줌
        @Before("allMember() && args(arg, ..)")
        public void logArgs3(String arg) throws Throwable {
            log.info("[logArgs3] arg={}", arg);
        }

        // 타입 정보를 넘겨줌
        @Before("allMember() && this(obj)") // 타입의 프록시 객체
        public void thisArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[this] {}, obj={}", joinPoint.getSignature(), obj.getClass());
        }

        // 타입 정보를 넘겨줌
        @Before("allMember() && target(obj)") // 타입의 구현체
        public void targetArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[target] {}, obj={}", joinPoint.getSignature(), obj.getClass());
        }

        // 클래스 어노테이션 정보를 넘겨줌
        @Before("allMember() && @target(obj)")
        public void atTargetArgs(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@target] {}, annotation={}", joinPoint.getSignature(), annotation.getClass());
        }

        // 클래스 어노테이션 정보를 넘겨줌
        @Before("allMember() && @within(obj)")
        public void atWithinArgs(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@within] {}, annotation={}", joinPoint.getSignature(), annotation.getClass());
        }

        // 메서드 어노테이션 정보를 넘겨줌
        @Before("allMember() && @annotation(obj)")
        public void atAnnotationArgs(JoinPoint joinPoint, MethodAop annotation) {
            log.info("[@within] {}, annotation={}", joinPoint.getSignature(), annotation.value());
        }
    }
}
