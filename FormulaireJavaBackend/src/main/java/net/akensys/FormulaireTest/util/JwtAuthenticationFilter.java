package net.akensys.FormulaireTest.util;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String path = request.getRequestURI();

        // Ignore le filtre pour l'endpoint de login
        if (path.equals("/api/auth/login") || path.equals("/api/auth/signup") ||
        path.equals("/api/auth/init-demo")) {
            chain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String token = null;

        // Vérifie si le header Authorization contient un JWT
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendErrorResponse(response, "Missing or invalid Authorization header", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        token = authHeader.substring(7); // Extrait le token (sans "Bearer ")
        try {
            username = jwtUtil.extractUsername(token); // Tente d'extraire le username
            if (!jwtUtil.validateToken(token, username)) {
                sendErrorResponse(response, "Token is invalid or expired", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } catch (Exception e) {
            sendErrorResponse(response, "Error processing token: " + e.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Configure l'utilisateur si le token est valide et non authentifié
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User userDetails = new User(username, "", new ArrayList<>());
            JwtAuthenticationToken authentication = new JwtAuthenticationToken(userDetails, token, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continue la chaîne si aucune erreur
        chain.doFilter(request, response);
    }

// Méthode pour envoyer une réponse d'erreur JSON
    private void sendErrorResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.getWriter().write(String.format("{\"error\": \"%s\"}", message));
    }
}
