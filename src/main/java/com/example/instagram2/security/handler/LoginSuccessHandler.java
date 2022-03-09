package com.example.instagram2.security.handler;


import com.example.instagram2.security.dto.AuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <code>LoginSuccessHandler</code><br>
 * 로그인 성공했을때 작동합니다.
 * @author chasw326
 */
@Log4j2
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private PasswordEncoder passwordEncoder;

    public LoginSuccessHandler(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 로그인 성공한 뒤 소셜로그인을 했다면<br>
     * 사용자이름, 비밀번호를 변경할 수 있는 페이지로<br>
     * redirect 시킵니다.
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
        throws IOException, ServletException{

        log.info("----------------------------------------------");
        log.info("onAuthenticationSuccess 시작");


        AuthMemberDTO authMember = (AuthMemberDTO) authentication.getPrincipal();


        boolean fromSocial = authMember.isFromSocial();

        log.info("Need Modify Member?" + fromSocial);

        boolean passwordResult = passwordEncoder.matches("1111", authMember.getPassword());

        // 소셜로그인이라면 redirect
        if(fromSocial && passwordResult){
            redirectStrategy.sendRedirect(request, response, "소셜로그인 패스워드 이름 변경 주소");
        }
    }
}
