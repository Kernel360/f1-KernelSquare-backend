package com.kernel360.kernelsquare.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class Logging {
    //ToDo 로그 정책이 정해지면 리팩토링
    @Pointcut("execution(* com.kernel360.kernelsquare.domain.member.service.MemberService.*(..)) || " +
            "execution(* com.kernel360.kernelsquare.domain.question.service.QuestionService.*(..)) || " +
            "execution(* com.kernel360.kernelsquare.domain.answer.service.AnswerService.*(..)) || " +
            "execution(* com.kernel360.kernelsquare.domain.member_answer_vote.service.MemberAnswerVoteService.*(..)) || " +
            "execution(* com.kernel360.kernelsquare.domain.image.service.ImageService.*(..))")
    public void allService() {
    }

    @Around("allService()")
    public Object aroundServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        String fileFullName = signature.getDeclaringTypeName(); // 파일 전체 이름을 가져옵니다.
        String packageName = fileFullName.substring(0, fileFullName.lastIndexOf('.')); // 패키지 이름을 가져옵니다.
        String className = fileFullName.substring(fileFullName.lastIndexOf('.') + 1); // 클래스 이름을 가져옵니다.
        String methodName = signature.getName(); // 메서드 이름을 가져옵니다.

        try {
            long startTime = System.currentTimeMillis();

            log.info("Start - Package: " + packageName + ", Class: " + className + ", Method: " + methodName);

            // 메서드 실행
            Object result = joinPoint.proceed();

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            log.info("Method " + signature.toShortString() + " execution finished with duration " + executionTime + " ms");

            return result;
        } catch (Throwable e) {
            log.error("Method " + signature.toShortString() + " execution finished with error: " + e);

            throw e;
        }
    }

    /*일단은 레포지토리 메서드가 정상적으로 동작하면 DB에 반영됨을 전제로 함.*/
    @Pointcut("execution(* com.kernel360.kernelsquare.domain.member.repository.MemberRepository.*(..)) || " +
            "execution(* com.kernel360.kernelsquare.domain.question.repository.QuestionRepository.*(..)) || " +
            "execution(* com.kernel360.kernelsquare.domain.answer.repository.AnswerRepository.*(..)) || " +
            "execution(* com.kernel360.kernelsquare.domain.member_answer_vote.repository.MemberAnswerVoteRepository.*(..))")
    public void allRepository() {
    }

    @Around("allRepository()")
    public Object aroundRepositoryMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();

        try {
            long startTime = System.currentTimeMillis();

            Object result = joinPoint.proceed();

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            log.info("JPA " + signature.toShortString() + " execution time: " + executionTime + " ms");

            return result;
        } catch (Throwable e) {
            log.error("JPA " + signature.toShortString() + " execution finished with error: " + e);

            throw e;
        }
    }
}
