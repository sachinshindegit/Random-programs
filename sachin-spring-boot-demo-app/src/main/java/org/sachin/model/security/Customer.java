package org.sachin.model.security;

public class Customer {
	
	private int id;
	private String username;
	private String password;
	private CustomerRole role;
	
	public Customer(){}
	
	public Customer(String u, String pass, CustomerRole role){
		this.username = u;
		this.password = pass;
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public CustomerRole getRole() {
		return role;
	}

	public void setRole(CustomerRole role) {
		this.role = role;
	}
	
	
}
