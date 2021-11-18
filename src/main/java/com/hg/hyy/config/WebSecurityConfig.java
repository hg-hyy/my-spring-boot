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
 * <p>/**
 *
 * @author hyy
 * @since 2021-11-18
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
        .antMatchers(
            "/v1/hello-spring",
            "/v1/greeting",
            "/v1/greeting1",
            "/spring.ws",
            "/stomp.ws",
            "/annotation.ws",
            "/v2/**",
            "/*",
            "/file/**",
            "/test/**")
        .permitAll()
        .antMatchers("/css/**", "/js/**", "/pic/**", "/favicon.ico")
        .permitAll()
        .antMatchers("/v2/role", "/v2/wss")
        .access("hasRole('USER')")
        .antMatchers("/v2/greet")
        .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
        // .and().rememberMe().tokenRepository( persistentTokenRepository() )
        .anyRequest()
        .access("@myAccessImpl.hasPermit(request,authentication)")
        // .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage("/login")
        .defaultSuccessUrl("/v2/role")
        .permitAll()
        .and()
        .logout()
        .permitAll();

    http.csrf().disable();

    // 统一的403页面
    http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);
    http.exceptionHandling().accessDeniedPage("/unauth.html");

    // http.authorizeHttpRequests(authorize ->
    // authorize.mvcMatchers("/resources/**", "/signup", "/about").permitAll()
    // .mvcMatchers("/admin/**").hasRole("ADMIN").mvcMatchers("/db/**")
    // .access("hasRole('ADMIN') and hasRole('DBA')").anyRequest().denyAll());
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
        .antMatchers("/index.html", "/static/**", "/favicon.ico")
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
