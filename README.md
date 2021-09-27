# Users, products and comments v2
Microservices-based application to practice version 2 (didn't want to use different branches xD)

### Features
- Users service
  * Register account, login, get details, edit details and delete account
  * Role based
  * Administrator can do everything
  * Guest users can only retrieve and edit their own details, as well as to delete them
- Product service
  * Categories can be listed, created, edited and deleted
  * Products can be listed, created, edited and deleted
  * Products can be associated with categories
  * Only admin can create, edit and delete categories and products
  * Guests can only listed categories and products
- Comments service
  * Users can comment products
  * Users can list product comments
  * Admin can do everything
  * Guests can only list, edit and delete their own comments
  
### Class diagram
<p float="left">
<img src="https://github.com/CamiloDelReal/project-users-products-comments-microservices-2/blob/main/specs/exported/Class%20Diagram.png" width="80%" height="80%" />
</p>

### Entity relationship diagram
<p float="left">
<img src="https://github.com/CamiloDelReal/project-users-products-comments-microservices-2/blob/main/specs/exported/Entity%20Relationship%20Diagram.png" width="80%" height="80%" />
</p>

### Deployment diagram
<p float="left">
<img src="https://github.com/CamiloDelReal/project-users-products-comments-microservices-2/blob/main/specs/exported/Deployment%20Diagram.png" width="80%" height="80%" />
</p>

### Technologies used
- Spring Boot
  * Web application
  * Security
  * Validation
  * Actuator
  * JPA
  * WebFlux
- Spring Cloud
  * Configuration service
  * Discovery service
  * Gateway
  * Bus
  * Request/Response tracing
  * Feign
- Eureka
  * Discovery service provider
- Resilience4j
	* Circuit breaker
- RabbitMQ
	* Bus communication to update configuration in client services
- Sleuth and Zipkin
  * Distributed tracing system
- Prometheus and Grafana
  * Alert and monitoring
- Logstash, Elasticsearch and Kibana
  * Logs
- MySQL
- Lombok
- Json Web Token
- Model Mapper
- Docker
  * Deployment

### Others
- Script for packaging services
- Script for building docker images for services
- Docker compose deployment script
- You may need to configure some paths, credentials and urls inside configuration files based on you environment
