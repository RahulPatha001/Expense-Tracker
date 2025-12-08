# Expense Tracker Backend

Welcome to the backend of our **Expense Tracker Application** – a modular, microservices-based system built with scalability and clarity in mind. This project helps users track their expenses through both manual entry and automated SMS parsing powered by a Data Science layer.

---

## System Overview

The backend is designed as a set of microservices communicating over REST and Kafka, with token-based authentication and structured data pipelines.

### Key Microservices

1. **Auth Service** 
2. **User Service**   
3. **Data Science Service (dsService)**  
4. **Expense Service** 
5. **API Gateway** 

Each service is isolated with its own database and interacts via REST APIs and Kafka topics.

---

## 1. Auth Service

Responsible for user authentication, token issuance, and user identity resolution.

- **Functions:**
  - Signup/Login (returns access & refresh tokens)
  - Verify token for protected routes
  - Provides `getUserId` for internal service calls
- **DB:** MySql – `users`, `roles`, `tokens`, `users_roles`
- **Integration:** Produces user details to Kafka for User Service

---

## 2. User Service

Manages and stores user-related data after authentication.

- **Functions:**
  - Consumes `userDetails` from Kafka
  - Stores user profiles in its own DB
- **DB:** MySql – `users`
- **Note:** Keeps auth and profile data logically separated

---

## 3. Data Science Service (dsService)

Processes incoming SMS messages using a Large Language Model to extract structured expense data.

- **Functions:**
  - Input: Raw SMS message
  - Output: JSON with `amount`, `merchant`, `currency`
  - Publishes processed data to Kafka
- **Tech Stack:** Python, LLM (Mistral AI)

---

## 4. Expense Service

Handles expense data ingestion, both from Kafka and via direct user APIs.

- **Functions:**
  - Consumes structured data from Kafka (produced by dsService)
  - Exposes APIs to create, update, and fetch expenses
  - Associates each expense with a user
- **DB:** MySql – `expenses`

---

## 5. Kong API Gateway

Acts as the single entry point for client requests.

- **Responsibilities:**
  - Routes all requests to appropriate services
  - Only `/login` and `/signup` are open
  - For other requests, Kong:
    - Extracts and verifies token via `auth-service/getUserId`
    - Injects `X-User-Id` header before forwarding to downstream services

> This offloads authentication from your app code and makes security centralized and consistent.

---

## Service Interaction Flow

1. Client sends request to Kong
2. For `/login` or `/signup`, Kong forwards to Auth Service directly
3. For all other endpoints:
   - Kong calls `auth/v1/ping` using the access token
   - Injects `X-User-Id` header in the request
   - Forwards request to appropriate service (e.g., `expense-service`)
4. SMS ingestion (via user or webhook) is sent to `dsService`
5. `dsService` processes and publishes to Kafka
6. `expense-service` consumes and stores expense
7. User details are also produced by `auth-service` and consumed by `user-service`

---



