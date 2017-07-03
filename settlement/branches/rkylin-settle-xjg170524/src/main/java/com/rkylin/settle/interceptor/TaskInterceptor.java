package com.rkylin.settle.interceptor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.settle.util.RedisTaskUtil;

/** 
 * 计划任务切面 
 * @author CaoYang 
 */  
@Aspect
public class TaskInterceptor {
	private static Logger logger = LoggerFactory.getLogger(TaskInterceptor.class);
	/*
	 * 定义切点 com.rkylin.settle.task 存放定时任务相关类的包
	 */
	@Pointcut("execution(* com.rkylin.settle.task.*.*(..))")
	private void anyMethod(){}//定义一个切入点
	/*
	 * redis标记定时任务状态的工具类  
	 * */
	@Autowired
	RedisTaskUtil redisTaskUtil;
	
	@Before("anyMethod()")  
    public void doAccessCheck(JoinPoint pjp){
//        System.out.println("前置通知");  
    }
	@AfterReturning("anyMethod()")  
    public void doAfter(){  
//        System.out.println("后置通知");  
    }  
      
    @After("anyMethod()")  
    public void after(){  
//        System.out.println("最终通知");  
    }  
	/*
	 * 例外通知
	 * */
    @AfterThrowing("anyMethod()")  
    public void doAfterThrow(JoinPoint pjp){
    	//类名 + 方法名
    	String nameAndSimpleName = pjp.getTarget().getClass().getSimpleName() + "_" + pjp.getSignature().getName();
    	//redis的key = 类名 + 方法名
    	String taskId = nameAndSimpleName;
    	//解锁定时任务
    	redisTaskUtil.doTaskOpen(taskId);
    }  
    /*
     * 环绕通知
     * */
    @Around("anyMethod()")  
    public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable{
    	//类名 + 方法名
    	String nameAndSimpleName = pjp.getTarget().getClass().getSimpleName() + "_" + pjp.getSignature().getName();
    	logger.info(">>> >>> >>> 准备执行 定时任务[" + nameAndSimpleName + "]  ... ..." );
    	//redis的key = 类名 + 方法名
    	String taskId = nameAndSimpleName;
    	//aop返回值
    	Object object = null;
    	//判断 定时任务锁 状态
        if(redisTaskUtil.doTaskClose(taskId)) {//封锁定时任务
        	logger.info(">>> >>> >>> 开始执行 定时任务[" + nameAndSimpleName + "]  ... ..." );	
        	object = pjp.proceed();
        	redisTaskUtil.doTaskOpen(taskId);//解锁定时任务
        	logger.info(">>> >>> >>> 执行完成 定时任务[" + nameAndSimpleName + "]  ... ..." );
        } else {//锁
        	logger.info(">>> >>> >>> 拒绝执行 定时任务[" + nameAndSimpleName + "], 此定时任务由其他服务正在执行!!! " );
        }
        return object;  
    }
}
