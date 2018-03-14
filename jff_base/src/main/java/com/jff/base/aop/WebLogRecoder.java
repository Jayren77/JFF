package com.jff.base.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * web端controller日志记录
 */
@Aspect
public class WebLogRecoder {

    @Pointcut("execution(public com.jff.base.entity.IJffResponse com.jff.base..)")
    public void webLogRecoder(){

    }
}
