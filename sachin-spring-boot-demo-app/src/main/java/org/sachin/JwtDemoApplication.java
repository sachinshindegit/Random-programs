package org.sachin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class JwtDemoApplication implements CommandLineRunner{
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
    public static void main(String[] args) {
    	SpringApplication.run(JwtDemoApplication.class, args);
    }
    
    @Override
    public void run(String... strings) throws Exception {
    	 jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
    	 String query = "CREATE TABLE IF NOT EXISTS customers ("+
			"id INTEGER PRIMARY KEY AUTO_INCREMENT,"+
			"username varchar,"+
			"password varchar, role varchar)";
         jdbcTemplate.execute(query);
         jdbcTemplate.execute("INSERT INTO customers(" +
                 "username, password, role) VALUES ('sachin','password','admin')");
    }


        
}
