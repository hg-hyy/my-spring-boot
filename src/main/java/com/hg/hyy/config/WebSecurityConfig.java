package com.hg.hyy.config;

import com.hg.hyy.service.SysUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * spring-Security相关配置
 *
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SysUserDetailService sysUserDetailService;

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
                .antMatchers("/v1", "/v2/*", "/v1/hello-spring", "/v1/greeting", "/v1/greeting1", "/endpoint.ws",
                        "/spring.ws", "/stomp.ws", "/annotation.ws", "/*")
                .permitAll().antMatchers("/css/**", "/js/**", "/pic/**", "/favicon.ico").permitAll().anyRequest()
                .authenticated().and().formLogin().loginPage("/login").defaultSuccessUrl("/v2/role").permitAll().and()
                .logout().permitAll();
        http.csrf().disable();
    }

    // 仅限demo使用，已经废弃。
    // @Bean
    // @Override
    // public UserDetailsService userDetailsService() {

    // UserDetails user =
    // User.withDefaultPasswordEncoder().username("admin").password("111111").roles("USER").build();

    // return new InMemoryUserDetailsManager(user);
    // }

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
