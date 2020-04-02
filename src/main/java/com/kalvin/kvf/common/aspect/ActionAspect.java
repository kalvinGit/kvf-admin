package com.kalvin.kvf.common.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.Platform;
import cn.hutool.http.useragent.UserAgentUtil;
import com.kalvin.kvf.common.annotation.Log;
import com.kalvin.kvf.common.utils.HttpServletContextKit;
import com.kalvin.kvf.modules.sys.entity.User;
import com.kalvin.kvf.modules.sys.service.ILogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


/**
 * 【作用】操作日志，切面处理类<br>
 * 【说明】（无）
 * @author Kalvin
 */
@Aspect
@Component
@Slf4j
public class ActionAspect {
    
	@Autowired
	private ILogService logService;
	
	@Pointcut("@annotation(com.kalvin.kvf.common.annotation.Log)")
	public void logPointCut() { 
		
	}

	@Before("logPointCut()")
	public void before(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		// 如果是退出登录请求。侧使用前置通知
		if ("logout".equals(method.getName())) {
			// 保存日志
			saveActionLog(joinPoint, 0, false);
		}
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		//执行方法
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;

		//保存日志
		saveActionLog(point, time, true);

		return result;
	}

	private void saveActionLog(JoinPoint joinPoint, long time, boolean isAround) {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		if (user == null) {
			return;
		}
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		String methodName = method.getName();

		if (isAround && "logout".equals(methodName)) {
			return;
		}

		com.kalvin.kvf.modules.sys.entity.Log actionLog = new com.kalvin.kvf.modules.sys.entity.Log();
		Log action = method.getAnnotation(Log.class);
		if(action != null){
			// 注解上的描述
			if (StrUtil.isNotBlank(action.value())) {
				actionLog.setOperation(action.value());
			} else {	// 如果注解上的描述为空，则默认使用方法名
				actionLog.setOperation(methodName);
			}
		}

		// 获取request
		HttpServletRequest request = HttpServletContextKit.getHttpServletRequest();
		// 设置IP地址
		String clientIP = ServletUtil.getClientIP(request);
		if ("0:0:0:0:0:0:0:1".equals(clientIP)) {
			clientIP = "127.0.0.1";
		}
		actionLog.setIp(clientIP);
		actionLog.setForwardAction(request.getRequestURI());

		// 设置浏览器和设备系统
		String header = request.getHeader("User-Agent");
		Browser browser = UserAgentUtil.parse(header).getBrowser();
		Platform platform = UserAgentUtil.parse(header).getPlatform();
		String browserName = browser.getName() + " " + browser.getVersion(header);
		String os = platform.getName();
		actionLog.setBrowser(browserName);
		actionLog.setOs(os);

		// 设置用户信息
		actionLog.setUserId(user.getId());
		actionLog.setUsername(user.getUsername());

		actionLog.setTime(time);
		// 保存系统日志
		logService.save(actionLog);
	}
}
