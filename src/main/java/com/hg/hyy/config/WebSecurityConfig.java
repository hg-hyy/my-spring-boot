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
 * @author hyy
 * @date 2021-11-05
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private SysUserDetailService sysUserDetailService;

    public WebSecurityConfig(SysUserDetailService sysUserDetailService) {
        this.sysUserDetailService = sysUserDetailService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(sysUserDetailService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/v1", "/v2/*", "/v1/hello-spring", "/v1/greeting", "/v1/greeting1", "/endpoint.ws",
                        "/spring.ws", "/stomp.ws", "/annotation.ws")
                .permitAll().antMatchers("/css/**", "/js/**", "/pic/**", "/favicon.ico").permitAll().anyRequest()
                .authenticated().and().formLogin().loginPage("/login").defaultSuccessUrl("/v1").permitAll().and()
                .logout().permitAll();
        http.csrf().disable();
    }

    // 测试使用已经废弃
    // @Bean
    // @Override
    // public UserDetailsService userDetailsService() {

    // UserDetails user =
    // User.withDefaultPasswordEncoder().username("admin").password("111111").roles("USER").build();

    // return new InMemoryUserDetailsManager(user);
    // }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**", "/js/**");
    }
    // private static final String ENCODED_PASSWORD =
    // "$2a$10$AIUufK8g6EFhBcumRRV2L.AQNz3Bjp7oDQVFiO5JJMBFZQ6x2/R/2";
    // private final String ENCODED_PASSWORD = passwordEncoder().encode("111111");

    // @Override
    // protected void configure(AuthenticationManagerBuilder auth) throws Exception
    // {
    // auth.inMemoryAuthentication().passwordEncoder(passwordEncoder()).withUser("user").password(ENCODED_PASSWORD)
    // .roles("USER");
    // }

    // 加密方式
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
