#\u8CC7\u6599\u5EAB\u9023\u63A5\u8A2D\u5B9A
#spring.datasource.url=jdbc:mysql://localhost:3306/fixlife01?serverTimezone=Asia/Taipei
#spring.datasource.username=root
#spring.datasource.password=666666

# Server port
server.port=8081

# JPA configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect


# Spring Boot management endpoints
management.endpoints.web.exposure.include=mappings

# Logging level configuration
logging.level.org.springframework.web=DEBUG

# Thymeleaf template configuration
spring.thymeleaf.enabled=true
spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html

# Enable Thymeleaf Layout Dialect if using layout templates
# Ensure to add the dependency in pom.xml if not already installed
logging.level.com.fixlife.controller=DEBUG
spring.web.resources.static-locations=classpath:/static/