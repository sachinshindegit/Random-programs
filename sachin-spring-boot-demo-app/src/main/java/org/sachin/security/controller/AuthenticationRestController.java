package org.sachin.security.controller;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.sachin.exceptions.ExistingUserException;
import org.sachin.model.security.Customer;
import org.sachin.model.security.CustomerRole;
import org.sachin.security.JwtAuthenticationRequest;
import org.sachin.security.JwtTokenUtil;
import org.sachin.security.JwtUser;
import org.sachin.security.service.CustomerService;
import org.sachin.security.service.JwtAuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationRestController {
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	@Autowired
	CustomerService customerService;
	
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;
    
    @RequestMapping(value = "/check", method = { RequestMethod.GET })
    public ResponseEntity check() {
    	return new ResponseEntity(Collections.singletonMap("health", "ok"), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/getoken", method = { RequestMethod.POST})
    public ResponseEntity adminLogin(@RequestParam String username, @RequestParam String password){
    	if(username.length()==0 || password.length()==0){
    		throw new AuthenticationException("Bad credentials!");
    	}
    	
    	Customer result = null;
    	try {
			result = customerService.validateCredentials(username, password);			
		} catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }
    	
    	if(result!=null){
    		final String token = jwtTokenUtil.genToken(username,result.getRole().toString());
        	return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    	}else{
    		return new ResponseEntity<String>("Authentication failed", HttpStatus.BAD_REQUEST);
    	}
    	
	}
    
    @RequestMapping(value = "/adduser", method = { RequestMethod.POST })
    public ResponseEntity adduser(@RequestParam String username, @RequestParam String password) {
    	if(username.length()==0 || password.length()==0){
    		return new ResponseEntity<String>("Invalid input for username or password of new user", HttpStatus.BAD_REQUEST);
    	}
    	if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
    		GrantedAuthority authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next();
    		if(authority.getAuthority().equalsIgnoreCase("admin")){
    			Customer cust = null;
    	    	try {
    				cust = customerService.addCustomer(username, password);
    				return new ResponseEntity(Collections.singletonMap("Created a new user with id", cust.getId()), HttpStatus.OK);
    			} catch (ExistingUserException e) {
    				return new ResponseEntity<String>("User already existing ", HttpStatus.BAD_REQUEST);
    			}
    		}else{
    			throw new AuthenticationException("UnAuthorized operation!");
    		}
    			    	
	    }else{
    		throw new AuthenticationException("UnAuthorized operation!");
    	}
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

}
