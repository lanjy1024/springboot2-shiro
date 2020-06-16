package com.example.demo.config.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author：lanjy
 * @date：2020/6/11
 * @description：
 */

@Configuration
@Slf4j
public class ShiroConfig {

    /**
     * 开启Shiro注解(如@RequiresRoles,@RequiresPermissions),
     * 需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    //1======开启shiro aop注解支持.
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
        log.info("1======ShiroConfig======开启shiro aop注解支持======注入======AuthorizationAttributeSourceAdvisor");
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    //2======权限管理/安全管理器，配置主要是Realm的管理认证
    @Bean
    DefaultWebSecurityManager securityManager() {
        log.info("2======ShiroConfig======配置主要是Realm的管理认证======注入======DefaultWebSecurityManager");
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(getRealm());
        // 自定义缓存实现 使用redis
        // securityManager.setCacheManager(cacheManager());
        // 自定义session管理 使用redis
//        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }


    //3======将自己的验证方式加入容器,Realm/shiro连接数据的桥梁
    @Bean
    Realm getRealm() {
        log.info("3======ShiroConfig======将自己的验证方式加入容器======注入======MyShiroRealm");
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        //凭证匹配器
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }



    //4======凭证匹配器（密码校验交给Shiro的SimpleAuthenticationInfo进行处理)
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        log.info("4======ShiroConfig======凭证匹配器======注入======HashedCredentialsMatcher");
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //散列的次数，比如散列两次，相当于 md5(md5(""));
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }

    /**
     * 5======Filter工厂
     * 设置安全管理器
     * 设置内置过滤器,实现权限相关的拦截
     * @return
     */
    @Bean
    ShiroFilterFactoryBean shiroFilterFactoryBean() {
        log.info("5======ShiroConfig======Filter工厂======注入======ShiroFilterFactoryBean");
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //安全管理器
        bean.setSecurityManager(securityManager());
        // 没有登陆的用户只能访问登陆页面
        bean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        bean.setSuccessUrl("/index");
        // 未授权页面，认证不通过跳转
        bean.setUnauthorizedUrl("/403");

        //设置内置过滤器,实现权限相关的拦截,从上到下顺序执行,一般将/**放到最下边,这是个坑,不然你会发现意想不到的种种鬼一样的问题
        Map<String, String> filterMap = new LinkedHashMap<String, String>();
        //shiro默认自带的登出,使用方式也很简单
        //filterMap.put("/logout", "logout");
        //开放登录接口
        filterMap.put("/login", "anon");
        //filterMap.put("/static/**", "anon");
        // swagger
        filterMap.put("/swagger**/**", "anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/v2/**", "anon");
        // 对所有用户认证
        filterMap.put("/**", "authc");
        bean.setFilterChainDefinitionMap(filterMap);

        return bean;
    }



    //6======shiro注解模式下，登录失败或者是没有权限都是抛出异常，并且默认的没有对异常做处理，配置一个异常处理
    @Bean(name="simpleMappingExceptionResolver")
    public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
        log.info("6======ShiroConfig======shiro注解模式下，登录失败或者是没有权限都是抛出异常，并且默认的没有对异常做处理，配置一个异常处理======注入======simpleMappingExceptionResolver");
        SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
        Properties mappings = new Properties();
        mappings.setProperty("DatabaseException", "databaseError");//数据库异常处理
        mappings.setProperty("UnauthorizedException","/403");
        r.setExceptionMappings(mappings);  // None by default
        r.setDefaultErrorView("error");    // No default
        r.setExceptionAttribute("exception");     // Default is "exception"
        return r;
    }


}