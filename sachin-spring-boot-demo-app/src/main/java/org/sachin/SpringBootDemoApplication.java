package org.sachin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class SpringBootDemoApplication implements CommandLineRunner{
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
    public static void main(String[] args) {
    	SpringApplication.run(SpringBootDemoApplication.class, args);
    }
    
    @Override
    public void run(String... strings) throws Exception {
    	// CommandLineRunner run() will get execute, just after applicationcontext is created and before springboot application starts up.
    	// It accepts the argument, which are passed at time of server startup.
    	
    	 jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
    	 String query = "CREATE TABLE IF NOT EXISTS customers ("+
			"id INTEGER PRIMARY KEY AUTO_INCREMENT,"+
			"username varchar,"+
			"password varchar, role varchar)";
         jdbcTemplate.execute(query);
         jdbcTemplate.execute("INSERT INTO customers(" +
                 "username, password, role) VALUES ('admin','password','admin')");
         jdbcTemplate.execute("INSERT INTO customers(" +
                 "username, password, role) VALUES ('nonadmin','password','customer')");
    }


        
}
