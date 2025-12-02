package com.aida.usefy_lite.security;

import com.aida.usefy_lite.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService,
                         UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1) Read Authorization header
        String authHeader = request.getHeader("Authorization");

        // If no Authorization header or no Bearer token → allow next filter and exit
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2) Extract token
        String jwt = authHeader.substring(7); // Remove "Bearer "
        String username = jwtService.extractUsername(jwt);

        // 3) If we have username AND the user is not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 4) Load the user from database
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 5) Validate the token
            if (jwtService.isTokenValid(jwt)) {

                // Create authentication object
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 6) Store authentication inside SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 7) Continue filter chain
        filterChain.doFilter(request, response);
    }
}

