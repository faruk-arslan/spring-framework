package com.frankmoley.lil.fid.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

@Component
@Aspect
public class LoggingAspect {

    private static final Logger LOGGER= LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("@annotation(Loggable)")
    public void executeLogging() {

    }

    // we als acces twhat the method returns in addition to the data of the method
    // we do all of that after the call executed
    // if the method returns an exception, this will never be executed, because
    // this is after returning not after throwing
    @AfterReturning(value = "executeLogging()", returning = "returnValue")
    public void logMethodCall(JoinPoint joinPoint, Object returnValue){
        StringBuilder message=new StringBuilder("Method: ");
        message.append(joinPoint.getSignature().getName());
        // be careful on production code, args could contain sensitive information
        Object[] args=joinPoint.getArgs();
        if(args!=null && args.length>0){
            message.append(" args=[ | ");
            Arrays.asList(args).forEach(arg->{
                message.append(arg).append(" | ");
            });
            message.append("]");
        }
        if(returnValue instanceof Collection){
            message.append(", returning: ").append(((Collection<?>) returnValue).size()).append(" instance(s)");
        }else{
            message.append(", returning ").append(returnValue.toString());
        }

        LOGGER.info(message.toString());
    }

}
