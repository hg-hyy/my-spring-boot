package com.hg.hyy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

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
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/v1", "/home", "/spring.ws", "/stomp.ws", "/annotation.ws", "/annotation1.ws").permitAll()
                .antMatchers("/css/**", "/js/**", "/pic/**", "/favicon.ico").permitAll().anyRequest().authenticated()
                .and().formLogin().loginPage("/login").permitAll().and().logout().permitAll();
        http.csrf().disable();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {

        UserDetails user = User.withDefaultPasswordEncoder().username("admin").password("111111").roles("USER").build();

        return new InMemoryUserDetailsManager(user);
    }

    // private static final String ENCODED_PASSWORD =
    // "$2a$10$AIUufK8g6EFhBcumRRV2L.AQNz3Bjp7oDQVFiO5JJMBFZQ6x2/R/2";
    // private final String ENCODED_PASSWORD =
    // passwordEncoder().encode("secret123");

    // @Override
    // protected void configure(AuthenticationManagerBuilder auth) throws Exception
    // {
    // auth.inMemoryAuthentication().passwordEncoder(passwordEncoder()).withUser("user").password(ENCODED_PASSWORD)
    // .roles("USER");
    // }

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    // return new BCryptPasswordEncoder();
    // }

}
