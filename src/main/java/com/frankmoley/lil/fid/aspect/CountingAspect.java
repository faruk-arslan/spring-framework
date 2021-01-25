package com.frankmoley.lil.fid.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
public class CountingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountingAspect.class);

    private static final Map<String,Integer> countMap=new HashMap<>();

    @Pointcut("@annotation(Countable)")
    public void executeCounting(){ }

    @Around(value = "executeCounting()")
    public Object countMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {

        Object returnValue=joinPoint.proceed();

        StringBuilder message=new StringBuilder("");
        // package + class name
        String decleration=joinPoint.getSignature().getDeclaringType().toString();
        String methodName=joinPoint.getSignature().getName();

        if (countMap.containsKey(methodName)) {
            int current=countMap.get(methodName);
            current++;
            countMap.put(methodName,current);
        } else {
            countMap.put(methodName,1);
        }

        countMap.forEach((k,v)->{
;            message.append("Method: "+k+" -- Called "+v+" times.");
        });
        LOGGER.info(message.toString());
        return returnValue;

    }
}
