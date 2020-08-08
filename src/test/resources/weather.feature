@WeatherRestAPIFeature
Feature: Weather rest API endpoint tests


  Scenario: The application is started and data is populated in the Weather table
    Given the application has started successfully
    When the following data is added to the Weather table
      | zipcode | date       | temperature |
      | 1001    | 2020-01-01 | 10.0        |
      | 1001    | 2020-01-02 | 11.0        |
      | 1001    | 2020-01-04 | 15.0        |
      | 1005    | 2020-01-02 | 11.1        |
      | 1003    | 2020-05-01 | 10.1        |
      | 1003    | 2020-05-07 | 20.1        |
      | 1007    | 2020-07-31 | 32.21       |
      | 1007    | 2020-08-01 | 34.21       |
      | 1002    | 2020-08-30 | 43.1        |
      | 1009    | 2020-08-31 | 44.1        |
    Then the total number of records in the Location table is 6
    Then the total number of records in Weather table is 10

  Scenario Outline: Requesting weather data using fetchAllByZipCode endpoint
    When I request weather details by zipcode "<zipcode>"
    Then I expect weather details in the content as "<content>"
    Examples:
      | zipcode | content                                                                                                                                           |
      | 1001    | [{"id":1,"date":"2020-01-01","temperature":10.0},{"id":2,"date":"2020-01-02","temperature":11.0},{"id":3,"date":"2020-01-04","temperature":15.0}] |
      | 1003    | [{"id":5,"date":"2020-05-01","temperature":10.1},{"id":6,"date":"2020-05-07","temperature":20.1}]                                                 |
      | 1007    | [{"id":7,"date":"2020-07-31","temperature":32.21},{"id":8,"date":"2020-08-01","temperature":34.21}]                                               |
      | 1002    | [{"id":9,"date":"2020-08-30","temperature":43.1}]                                                                                                 |
      | 1006    | []                                                                                                                                                |

  Scenario Outline: Requesting weather data using fetchAllByZipCode endpoint and using pagination
    When I request weather details with zipcode "<zipcode>", page "<page>", size "<size>" and sort "<sort>"
    Then I expect weather details in the content as "<content>"
    Examples:
      | zipcode | page | size | sort             | content                                                                                                                                           |
      | 1001    | 0    | 5    | date,asc         | [{"id":1,"date":"2020-01-01","temperature":10.0},{"id":2,"date":"2020-01-02","temperature":11.0},{"id":3,"date":"2020-01-04","temperature":15.0}] |
      | 1001    | 1    | 5    | date,asc         | []                                                                                                                                                |
      | 1001    | 0    | 2    | date,asc         | [{"id":1,"date":"2020-01-01","temperature":10.0},{"id":2,"date":"2020-01-02","temperature":11.0}]                                                 |
      | 1001    | 1    | 2    | date,asc         | [{"id":3,"date":"2020-01-04","temperature":15.0}]                                                                                                 |
      | 1001    | 2    | 2    | date,asc         | []                                                                                                                                                |
      | 1001    | 0    | 5    | date,desc        | [{"id":3,"date":"2020-01-04","temperature":15.0},{"id":2,"date":"2020-01-02","temperature":11.0},{"id":1,"date":"2020-01-01","temperature":10.0}] |
      | 1001    | 0    | 1    | date,desc        | [{"id":3,"date":"2020-01-04","temperature":15.0}]                                                                                                 |
      | 1001    | 1    | 1    | date,desc        | [{"id":2,"date":"2020-01-02","temperature":11.0}]                                                                                                 |
      | 1001    | 2    | 1    | date,desc        | [{"id":1,"date":"2020-01-01","temperature":10.0}]                                                                                                 |
      | 1001    | 3    | 1    | date,desc        | []                                                                                                                                                |
      | 1006    | 0    | 4    | date,desc        | []                                                                                                                                                |
      | 1007    | 0    | 5    | temperature,desc | [{"id":8,"date":"2020-08-01","temperature":34.21},{"id":7,"date":"2020-07-31","temperature":32.21}]                                               |
      | 1007    | 0    | 1    | temperature,desc | [{"id":8,"date":"2020-08-01","temperature":34.21}]                                                                                                |
      | 1007    | 1    | 1    | temperature,desc | [{"id":7,"date":"2020-07-31","temperature":32.21}]                                                                                                |
      | 1007    | 2    | 1    | temperature,desc | []                                                                                                                                                |

  Scenario Outline: Requesting weather data using fetchByZipcodeAndDate endpoint
    When I request weather details using zipcode "<zipcode>" and date "<date>"
    Then I expect weather details in the response as "<response>"
    Examples:
      | zipcode | date       | response                                         |
      | 1001    | 2020-01-04 | {"id":3,"date":"2020-01-04","temperature":15.0}  |
      | 1001    | 2020-01-05 | null                                             |
      | 1003    | 2020-05-01 | {"id":5,"date":"2020-05-01","temperature":10.1}  |
      | 1007    | 2020-08-01 | {"id":8,"date":"2020-08-01","temperature":34.21} |
      | 1002    | 2020-08-01 | null                                             |
      | 1006    | 2020-08-01 | null                                             |

  Scenario Outline: Requesting weather data using fetchAllByZipcodeBetweenDates endpoint
    When I request weather details using zipcode "<zipcode>", startDate "<startDate>" and endDate "<endDate>"
    Then I expect weather details in the content as "<content>"
    Examples:
      | zipcode | startDate  | endDate    | content                                                                                                                                           |
      | 1001    | 2020-01-01 | 2020-01-04 | [{"id":1,"date":"2020-01-01","temperature":10.0},{"id":2,"date":"2020-01-02","temperature":11.0},{"id":3,"date":"2020-01-04","temperature":15.0}] |
      | 1001    | 2020-01-05 | 2020-01-05 | []                                                                                                                                                |
      | 1003    | 2020-05-01 | 2020-05-01 | [{"id":5,"date":"2020-05-01","temperature":10.1}]                                                                                                 |
      | 1007    | 2020-07-31 | 2020-08-01 | [{"id":7,"date":"2020-07-31","temperature":32.21},{"id":8,"date":"2020-08-01","temperature":34.21}]                                               |
      | 1007    | 2020-08-01 | 2020-08-09 | [{"id":8,"date":"2020-08-01","temperature":34.21}]                                                                                                |
      | 1002    | 2020-08-01 | 2020-08-01 | []                                                                                                                                                |
      | 1006    | 2020-08-01 | 2020-08-01 | []                                                                                                                                                |

  Scenario Outline: Requesting weather data using fetchAllByZipcodeBetweenDates endpoint and using pagination
    When I request weather details using zipcode "<zipcode>", startDate "<startDate>", endDate "<endDate>", page "<page>", size "<size>" and sort "<sort>"
    Then I expect weather details in the content as "<content>"
    Examples:
      | zipcode | startDate  | endDate    | page | size | sort      | content                                                                                                                                           |
      | 1001    | 2020-01-01 | 2020-01-04 | 0    | 5    | date,asc  | [{"id":1,"date":"2020-01-01","temperature":10.0},{"id":2,"date":"2020-01-02","temperature":11.0},{"id":3,"date":"2020-01-04","temperature":15.0}] |
      | 1001    | 2020-01-01 | 2020-01-04 | 1    | 5    | date,asc  | []                                                                                                                                                |
      | 1001    | 2020-01-01 | 2020-01-04 | 0    | 1    | date,desc | [{"id":3,"date":"2020-01-04","temperature":15.0}]                                                                                                 |
      | 1001    | 2020-01-01 | 2020-01-04 | 1    | 1    | date,desc | [{"id":2,"date":"2020-01-02","temperature":11.0}]                                                                                                 |
      | 1001    | 2020-01-01 | 2020-01-04 | 2    | 1    | date,desc | [{"id":1,"date":"2020-01-01","temperature":10.0}]                                                                                                 |
      | 1001    | 2020-01-01 | 2020-01-04 | 3    | 1    | date,desc | []                                                                                                                                                |
      | 1001    | 2020-01-01 | 2020-01-04 | 0    | 2    | date,asc  | [{"id":1,"date":"2020-01-01","temperature":10.0},{"id":2,"date":"2020-01-02","temperature":11.0}]                                                 |
      | 1001    | 2020-01-01 | 2020-01-04 | 1    | 2    | date,asc  | [{"id":3,"date":"2020-01-04","temperature":15.0}]                                                                                                 |
      | 1001    | 2020-01-05 | 2020-01-05 | 0    | 5    | id,desc   | []                                                                                                                                                |
      | 1001    | 2020-01-05 | 2020-01-05 | 1    | 5    | id,desc   | []                                                                                                                                                |

  Scenario Outline: Requesting weather data using fetchAllByZipcodeBetweenDates endpoint and passing invalid request params
    When I request weather details using zipcode "<zipcode>", startDate "<startDate>", endDate "<endDate>", page "<page>", size "<size>" and sort "<sort>"
    Then I expect in the response the status code is "<statusCode>"
    Examples:
      | zipcode | startDate  | endDate    | page | size | sort                       | statusCode |
      | 100x    | 2020-01-0x | 2020-01-04 | 0    | 5    | date,asc                   | 400        |
      | 1001    | 2020-01-0x | 2020-01-04 | 0    | 5    | date,asc                   | 400        |
      | 1001    | 2020-01-32 | 2020-01-04 | 0    | 5    | date,asc                   | 400        |
      | 1001    | 2020-13-32 | 2020-01-04 | 0    | 5    | date,asc                   | 400        |
      | 1001    | 2020-13-32 | 2020-32-32 | 0    | 5    | date,asc                   | 400        |
      | 1001    | 2020-12-31 | 2020-12-12 | 0    | 1    | date,asc                   | 400        |
      | 1001    | 2020-11-02 | 2020-12-12 | 1    | 1    | unknownColumn,asc          | 500        |
      | 1001    | 2020-11-02 | 2020-12-12 | 1    | 1    | date,unknowSortingStrategy | 500        |
