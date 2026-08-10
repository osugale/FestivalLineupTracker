# Festival Lineup Tracker

A Spring Boot REST API for managing music festivals, artists and festival lineups.

I built this project to practice building a backend application using Spring Boot, Spring Data JPA, MySQL and REST APIs.

The main idea is to keep track of festivals and the artists performing at them, along with the relationship between the two.

## What the project can do

### Artists

- Create an artist
- Get all artists
- Get an artist by ID
- Update an artist
- Delete an artist
- Search artists by name
- Get all festivals an artist is performing at

### Festivals

- Create a festival
- Get all festivals
- Get a festival by ID
- Update a festival
- Delete a festival
- Search festivals by name
- Get all artists performing at a festival

### Festival lineups

- Add an artist to a festival
- Remove an artist from a festival
- View the artists performing at a festival
- View the festivals an artist is associated with

The relationship between artists and festivals is a many-to-many relationship.

## Tech Stack

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Jakarta Bean Validation
- Swagger / OpenAPI

## Project Structure

The project follows a basic layered architecture:

```text

Controller
    ↓
Service
    ↓
Repository
    ↓
MySQL Database


The main packages so far are:
config, controller, dto, exception, model ,repository, service


Controller
Handles HTTP requests and responses.


Service
Contains the main application logic and handles conversion between entities and DTOs.


Repository
Handles communication with the database using Spring Data JPA.


Model
Contains the JPA entities used for database mapping.


DTO
Request and response DTOs are used so that the API does not directly expose the database entities.


Exception
Contains the global exception handling and custom resource-not-found exception.


DTO Flow
For requests, the JSON sent by the client is converted into a Request DTO by Jackson.


For Requests following is the usual flow:
Client
   ↓
JSON
   ↓
Jackson
   ↓
Request DTO
   ↓
Controller
   ↓
Service
   ↓
Entity
   ↓
Repository
   ↓
Database



Reponse Flow:
Database
   ↓
Repository
   ↓
Entity
   ↓
Service
   ↓
Response DTO
   ↓
Controller
   ↓
Jackson
   ↓
JSON
   ↓
Client



Database
The project uses MySQL.




API Endpoints :

Artists
Method	Endpoint	                  Description
GET	/artists	                Get all artists
GET	/artists/{id}	                Get artist by ID
POST	/artists	                Create an artist
PUT	/artists/{id}	                Update an artist
DELETE	/artists/{id}	                Delete an artist
GET	/artists/search?name={name}	Search artists by name
GET	/artists/{artistId}/festivals	Get festivals of an artist


Festivals
Method	Endpoint	                   Description
GET	/festivals	                Get all festivals
GET	/festivals/{id}	                Get festival by ID
POST	/festivals	                Create a festival
PUT	/festivals/{id}	                Update a festival
DELETE	/festivals/{id}	                Delete a festival
GET	/festivals/search?name={name}	Search festivals by name
GET	/festivals/{festivalId}/artists	Get artists of a festival

Festival / Artist relationship
Method	                     Endpoint	                        Description
POST	    /festivals/{festivalId}/artists/{artistId}	Add an artist to a festival
DELETE	    /festivals/{festivalId}/artists/{artistId}	Remove an artist from a festival
Pagination and Sorting

Artists support pagination and sorting.







Validation
Request DTOs use Jakarta Bean Validation.

For example:
@NotBlank(message = "Artist name cannot be blank")
private String name;

The controller uses @Valid to trigger validation of incoming request data.
Invalid requests return a 400 Bad Request response.

Global Exception Handling
The project uses @RestControllerAdvice for global exception handling.


Validation errors are handled using:
@ExceptionHandler(MethodArgumentNotValidException.class)

A custom ResourceNotFoundException is also used when an artist or festival cannot be found.
This keeps error handling separate from the controller logic.




Swagger

Swagger/OpenAPI is included for API documentation and testing.

Once the application is running, Swagger UI can be opened at:
http://localhost:8080/swagger-ui/index.html

Swagger can be used to:
View all available endpoints
See request and response structures
Test endpoints directly
View DTO schemas
See API validation information

Running the Project Requirements:
Java
Maven
MySQL


```

## Project Version: V1

### What can the project do right now?



The first version focuses on getting the core backend working.

It currently includes:


``` text
CRUD operations for artists
CRUD operations for festivals
Artist and festival search
Pagination
Sorting
DTO-based request and response handling
Many-to-many artist/festival relationships
Request validation
Global exception handling
Swagger/OpenAPI documentation
```
