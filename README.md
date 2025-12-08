## Transport Management System (TMS)

A backend application designed to manage transportation logistics efficiently. Built with Spring Boot 3.5.8 and Java 21, the system focuses on load posting, bidding, booking, and fleet capacity management with robust validation and concurrency control.


## Setup Instructions

### Prerequisites

1. Java 21
2. SpringBoot 3.5.8
3. PostgreSQL

### Database Setup

```bash
CREATE DATABASE tms_db;
```

### Configuration

Update application.properties:

```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/tms_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### Build & Run

```bash
mvn clean install
mvn spring-boot:run
```

Application runs at: http://localhost:8080

## Key Features

1. Load management with status and lifecycle tracking
2. Transporter registration with fleet capacity mapping
3. Bidding system with comparison and best bid scoring
4. Multi-truck booking and partial allocation handling
5. Prevention of concurrent bookings using optimistic locking
6. Full global exception handling and validation
7. Pagination and filtering across listing endpoints

## API Documentation

### Load APIs

1. Create Load
```bash
POST /load
Content-Type: application/json

{
  "shipperId": "SHIP-001",
  "loadingCity": "Mumbai",
  "unloadingCity": "Delhi",
  "loadingDate": "2025-12-15T09:00:00",
  "productType": "Electronics",
  "weight": 5000,
  "weightUnit": "KG",
  "truckType": "Container",
  "noOfTrucks": 2
}
```

2. Get Loads (with pagination)
```bash
GET /load?shipperId=SHIP-001&status=POSTED&page=0&size=10
```

3. Get Load with Active Bids
```bash
GET /load/{loadId}
```

4. Cancel Load
```bash
PATCH /load/{loadId}/cancel
```

5. Get Best Bids
```bash
GET /load/{loadId}/best-bids
```

### Transporter APIs

1. Create Transporter
```bash
POST /transporter
Content-Type: application/json

{
  "companyName": "FastTransport Ltd",
  "rating": 4.5,
  "availableTrucks": [
    {"truckType": "Container", "count": 10},
    {"truckType": "Flatbed", "count": 5}
  ]
}
```

2. Get Transporter
```bash
GET /transporter/{transporterId}
```

3. Update Trucks
```bash
PUT /transporter/{transporterId}/trucks
Content-Type: application/json

[
  {"truckType": "Container", "count": 8},
  {"truckType": "Flatbed", "count": 3}
]
```

### Bid APIs
1. Create Bid
```bash
POST /bid
Content-Type: application/json

{
  "loadId": "{loadId}",
  "transporterId": "{transporterId}",
  "proposedRate": 50000.00,
  "trucksOffered": 2
}
```

2. Get Bids (with filters)
```bash
GET /bid?loadId={loadId}&transporterId={transporterId}&status=PENDING
```

3. Get Bid
```bash
GET /bid/{bidId}
```

4. Reject Bid
```bash
PATCH /bid/{bidId}/reject
```

### Booking APIs

1. Create Booking
```bash
POST /booking
Content-Type: application/json

{
  "bidId": "{bidId}"
}
```

2. Get Booking
```bash
GET /booking/{bookingId}
```

3. Cancel Booking
```bash
PATCH /booking/{bookingId}/cancel
```
