Title: Conference Room Booking Service
Description: This application is used for booking conference rooms and checking availability of conference rooms.

Requirements-
Java 17
Maven 3.8.8 or above

How to run this application
- Clone this repository in your machine
Option 1
- Open the project in Intellij
- Under project structure set sdk as 17
- Right click on BookingApplication.java file and select 'Run room-booking-service'
Option 2
- Open command prompt in root project directory
- Run 'mvn spring-boot:run'


How to access database tables
- Start the application
- Open this url in your browser- 'http://localhost:8080/h2-console'
- Set Driver Class as 'org.h2.Driver'
- Set JDBC URL as 'jdbc:h2:mem:booking-app'
- Set Username as 'sa'
- Keep the password blank
- Click on connect

API details
- Room availability check API- http://localhost:8080/v1/booking/check-availability
  Sample request body-
  {
    "startTime": "14:00",
    "endTime": "14:15"
  }
- Room booking API
  Sample request body-
  {
    "noOfPeople": 20,
    "userName": "test",
    "startTime": "16:15",
    "endTime": "16:15"
  }


Other info
- For the master list of maintenance timings and room details please refer to data.sql file in resources folder
- 'openapi.json' file is included in project root directory
- OpenAPI docs URL- 'http://localhost:8080/v3/api-docs'
- SwaggerUI URL- 'http://localhost:8080/swagger-ui/index.html'

Note: This application uses an in-memory database, so any restart will delete all data and the schema will be recreated.