package com.hg.hyy.config;

import com.hg.hyy.service.SysUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * spring-Security相关配置
 *
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SysUserDetailService sysUserDetailService;
    private AccessDeniedHandler myAccessDeniedHandler;

    @Autowired
    public void setMyAccessDeniedHandler(AccessDeniedHandler myAccessDeniedHandler) {
        this.myAccessDeniedHandler = myAccessDeniedHandler;
    }

    public WebSecurityConfig(SysUserDetailService sysUserDetailService) {
        this.sysUserDetailService = sysUserDetailService;
    }

    // 加密
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(sysUserDetailService).passwordEncoder(passwordEncoder());
    }

    // 不加密
    // @Override
    // protected void configure(AuthenticationManagerBuilder auth) throws Exception
    // {
    // auth.userDetailsService(sysUserDetailService).passwordEncoder(bCryptPasswordEncoder());
    // }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/v1/hello-spring", "/v1/greeting", "/v1/greeting1", "/spring.ws", "/stomp.ws",
                        "/annotation.ws", "/*")
                .permitAll().antMatchers("/v2/role").access("hasRole('ADMIN')")
                // 在mySecurityUserDetails中添加角色时需要添加上ROLE_前缀
                .regexMatchers("/main1.html").hasRole("ADMIN")
                // 登录成功之后具有某一个权限才能访问该页面（字母严格区分大小写）
                .regexMatchers("/main1.html").hasAuthority("admin")
                .antMatchers("/css/**", "/js/**", "/pic/**", "/favicon.ico").permitAll().anyRequest()
                .access("@myAccessImpl.hasPermit(request,authentication)").anyRequest()
                .access("@myAccessServiceImpl.myUri(request,authentication)")
                // .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").defaultSuccessUrl("/v2/role").permitAll().and().logout()
                .permitAll();

        http.csrf().disable();

        // 统一的403页面
        http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);

        // http.authorizeHttpRequests(authorize ->
        // authorize.mvcMatchers("/resources/**", "/signup", "/about").permitAll()
        // .mvcMatchers("/admin/**").hasRole("ADMIN").mvcMatchers("/db/**")
        // .access("hasRole('ADMIN') and hasRole('DBA')").anyRequest().denyAll());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/index.html", "/static/**", "/favicon.ico")
                // 给 swagger 放行；不需要权限能访问的资源
                .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**");
    }

    // 加密方式
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 重写密码加密校验方式:字符串存储
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        };
    }
}
