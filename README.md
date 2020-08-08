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
### Examples
  * Add weather details into DB
    - curl -X PUT -H 'Content-Type: application/json' -H 'Authorization: Basic ZGVtbzpkZW1v' -i 'http://localhost:8080/weather/add?zipcode=524121&date=2019-12-28&temperature=140.0'
  * Retrieve weather info by zipcode and date
    - curl -X GET -H 'Authorization: Basic ZGVtbzpkZW1v' -i 'http://localhost:8080/weather/fetchByZipcodeAndDate?zipcode=524121&date=2019-12-28'
      - Output: {"id":1,"date":"2019-12-28","temperature":140.0}
  * Retrieve weather info by zipcode and in between startDate and endDate
    - curl -X GET -H 'Authorization: Basic ZGVtbzpkZW1v' -i 'http://localhost:8080/weather/fetchAllByZipcodeBetweenDates?zipcode=524121&startDate=2019-12-27&
      - Output: {"content":[{"id":2,"date":"2019-12-29","temperature":110.0}],"pageable":{"sort": {"unsorted":false,"sorted":true,"empty":false},"pageNumber":0,"pageSize":1,"offset":0,"paged":true,"unpaged":false},"totalElements":2,"totalPages":2,"last":false,"first":true,"sort":{"unsorted":false,"sorted":true,"empty":false},"numberOfElements":1,"size":1,"number":0,"empty":false}
