package com.jff.base.usr.conf;

import com.jff.base.usr.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@EnableWebSecurity
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
        auth.userDetailsService(userService());
        auth.inMemoryAuthentication().withUser("admin").password("admin@3852").roles("Role_admin");
    }
}
