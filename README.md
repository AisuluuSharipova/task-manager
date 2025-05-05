# Task Management System 📝

A **Spring Boot**-based Task Management System that allows **Admins** to manage users and tasks efficiently.  
**Users** can update the status of their assigned tasks, while **Admins** have full control over task and user management.

---

## ✨ Features
- ✅ Users can **view and update** the status of their assigned tasks.
- 🔧 Admins can **create, update, assign, and delete** tasks.
- 🛠 Admins can **create, update, delete, and search users** by email or last name.
- 🔐 Full **Authentication & Authorization** with:
  - Email/password login
  - GitHub OAuth login (`/oauth2/authorization/github`)
  - JWT (Access + Refresh tokens)
  - Email verification during registration
  - Refresh token storage in DB
- ⚠️ Proper **exception handling and validation**.
- 📄 Protected endpoints using Spring Security.
- 🗄 Uses **H2 Database (Testing)** and **PostgreSQL (Production)**.
- 📖 API documentation with **Swagger (SpringDoc OpenAPI)**.
- 🧪 **Unit & Integration tests** for reliability.

---

## 📂 Project Structure
This project follows a standard **Spring Boot architecture**:

- **Controllers** → Handle API requests and responses.
- **DTOs** → Used for request and response data mapping.
- **Entities** → Represent database models.
- **Repositories** → Provide database access using JPA.
- **Services** → Implement business logic.
- **Mappers** → Convert between DTOs and entities.
- **Security** → Configured JWT, OAuth2, filter chains.
- **Bootstrap (DataInit)** → Initializes sample data for testing.

---

## 📖 API Documentation
Swagger UI is available at:
🔗 http://localhost:8080/swagger-ui.html

---

## 🛠 Technologies Used
- Java 17+
- Spring Boot
- Spring Security
- Spring Data JPA
- OAuth2 Client (GitHub)
- JWT (jjwt)
- H2 Database (Testing)
- PostgreSQL (Production)
- Swagger (SpringDoc OpenAPI)
- JUnit & Mockito (Testing)

---

## 🧪 Testing Security
- Test login with valid/invalid credentials.
- Test email verification flow.
- Test GitHub login: visit http://localhost:8080/oauth2/authorization/github
- Test token refresh endpoint.
- Use Swagger to try protected endpoints with Bearer tokens.

---

## 🚀 Getting Started

### 🛠 Prerequisites
- Java **17+**
- Maven
- PostgreSQL (for production)

### 📥 Clone the Repository
```sh
git clone https://github.com/your-username/task-management.git
cd task-management
```

---

## 📩 Contact
If you have any issues or suggestions, feel free to open an issue or contact the developer.
