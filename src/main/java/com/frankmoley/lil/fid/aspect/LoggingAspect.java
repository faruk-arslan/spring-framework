package com.frankmoley.lil.fid.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
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

    // with around, we can handle anything before, anything after returning and
    // anything after throwing
    @Around(value = "executeLogging()")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        // before
        long startTime=System.currentTimeMillis();
        // execution
        Object returnValue=joinPoint.proceed();
        // after
        long totalTime=System.currentTimeMillis()-startTime;

        StringBuilder message=new StringBuilder("Method: ");
        message.append(joinPoint.getSignature().getName());

        message.append(" totalTime: ").append(totalTime).append("ms");

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
        // we sent back the return data in order to print it
        // it'll be printed out what we send here
        return returnValue;
    }

}
