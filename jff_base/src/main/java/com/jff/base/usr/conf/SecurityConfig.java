package com.jff.base.usr.conf;

import com.jff.base.usr.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity //需要注解到 WebSecurityConfigurerAdapter 的子类上才可以
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    /**
     * 注册自己实现的用户验证方法
     * @return
     */
    @Bean
    UserDetailsService userService(){
        return new UserService();
    }

    /**
     *

     BCryptPasswordEncoder相关知识：

     用户表的密码通常使用MD5等不可逆算法加密后存储，为防止彩虹表破解更会先使用一个特定的字符串（如域名）加密，然后再使用一个随机的salt（盐值）加密。

     特定字符串是程序代码中固定的，salt是每个密码单独随机，一般给用户表加一个字段单独存储，比较麻烦。

     BCrypt算法将salt随机并混入最终加密后的密码，验证时也无需单独提供之前的salt，从而无需单独处理salt问题。

     * @return 添加密码加密算法
     */
    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 权限配置入口
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
                 http
                .csrf().disable()
                .authorizeRequests()
                .mvcMatchers("/wx/login").permitAll()
                .antMatchers("/admin/**").hasRole("Role_Admin")
                .anyRequest().authenticated();
    }

    /**
     * 添加自定义验证逻辑
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService()).passwordEncoder(passwordEncoder());
        auth.inMemoryAuthentication().withUser("admin").password("admin@3852").roles("Role_admin");
    }
}
