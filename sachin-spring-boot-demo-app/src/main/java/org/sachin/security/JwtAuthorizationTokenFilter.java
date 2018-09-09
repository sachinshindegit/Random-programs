package org.sachin.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sachin.security.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private CustomerService customerService;
    
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final String tokenHeader;

    public JwtAuthorizationTokenFilter(UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil, @Value("${jwt.header}") String tokenHeader) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenHeader = tokenHeader;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        logger.debug("processing authentication for '{}'", request.getRequestURL());

        final String requestHeader = request.getHeader(this.tokenHeader);
        String username = null;
        String authToken = null;
        String role = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            authToken = requestHeader.substring(7);
            try {
            	if(jwtTokenUtil.validateToken(authToken)){
            		username = jwtTokenUtil.getUsernameFromToken(authToken);
                    role = jwtTokenUtil.getRoleFromToken(authToken);
            	}else{
            		logger.warn("TOken validation failed");
            		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(null, null);
                	// The above line will create the auth object with authentication set to false by default
                	// This cannot be made to true manually either
                	SecurityContextHolder.getContext().setAuthentication(authentication);
            	}
            } catch (IllegalArgumentException e) {
                logger.error("an error occured during getting username from token", e);
            } catch (ExpiredJwtException e) {
                logger.warn("the token is expired and not valid anymore", e);
            }
        } else {
        	UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(null, null);
        	// The above line will create the auth object with authentication set to false by default
        	// This cannot be made to true manually either
        	SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.warn("couldn't find bearer string, will ignore the header");
        }
        logger.debug("checking authentication for user '{}'", username);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        	logger.debug("security context was null, so authorizating user");
            
            boolean  isValidUsernameFromToken = customerService.isValidUsername(username);
            if(isValidUsernameFromToken){
            	List<GrantedAuthority>authorities = new ArrayList<GrantedAuthority>();
            	authorities.add(new AuthorizationGranted(role));
            	UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken((String)username, null, authorities);
            	// The above line will create the auth object with authentication set to true by default
            	// This cannot be made to true manually either
            	SecurityContextHolder.getContext().setAuthentication(authentication);
            	logger.debug("Username from token is invalid");
            }else{
            	UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null);
            	// The above line will create the auth object with authentication set to false by default
            	// This cannot be made to true manually either
            	SecurityContextHolder.getContext().setAuthentication(authentication);
            	logger.debug("Username from token is invalid");
            	
            }
        }

        chain.doFilter(request, response);
    }
    
    
}
