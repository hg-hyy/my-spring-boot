package com.hg.hyy.config;

import com.hg.hyy.handlers.MyFailureHandler;
import com.hg.hyy.handlers.MySuccessHandler;
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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * spring-Security相关配置
 *
 * @author hyy
 * @since 2021-11-18
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final SysUserDetailService sysUserDetailService;

  private AccessDeniedHandler myAccessDeniedHandler;
  private DataSource dataSource;

  private PersistentTokenRepository jdbcTokenRepository;

  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Autowired
  public void setJdbcTokenRepository(PersistentTokenRepository jdbcTokenRepository) {
    this.jdbcTokenRepository = jdbcTokenRepository;
  }

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
                    "/*",
                    "/file/**",
                    "/test/**")
            .permitAll()
            .antMatchers("/css/**", "/js/**", "/pic/**", "/favicon.ico")
            .permitAll()
            .antMatchers("/view/role", "/view/wss")
            .access("hasRole('ADMIN')")
            .antMatchers("/v2/greet")
            .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
            // .and().rememberMe().tokenRepository( persistentTokenRepository() )
            .anyRequest()
            .access("@myAccessImpl.hasPermit(request,authentication)")
            // .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login") // 登录页面
            .loginProcessingUrl("/login") // 登录处理逻辑
            .defaultSuccessUrl("/view/role") // 默认登陆成功跳转
            .successForwardUrl("/view/role") // 登陆成功跳转
            .successHandler(new MySuccessHandler("/view/role")) // 自定义登陆成功处理
            .failureForwardUrl("/error") // 登录失败页面
            .failureHandler(new MyFailureHandler()) // 自定义登陆失败处理
            .permitAll()
            .and()
            .logout()
            .permitAll();
    // 开启csrf
    // http.csrf().disable();
    // 开启 Remember-Me 功能
    http.rememberMe()
            // 指定在登录时“记住我”的 HTTP 参数，默认为 remember-me
            .rememberMeParameter("remember-me")
            // .rememberMeServices()//自定义记住我
            // 设置 Token 有效期为 200s，默认时长为 2 星期
            .tokenValiditySeconds(200)
            // 指定 UserDetailsService 对象
            .userDetailsService(sysUserDetailService)
            .tokenRepository(jdbcTokenRepository);

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

  @Bean
  public PersistentTokenRepository MyTokenRepository() {
    JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
    jdbcTokenRepository.setDataSource(dataSource);
//    jdbcTokenRepository.setCreateTableOnStartup(true);//第一次创建表
    return jdbcTokenRepository;
  }
}
