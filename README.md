# 🛒 E-Commerce Project - Web Platforms

This repository contains the source code for a full-stack e-commerce application, developed as a final project for the **Web Platforms** university course.

The system is divided into a frontend (another repository) (user interface) and a RESTful backend that handles the business logic, authentication, product catalog management, and a highly secure, transactional purchasing process.

## 🚀 Architecture and Technologies

### Backend
The backend was developed in **Java** using the **Spring Boot** framework, following a layered architecture (Controller, Service, Repository).

Core technologies include:
* **Spring Boot (Web, Data JPA, Security):** Used to build the RESTful APIs, handle data access, and secure the endpoints.
* **Keycloak (OAuth2 & JWT):** Identity and access management server. It handles user registration, roles (`client`, `admin`), and issues the JWTs used to secure the API.
* **Hibernate / JPA:** ORM used for mapping entities (Users, Products, Carts, Purchase Details).
* **Relational Database:** (e.g., PostgreSQL/MySQL) configured for data persistence.

## ⚙️ Core Features

Based on the exposed REST services, the system covers the entire lifecycle of an online purchase:

### 1. Authentication and Account Management (`AccountController` & Keycloak)
* Registration of new users with automatic synchronization to the **Keycloak** server.
* Automatic creation of an empty shopping cart associated with the user upon registration.
* Protection of sensitive routes (e.g., cart, purchases) using JWT tokens (`JwtAuthConverter`).

### 2. Product Catalog (`ProdottoController`)
* Catalog browsing with **pagination and sorting** (`pageNumber`, `pageSize`, `sortBy`).
* Advanced search:
  * By name.
  * By barcode.
  * By category (with dynamic filtering).
* Search by specific product ID.

### 3. Shopping Cart Management (`CarrelloController`)
* Adding products to the cart (with real-time stock availability checks).
* Updating quantities and removing products from the cart.
* Unique association of the cart to the logged-in user, extracted securely from the security token (`Utils.getEmail()`).

### 4. Checkout and Order History (`CarrelloController` & `ConsultaAcquisti`)
* **Secure Checkout:** The system converts the cart into an actual purchase (`AcquistoService`), creating the order and emptying the cart.
* **Transactional Checks:** During checkout, the backend strictly verifies two critical scenarios:
  1. *Price changes:* Prevents the user from buying a product at a different price than what was added to the cart.
  2. *Stock depletion:* Ensures that the warehouse quantities are still sufficient at the exact moment of the purchase click.
* Order history consultation for the customer, implemented with pagination.

---

## 🧠 Technical Highlights (Backend)

* **Concurrency Management:** Implementation of optimistic and pessimistic locking (`@Version` on entities, handling `PessimisticLockException`) to prevent *race conditions* (e.g., two users trying to buy the last available item at the exact same time).
* **Custom Exception Handling:** Extensive use of custom exceptions (e.g., `QuantitaInsufficienteException`, `PrezzoCambiatoException`, `MailUsataEsisteException`) to return clear, manageable HTTP error messages to the frontend.
* **CORS Configuration:** Dedicated configuration (`SecurityConfiguration`) to safely allow the frontend to communicate with the API.
* **Stateless Auth:** The backend server is entirely stateless; it does not use sessions but relies on JWT tokens validated with every request.

---

## 🛠️ How to Run Locally

### Prerequisites
* Java 17+ (or the version you used)
* Maven or Gradle
* A running Keycloak instance (on `http://localhost:8080` with an `ecommerce` realm configured)
* A relational database configured in `application.properties`
