# Event Ticketing Backend

This is the backend service for the Event Ticketing application. It provides APIs for managing events, tickets, and users.

---

## Table of Contents

- [Installation](#installation)
- [API Routes](#api-routes)
  - [VendorController](#vendorcontroller-apiv1vendors)
  - [CustomerController](#customercontroller-apiv1customers)
  - [EventController](#eventcontroller-apiv1events)
  - [TicketPoolController](#ticketpoolcontroller-apiv1ticketpools)
  - [TicketController](#ticketcontroller-apiv1tickets)
  - [TransactionController](#transactioncontroller-apiv1transactions)
  - [ConfigurationController](#configurationcontroller-apiv1configuration)
- [Folder Structure](#folder-structure)
- [Contributing](#contributing)
- [Troubleshooting](#troubleshooting)
- [License](#license)
- [Contact](#contact)
- [Acknowledgements](#acknowledgements)

---

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/ammar-jesliy/event-ticketing-backend.git
   ```
2. Navigate to the project directory:
   ```bash
   cd event-ticketing-backend
   ```
3. Build the Docker containers:
   ```bash
   docker-compose up --build
   ```
4. Start the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```

The application will be running on `http://localhost:8080`.

---

## API Routes

### VendorController (`api/v1/vendors`)

#### Fetch all vendors

```http
GET /api/v1/vendors
```

#### Fetch vendor by ID

```http
GET /api/v1/vendors/{vendorId}
```

| Parameter | Type   | Description                |
| :-------- | :----- | :------------------------- |
| vendorId  | string | **Required**. ID of vendor |

#### Fetch vendors for simulation

```http
GET /api/v1/vendors/simulation
```

#### Check email availability

```http
GET /api/v1/vendors/check-email
```

#### Register a new vendor

```http
POST /api/v1/vendors/register
```

#### Vendor login

```http
POST /api/v1/vendors/login
```

#### Release tickets for an event

```http
POST /api/v1/vendors/release-tickets
```

#### Delete vendor by ID

```http
DELETE /api/v1/vendors/{vendorId}
```

| Parameter | Type   | Description                |
| :-------- | :----- | :------------------------- |
| vendorId  | string | **Required**. ID of vendor |

### CustomerController (`api/v1/customers`)

#### Fetch all customers

```http
GET /api/v1/customers
```

#### Fetch customer by ID

```http
GET /api/v1/customers/{customerId}
```

| Parameter  | Type   | Description                  |
| :--------- | :----- | :--------------------------- |
| customerId | string | **Required**. ID of customer |

#### Fetch customers for simulation

```http
GET /api/v1/customers/simulation
```

#### Check email availability

```http
GET /api/v1/customers/check-email
```

#### Register a new customer

```http
POST /api/v1/customers/register
```

#### Customer login

```http
POST /api/v1/customers/login
```

#### Buy tickets

```http
POST /api/v1/customers/buy-tickets
```

#### Delete customer by ID

```http
DELETE /api/v1/customers/{customerId}
```

| Parameter  | Type   | Description                  |
| :--------- | :----- | :--------------------------- |
| customerId | string | **Required**. ID of customer |

### EventController (`api/v1/events`)

#### Fetch all events

```http
GET /api/v1/events
```

#### Add a new event

```http
POST /api/v1/events
```

### TicketPoolController (`api/v1/ticketpools`)

#### Fetch all ticket pools

```http
GET /api/v1/ticketpools
```

#### Fetch ticket pool by event ID

```http
GET /api/v1/ticketpools/{eventId}
```

| Parameter | Type   | Description               |
| :-------- | :----- | :------------------------ |
| eventId   | string | **Required**. ID of event |

### TicketController (`api/v1/tickets`)

#### Fetch all tickets

```http
GET /api/v1/tickets
```

#### Fetch tickets by vendor ID

```http
GET /api/v1/tickets/vendor/{vendorId}
```

| Parameter | Type   | Description                |
| :-------- | :----- | :------------------------- |
| vendorId  | string | **Required**. ID of vendor |

### TransactionController (`api/v1/transactions`)

#### Fetch all transactions

```http
GET /api/v1/transactions
```

#### Fetch transactions by vendor ID

```http
GET /api/v1/transactions/vendor/{vendorId}
```

| Parameter | Type   | Description                |
| :-------- | :----- | :------------------------- |
| vendorId  | string | **Required**. ID of vendor |

#### Fetch transactions by customer ID

```http
GET /api/v1/transactions/customer/{customerId}
```

| Parameter  | Type   | Description                  |
| :--------- | :----- | :--------------------------- |
| customerId | string | **Required**. ID of customer |

### ConfigurationController (`api/v1/configuration`)

#### Retrieve configuration JSON

```http
GET /api/v1/configuration
```

#### Update configuration JSON

```http
POST /api/v1/configuration
```

---

## **Folder Structure**

The following is the folder structure of the Angular frontend project:

```plaintext

event-ticketing-backend/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── lk/ac/iit/eventticketingbackend/
│   │   │       ├── controller/ # Holds the REST controllers
│   │   │       ├── dto/        # Contains Data transfer objects
│   │   │       ├── model/      # Stores the models of the main entities
│   │   │       ├── repository/ # Stores files for CRUD operations.
│   │   │       ├── service/    # Contains business logic and methods
│   │   │       ├── Cli.java    # The command-line component
│   │   │       └── EventTicketingBackendApplication.java # Entry point
│   │   └── resources/
│   │       ├── application.properties # Application settings and configuration settings
│   │       └── logback.xml     # Logback configuration file for logging setup
│   └── test/
│       └── java/
│           └── lk/ac/iit/eventticketingbackend/
│
├── .gitattributes
├── .gitignore
├── docker-compose.yaml         # Docker container setup
├── mvnw
├── mvnw.cmd
└── pom.xml                     # Maven configuration file


```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a pull request

---

## Troubleshooting

### Common Issues

1. **Server not starting**

   - Ensure all environment variables are set correctly in the `application.properties` file.
   - Check if the port is already in use.
   - Review the logs for any specific error messages.

2. **Database connection errors**

   - Verify the database connection settings in the `application.properties` file.
   - Ensure the database server is running.
   - Check if the database credentials are correct.

3. **Docker Issues**

   - Run `docker-compose logs` to view errors
   - Ensure Docker is installed and the port `27017` is not in use.

4. **API endpoint not reachable**

   - Ensure the server is running and accessible.
   - Verify the API endpoint URL is correct.
   - Check for any network issues or firewall restrictions.

5. **Application crashes on startup**

   - Review the stack trace in the logs to identify the root cause.
   - Ensure all required dependencies are included in the `pom.xml` file.
   - Check for any missing or incorrect configurations.

6. **Unexpected errors**
   - Review the logs for any error messages or stack traces.
   - Check for any recent changes that might have introduced the issue.
   - Test the application in a different environment to isolate the problem.

---

## License

This project is licensed under the MIT License.

## Contact

Ammar Jesliy - [ammarjc1@gmail.com](mailto:ammarjc1@email.com)

Project Link: [https://github.com/ammar-jesliy/event-ticketing-backend](https://github.com/ammar-jesliy/event-ticketing-backend)

## Acknowledgements

- [Sprint Boot](https://spring.io/projects/spring-boot)
- [MongoDB](https://www.mongodb.com/)
- [Docker](https://www.docker.com/)
- [Jackson](https://github.com/FasterXML/jackson)
- [SLF4j](https://www.slf4j.org/)
- [MIT License](https://opensource.org/licenses/MIT)
