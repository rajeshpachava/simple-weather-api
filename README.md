# Spring Boot Weather restful API

### Prerequisites
  * JDK 1.8.X
  * Maven 3.6.X
### Run 
  * Download/Clone the project on to your local machine
  * Run the project on your machine
    - mvn clean install
  * Start spring boot application
    - mvn spring-boot:run
  * Download the swagger specification
    - ex: curl -X GET -H 'Authorization: Basic ZGVtbzpkZW1v' -i 'http://localhost:8080/swagger.json' (username:password - demo:demo)
  * The swagger specification will describes the endpoints to populate and retrieve location and weather info and retrieved information using zipcode.
