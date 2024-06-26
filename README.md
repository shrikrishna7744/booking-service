# booking-service

App uses in-memory h2 database for persistence

## Steps to Setup

**1. Clone the application**

```bash
https://github.com/shrikrishna7744/booking-service.git
```

Build the app

```bash
mvn clean install
```

Run the app

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:8080>.

Swagger Link: <http://localhost:8080/swagger-ui/index.html>.

Postman
Collection: <https://api.postman.com/collections/10086641-26f675d7-0063-416a-96aa-d802fe9cbbe1?access_key=PMAT-01HTT6G65EVCFN43PCHPEBP4E7>.

## Explore Rest APIs

The app defines following APIs.

    POST http://localhost:8080/booking
    
    GET http://localhost:8080/booking
    
    DELETE http://localhost:8080/booking/{bookingId}
    
    PUT http://localhost:8080/booking/{bookingId}
