package com.github.xsajtlavai.todo.security.jwt;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String tokenHeader = getTokenHeader(request);

        if (JwtUtil.isTokenHeaderValue(tokenHeader)) {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(tokenHeader);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        super.doFilter(request, response, chain);
    }

    private String getTokenHeader(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        return httpRequest.getHeader(AUTHORIZATION);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        UsernamePasswordAuthenticationToken authToken = null;

        if (tokenHeader != null) {
            String user = JwtUtil.parseSubjectFromToken(tokenHeader);

            if (user != null) {
                authToken = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
        }
        return authToken;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) {

        String subject = ((User) authResult.getPrincipal()).getUsername();
        String tokenWithPrefix = JwtUtil.createTokenWithHeaderPrefix(subject);

        response.addHeader(ACCESS_CONTROL_EXPOSE_HEADERS, AUTHORIZATION);
        response.addHeader(AUTHORIZATION, tokenWithPrefix);
    }



}
