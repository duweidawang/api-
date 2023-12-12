package com.duwss.apibackend.aop;



import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.duwss.apibackend.annotation.AuthCheck;
import com.duwss.apibackend.common.ErrorCode;
import com.duwss.apibackend.exception.BusinessException;
import com.duwss.apibackend.model.entity.User;
import com.duwss.apibackend.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Aspect
@Component
public class AuthInterceptor {

    @Resource
    UserService userService;

    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint proceedingJoinPoint,AuthCheck authCheck) throws Throwable {
        //获取注解中的权限字段
        List<String> anyRoles = Arrays.asList(authCheck.anyRole()).stream().filter(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return StringUtils.hasText(s);
            }
        }).collect(Collectors.toList());

        String mustRole = authCheck.mustRole();

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        //获取当前登录的用户
        User loginUser = userService.getLoginUser(request);
        //拥有任意权限即可通过
        if(CollectionUtils.isNotEmpty(anyRoles)){
            String userRole = loginUser.getUserRole();
            if(!anyRoles.contains(userRole)){
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }
        //必须拥有某一个权限
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(mustRole)){
            String userRole = loginUser.getUserRole();
            if(!userRole.equals(mustRole)){
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);

            }

        }
        //通过校验 ，放行 直接执行目标方法
        return proceedingJoinPoint.proceed();





    }




}
