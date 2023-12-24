package com.dev.zine.api.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.dev.zine.dao.UserDAO;
import com.dev.zine.model.User;
import com.dev.zine.service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    /** The JWT Service. */
    private JWTService jwtService;
    /** The Local User DAO. */
    private UserDAO localUserDAO;

    /**
     * Constructor for spring injection.
     * 
     * @param jwtService
     * @param localUserDAO
     */
    public JWTRequestFilter(JWTService jwtService, UserDAO localUserDAO) {
        this.jwtService = jwtService;
        this.localUserDAO = localUserDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);
            try {
                String username = jwtService.getEmail(token);
                Optional<User> opUser = localUserDAO.findByEmailIgnoreCase(username);
                if (opUser.isPresent()) {
                    User user = opUser.get();
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
                            null, new ArrayList());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JWTDecodeException ex) {
                System.out.println("caught filet error");
            }
        }
        filterChain.doFilter(request, response);
    }

}