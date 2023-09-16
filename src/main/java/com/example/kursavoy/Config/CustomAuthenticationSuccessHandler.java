package com.example.kursavoy.Config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static CustomAuthenticationSuccessHandler authenticationSuccessHandler = null;
    private CustomAuthenticationSuccessHandler(){}
    public static CustomAuthenticationSuccessHandler getInstance() {
        if (authenticationSuccessHandler == null) {
            authenticationSuccessHandler = new CustomAuthenticationSuccessHandler();
        }
        return authenticationSuccessHandler;
    }
    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(authentication);
        if (response.isCommitted()) {
            return;
        }
        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("USER")) {
                return "/user/";
            } else if (grantedAuthority.getAuthority().equals("ADMIN")) {
                return "/admin/";
            }
        }
        return "/";
    }
}