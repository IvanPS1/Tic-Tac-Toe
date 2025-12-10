package com.tictactoe.web.filter;

import com.tictactoe.domain.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Component
public class AuthFilter implements Filter {
    private final UserService userService;

    public AuthFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        if (path.startsWith("/auth/") || path.equals("/")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            httpResponse.setStatus(401);
            httpResponse.getWriter().write("Authorization header required");
            return;
        }

        try {
            String base64Credentials = authHeader.substring("Basic ".length());
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            String[] values = credentials.split(":", 2);

            Optional<UUID> userId = userService.authenticate(values[0], values[1]);
            if (userId.isEmpty()) {
                httpResponse.setStatus(401);
                httpResponse.getWriter().write("Invalid credentials");
                return;
            }

            chain.doFilter(request, response);

        } catch (Exception e) {
            httpResponse.setStatus(401);
            httpResponse.getWriter().write("Invalid authorization header");
        }
    }
}
