package com.dev.zine.api.security;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
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
        if(tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization token is missing or invalid");
            return;
        }
        
        String token = tokenHeader.substring(7);
        try {
            String username = jwtService.getEmail(token);
            Optional<User> opUser = localUserDAO.findByEmailIgnoreCase(username);
            if (opUser.isPresent()) {
                User user = opUser.get();
                List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getType()));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
                        null, authorities); //principal, password, list of roles
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JWTDecodeException e) {
            System.out.println("JWT decoding failed: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Failed to decode JWT token.");
            return;
        } catch (JWTVerificationException ex) {
            System.out.println("caught filet error");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token.");
            return;
        } catch (Exception e) {
            System.out.println("An error occurred while processing the JWT: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "An error occurred while decoding the JWT token.");
            return;
        }
        filterChain.doFilter(request, response);
    }

}