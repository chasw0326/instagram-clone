package com.example.instagram2.security.filter;

import com.example.instagram2.security.service.InstaUserDetailsService;
import com.example.instagram2.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * <code>ApiCheckFilter</code><br>
 * jwt를 체크하는 필터입니다.
 * @author chasw326
 */
@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    private final AntPathMatcher antPathMatcher;
    private final String pattern;
    private final JWTUtil jwtUtil;
    private final String[] excludes;

    @Autowired
    private InstaUserDetailsService userDetailsService;

    /**
     * 체크할 경로와 제외할 경로를 지정할 수 있습니다.
     * @param pattern jwt체크할 경로패턴
     * @param excludes jwt체크제외할 경로패턴
     * @param jwtUtil
     */
    public ApiCheckFilter(String pattern, String[] excludes, JWTUtil jwtUtil) {
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
        this.jwtUtil = jwtUtil;
        this.excludes = excludes;
    }

    /**
     * 토큰 확인후 SecurityContextHolder에 담습니다.<br>
     * 실패하면 에러 메세지를 보내줍니다. (403, FAIL CHECK API TOKEN)
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {


        log.info("------------ApiCheckFilter in progress------------");
        log.info("RequestURI: " + request.getRequestURI());

        boolean needToCheckToken = antPathMatcher.match(pattern, request.getRequestURI());

        for (String exclude : excludes){
            if(antPathMatcher.match(exclude, request.getRequestURI())){
                needToCheckToken = false;
            }
        }

        log.info("토큰확인 해야하는지: " + needToCheckToken);

        if (needToCheckToken) {
            log.info("ApiCheckFilter......................................");
            log.info("ApiCheckFilter......................................");
            log.info("ApiCheckFilter......................................");

            String email = checkAuthHeader(request);

            // 올바른 jwt
            if (email.length() > 0) {
                UserDetails principal = userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        principal, principal.getPassword(), principal.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
                return;
            } else { // 올바르지 않은 jwt
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "FAIL CHECK API TOKEN";
                json.put("code", "403");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.print(json);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 헤더의 jwt를 체크합니다.
     * @param request
     * @return
     */
    private String checkAuthHeader(HttpServletRequest request) {

        String email = "";

        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            log.info("Authorization exist: " + authHeader);

            try {
                email = jwtUtil.validateAndExtract(authHeader.substring(7));
                log.info("validate result: " + email);
                return email;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return email;
    }
}
