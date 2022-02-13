package com.example.instagram2.config;

import com.example.instagram2.security.filter.ApiCheckFilter;
import com.example.instagram2.security.filter.ApiLoginFilter;
import com.example.instagram2.security.handler.ApiLoginFailHandler;
import com.example.instagram2.security.handler.LoginSuccessHandler;
import com.example.instagram2.security.service.InstaUserDetailsService;
import com.example.instagram2.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//@EnableWebSecurity
@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private InstaUserDetailsService userDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTUtil jwtUtil() {
        return new JWTUtil();
    }

    @Bean
    public ApiCheckFilter apiCheckFilter() {

        return new ApiCheckFilter("/user", jwtUtil());
    }

    @Bean
    public ApiLoginFilter apiLoginFilter() throws Exception {
        ApiLoginFilter apiLoginFilter = new ApiLoginFilter
                ("/login", jwtUtil());
        apiLoginFilter.setAuthenticationManager(authenticationManager());
        apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());

        return apiLoginFilter;
    }

    @Bean
    public LoginSuccessHandler successHandler() {
        return new LoginSuccessHandler(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/login").permitAll();

        http.formLogin().disable();
        http.csrf().disable();
        http.logout();
        http.oauth2Login().successHandler(successHandler());
        http.rememberMe().tokenValiditySeconds(60 * 60 * 24 * 7).userDetailsService(userDetailsService);
        http.addFilterBefore(apiCheckFilter(),
                UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(apiLoginFilter(),
                UsernamePasswordAuthenticationFilter.class);
    }

}

