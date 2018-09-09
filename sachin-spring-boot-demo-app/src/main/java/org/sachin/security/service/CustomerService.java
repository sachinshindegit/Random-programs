package org.sachin.security.service;

import java.util.List;
import java.util.Map;

import org.sachin.exceptions.ExistingUserException;
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
	
	/**
	 * Method will check whether the credentials are valid
	 * @param username
	 * @param pass
	 * @return
	 * @throws BadCredentialsException
	 */
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
	        if(row.get("role").toString().equals("admin")){
	        	cust.setRole(CustomerRole.ADMIN);
	        }else{
	        	cust.setRole(CustomerRole.CUSTOMER);
	        }
	    } 
		return cust;
	}
	
	/**
	 * Method will check whether username is valid
	 * @param username
	 * @return
	 */
	public boolean isValidUsername(String username){
		String sql = "SELECT ROLE FROM CUSTOMERS WHERE USERNAME='"+username+"'";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if(rows == null || rows.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * This method will add a new user to the database.
	 * @param username
	 * @param pass
	 * @return
	 * @throws ExistingUserException
	 */
	
	public Customer addCustomer(String username, String pass) throws ExistingUserException{
		
		String sql = "SELECT ROLE FROM CUSTOMERS WHERE USERNAME='"+username+"'";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if(rows == null || rows.isEmpty()){
			jdbcTemplate.execute("INSERT INTO customers(" +
	                 "username, password, role) VALUES ('"+username+"','"+pass+"','customer')");
		}else{
			throw new ExistingUserException("User already existing");
		}
		
		sql = "SELECT id FROM CUSTOMERS WHERE USERNAME='"+username+"'";
		rows = jdbcTemplate.queryForList(sql);
		Customer cust = new Customer();
		for (Map<String, Object> row : rows) {
	        cust.setId((Integer)row.get("id"));
	    } 
		return cust;
	}
}
