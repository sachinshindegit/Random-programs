package org.sachin.security.service;

import java.util.List;
import java.util.Map;

import org.sachin.model.security.Customer;
import org.sachin.model.security.CustomerRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	public Customer validateCredentials(String username, String pass) throws BadCredentialsException{
		Customer cust = null;
		
		String sql = "SELECT ROLE FROM CUSTOMERS WHERE USERNAME='"+username+"' AND PASSWORD='"+pass+"'";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if(rows == null || rows.isEmpty()){
			throw new BadCredentialsException("Invalid credentials");
		}else{
			cust = new Customer();
		}
		for (Map<String, Object> row : rows) {
	        if((String)row.get("role") == "admin")
	        	cust.setRole(CustomerRole.ADMIN);
	        else
	        	cust.setRole(CustomerRole.CUSTOMER);
	    } 
		return cust;
	}
}
