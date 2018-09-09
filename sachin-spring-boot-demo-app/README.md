
## About

## Requirements
This demo is build with with Maven 3 and Java 1.8.

## Usage
Just start the application with the Spring Boot maven plugin (`mvn spring-boot:run`). The application is
running at [http://localhost:8080](http://localhost:8080).

There are three user accounts present to demonstrate the different levels of access to the endpoints in
the API and the different authorization exceptions:
```
Admin - admin:admin
User - user:password
Disabled - disabled:password (this user is disabled)
```

There are three endpoints that are reasonable for the demo:
```

GET /check - This endpoint is used to check if the service is up
POST /getoken - This end point is used to get token.
POST /adduser - This endpoint is used to add users. Only admin user can add user. (admin:password)
```