package com.example.instagram2.config;

import com.example.instagram2.security.filter.ApiCheckFilter;
import com.example.instagram2.security.filter.ApiLoginFilter;
import com.example.instagram2.security.handler.LoginFailHandler;
import com.example.instagram2.security.handler.LoginSuccessHandler;
import com.example.instagram2.security.service.InstaUserDetailsService;
import com.example.instagram2.exception.ArgumentCheckUtil;
import com.example.instagram2.util.AuthUtil;
import com.example.instagram2.security.util.JWTUtil;
import com.example.instagram2.security.util.PasswordUtil;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * <code>SecurityConfig</code><br>
 *
 * @author chasw326
 */
@EnableWebSecurity
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

    /**
     * 서비스넘어가기 전에 컨트롤러에서 예외검사 등을 하려고 만들었습니다.
     */
    @Bean
    public ArgumentCheckUtil argumentCheckUtil() {
        return new ArgumentCheckUtil();
    }

    @Bean
    public PasswordUtil passwordUtil() {
        return new PasswordUtil();
    }

    @Bean
    public AuthUtil authUtil() {
        return new AuthUtil(passwordEncoder(), passwordUtil());
    }

    private final String[] excludePaths =
            new String[]{
                    "/accounts/signup", "/login", "/v3/api-docs",
                    "/configuration/ui", "/swagger-resources/**",
                    "/configuration/security", "/swagger-ui/**",
                    "/webjars/**", "/swagger/**",
                    "/static/css/**", "/static/js/**", "/favicon.ico"};

    /**
     * 모든 경로에 대해 jwt체크를 하고,<br>
     * excludesPaths로 체크하지 않을 경로들을 제외합니다.
     */
    @Bean
    public ApiCheckFilter apiCheckFilter() {
        return new ApiCheckFilter("/**", excludePaths, jwtUtil());
    }

    /**
     * 모든 경로에 대해 jwt체크를 할것임으로<br>
     * 체크가 필요없는 login은 필터로 합니다.
     */
    @Bean
    public ApiLoginFilter apiLoginFilter() throws Exception {
        ApiLoginFilter apiLoginFilter = new ApiLoginFilter
                ("/login", jwtUtil());
        apiLoginFilter.setAuthenticationManager(authenticationManager());
        apiLoginFilter.setAuthenticationFailureHandler(new LoginFailHandler());

        return apiLoginFilter;
    }

    /**
     * 로그인 성공했을때 소셜유저를 구분하기 위한 클래스입니다.
     */
    @Bean
    public LoginSuccessHandler successHandler() {
        return new LoginSuccessHandler(passwordEncoder());
    }

    /**
     *
     * 세션이 아니라 jwt로 인증을 하기때문에 세션을 제외합니다.<br>
     * cors 필터 -> apiCheck 필터 -> apiLogin 필터 순으로 작동합니다.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/swagger-resources/**").permitAll();


        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.httpBasic().disable();
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

