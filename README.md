# 💰 Expense Tracker Backend

Welcome to the backend of our **Expense Tracker Application** – a modular, microservices-based system built with scalability and clarity in mind. This project helps users track their expenses through both manual entry and automated SMS parsing powered by a Data Science layer.

---

## 🧱 System Overview

The backend is designed as a set of microservices communicating over REST and Kafka, with token-based authentication and structured data pipelines.

### 💡 Key Microservices

1. **Auth Service** 🔐  
2. **User Service** 👤  
3. **Data Science Service (dsService)** 🧠  
4. **Expense Service** 💸  
5. **API Gateway** 📡

Each service is isolated with its own database and interacts via REST APIs and Kafka topics.

---

## 🔐 1. Auth Service

Responsible for user authentication, token issuance, and user identity resolution.

- **Functions:**
  - Signup/Login (returns access & refresh tokens)
  - Verify token for protected routes
  - Provides `getUserId` for internal service calls
- **DB:** MySql – `users`, `roles`, `tokens`, `users_roles`
- **Integration:** Produces user details to Kafka for User Service

---

## 👤 2. User Service

Manages and stores user-related data after authentication.

- **Functions:**
  - Consumes `userDetails` from Kafka
  - Stores user profiles in its own DB
- **DB:** MySql – `users`
- **Note:** Keeps auth and profile data logically separated

---

## 🧠 3. Data Science Service (dsService)

Processes incoming SMS messages using a Large Language Model to extract structured expense data.

- **Functions:**
  - Input: Raw SMS message
  - Output: JSON with `amount`, `merchant`, `currency`
  - Publishes processed data to Kafka
- **Tech Stack:** Python, LLM (Mistral AI)

---

## 💸 4. Expense Service

Handles expense data ingestion, both from Kafka and via direct user APIs.

- **Functions:**
  - Consumes structured data from Kafka (produced by dsService)
  - Exposes APIs to create, update, and fetch expenses
  - Associates each expense with a user
- **DB:** MySql – `expenses`

---

## 📡 5. API Gateway (KONG)

Acts as a unified entry point for all client requests.

- **Functions:**
  - Forwards only `/signup` and `/login` directly to Auth Service
  - Verifies access tokens for all other endpoints via Auth Service’s `/ping` 
  - Routes authorized traffic to appropriate microservices

---

## 🔄 Service Interaction Flow

- Client interacts with the API Gateway
- API Gateway routes `/login` and `/signup` to Auth Service
- Auth Service issues tokens
- All other requests are passed through the gateway with token verification
- SMS messages go to dsService → parsed → pushed to Kafka
- Expense Service consumes from Kafka and stores expense
- Auth Service also pushes user details to Kafka → consumed by User Service

---



