# TODO

* While fetching temperature details from external weather API we need to convert the timestamp using timezone information to UTC timezone and store the DATE part in database.
Or else just convert to date time using the timestamp and timezone information available in the response and store the DATE part in DB.
* Need to have validation while adding new Location into database, we need to check if the location is valid using external weather APi location services if exist.
* Need to check if there is an API to fetch the temperature details for multiple zipcodes in a single request.
* Define a model class representing the current weather details response and use it in JSON ObjectMapper for deserialization.
* Persit the users in DB istead of maintaining in memory.
* Support of HTTPS required.
* Add more endpoints to the Weather API, like fetchByZipcodeForCurrentDay(), fetchByZipcodeForPreviousDay(), fetchByZipcodeForLast7Days(), fetchByZipcodeForLast30Days().
* Need to use spring transaction and properly manage the persisting and read of entities with in the transactions.
* Make HTTP client connection timeout property configurable.
* Create application.properties with production profile for integrating with external databases.
* Add support for monitoring and integrate with monitoring tools like prometheus and graphana.
* Make docker image for the application.
* Create helm charts for deploying in kubernetes cluster.
