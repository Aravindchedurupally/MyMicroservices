package com.sample.dental.smile.dentail.work.flow.authToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String authorizationHeader = request.getHeader("Authorization");

		String username = null;
		String jwt = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			try {
				username = jwtTokenUtil.extractUsername(jwt);
	            String firstName = jwtTokenUtil.extractClaim(jwt, "firstName");
	            String lastName = jwtTokenUtil.extractClaim(jwt, "lastName");
	            String patientId = jwtTokenUtil.extractClaim(jwt, "patientId");
	            String email = jwtTokenUtil.extractClaim(jwt, "email");

	            JwtDetails jwtDetails = new JwtDetails(firstName, lastName, patientId, email);
	            JwtDetailsHolder.setJwtDetails(jwtDetails);
			} catch (ExpiredJwtException e) {
				logger.warn("JWT Token has expired");
			}
		}

		// If token is valid and no authentication is set, set authentication in
		// SecurityContext
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			if (jwtTokenUtil.validateToken(jwt, userDetails.getUsername())) {
				// Create UsernamePasswordAuthenticationToken
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// Set authentication to SecurityContext
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		chain.doFilter(request, response);
	    JwtDetailsHolder.clear();
	}
}
