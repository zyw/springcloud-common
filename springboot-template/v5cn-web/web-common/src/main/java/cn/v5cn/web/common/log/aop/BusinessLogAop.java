package cn.v5cn.web.common.log.aop;

import cn.v5cn.web.common.Constants;
import cn.v5cn.web.common.log.BusinessLog;
import cn.v5cn.web.common.log.LogManager;
import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

/**
 * @author zyw
 */
@Aspect
@Order(Constants.BUSINESS_LOG_AOP)
public class BusinessLogAop {
    /**
     * 日志切入点
     *
     * @author xuyuxiang
     * @date 2020/3/23 17:10
     */
    @Pointcut("@annotation(cn.v5cn.web.common.log.BusinessLog)")
    private void getLogPointCut() {
    }

    /**
     * 操作成功返回结果记录日志
     *
     * @author xuyuxiang
     * @date 2020/3/20 11:51
     */
    @AfterReturning(pointcut = "getLogPointCut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        BusinessLog businessLog = method.getAnnotation(BusinessLog.class);
//        SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();

        String account = "";//Constants.UNKNOWN;
//        String loginName;
//        try {
//            loginName = UserInfoUtil.getLoginInfo().getAccount();
//        } catch (NotLoginException e) {
//            loginName = Constants.UNKNOWN;
//        }
//
//        if(StrUtil.isNotBlank(loginName)) {
////            account = sysLoginUser.getAccount();
//            account = loginName;
//        }
        //异步记录日志
        LogManager.instance().executeOperationLog(
                businessLog, account, joinPoint, JSON.toJSONString(result));
    }

    /**
     * 操作发生异常记录日志
     *
     * @author xuyuxiang
     * @date 2020/3/21 11:38
     */
    @AfterThrowing(pointcut = "getLogPointCut()", throwing = "exception")
    public void doAfterThrowing(JoinPoint joinPoint, Exception exception) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        BusinessLog businessLog = method.getAnnotation(BusinessLog.class);
        //        SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserWithoutException();

        String account = "";//Constants.UNKNOWN;
        //String loginName = StpUtil.getSession().getString(Constants.LOGIN_USER_NAME_KEY);
        String loginName;
//        try {
//            loginName = UserInfoUtil.getLoginInfo().getAccount();
//        } catch (NotLoginException e) {
//            loginName = Constants.UNKNOWN;
//        }
//
//        if(StrUtil.isNotBlank(loginName)) {
////            account = sysLoginUser.getAccount();
//            account = loginName;
//        }
        //异步记录日志
        LogManager.instance().executeExceptionLog(
                businessLog, account, joinPoint, exception);
    }
}
