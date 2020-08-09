@LocationRestAPIFeature
Feature: Location rest API endpoint tests


  Background: The weather application is started and data is populated in the Location table
    Given the application has started successfully
    When the following data is added to the Location table
      | zipcode |
      | 1001    |
      | 1005    |
      | 1003    |
      | 1007    |
      | 1002    |
    Then the total number of records in Location table is 5

  Scenario Outline: Requesting location data using fetchByZipcode endpoint
    When I request location by zipcode "<zipcode>"
    Then I expect the response as "<response>"
    Examples:
      | zipcode | response                |
      | 1001    | {"id":1,"zipcode":1001} |
      | 1002    | {"id":5,"zipcode":1002} |
      | 1003    | {"id":3,"zipcode":1003} |
      | 1007    | {"id":4,"zipcode":1007} |
      | 1005    | {"id":2,"zipcode":1005} |
      | 1006    | null                    |

  Scenario Outline: Requesting location data using fetchAll endpoint
    When I provide page number "<page>", size "<size>" and sort "<sort>"
    Then I expect in the response the content as "<content>"
    Examples:
      | page | size | sort         | content                                                                                                                   |
      | 0    | 5    | zipcode,desc | [{"id":4,"zipcode":1007},{"id":2,"zipcode":1005},{"id":3,"zipcode":1003},{"id":5,"zipcode":1002},{"id":1,"zipcode":1001}] |
      | 0    | 5    | id,desc      | [{"id":5,"zipcode":1002},{"id":4,"zipcode":1007},{"id":3,"zipcode":1003},{"id":2,"zipcode":1005},{"id":1,"zipcode":1001}] |
      | 1    | 5    | id,desc      | []                                                                                                                        |
      | 0    | 4    | id,desc      | [{"id":5,"zipcode":1002},{"id":4,"zipcode":1007},{"id":3,"zipcode":1003},{"id":2,"zipcode":1005}]                         |
      | 1    | 4    | id,desc      | [{"id":1,"zipcode":1001}]                                                                                                 |
      | 2    | 4    | id,desc      | []                                                                                                                        |
      | 0    | 1    | zipcode,desc | [{"id":4,"zipcode":1007}]                                                                                                 |
      | 1    | 1    | zipcode,desc | [{"id":2,"zipcode":1005}]                                                                                                 |
      | 0    | 1    | zipcode,asc  | [{"id":1,"zipcode":1001}]                                                                                                 |
      | 1    | 1    | zipcode,asc  | [{"id":5,"zipcode":1002}]                                                                                                 |
      | 0    | 2    | id,desc      | [{"id":5,"zipcode":1002},{"id":4,"zipcode":1007}]                                                                         |
      | 2    | 10   | id,desc      | []                                                                                                                        |


  Scenario Outline: Requesting location data using fetchByZipcode endpoint with invalid input
    When I request location by zipcode "<zipcode>"
    Then I expect in the response the status code as "<statusCode>"
    Examples:
    Examples:
      | zipcode                                   | statusCode |
      | 123x                                      | 400        |
      | 10060000000000000000000000000000000000000 | 400        |
      | xyz                                       | 400        |
      | 10.0                                      | 400        |


