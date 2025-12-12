package family_tree.logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BusinessLoggingAspect {

    @Autowired
    @Lazy
    private BusinessLogger businessLogger;

    @AfterReturning(pointcut = "execution(* family_tree.service.*.*(..))", returning = "result")
    public void logServiceSuccess(JoinPoint joinPoint, Object result) {
        String method = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        businessLogger.logInfo("SUCCESS", method, args, result);
    }

    @AfterThrowing(pointcut = "execution(* family_tree.service.*.*(..))", throwing = "exception")
    public void logServiceException(JoinPoint joinPoint, Throwable exception) {
        String method = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        businessLogger.logError(method, args, exception);
    }
}
