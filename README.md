# Task Management System 📝

A **Spring Boot**-based Task Management System that allows **Admins** to manage users and tasks efficiently.  
**Users** can update the status of their assigned tasks, while **Admins** have full control over task and user management.

## ✨ Features
- ✅ Users can **view and update** the status of their assigned tasks.
- 🔧 Admins can **create, update, assign, and delete** tasks.
- 🛠 Admins can **create, update, delete, and search users** by email or last name.
- ⚠️ Proper **exception handling and validation**.
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
- **Bootstrap (DataInit)** → Initializes sample data for testing.

---

## 📖 API Documentation
Swagger UI is available at:
🔗 http://localhost:8080/swagger-ui.html

---

## 🛠 Technologies Used
- Java 17+
- Spring Boot
- Spring Data JPA
- Spring Validation
- Spring Web
- Hibernate
- H2 Database (Testing)
- PostgreSQL (Production)
- Swagger (SpringDoc OpenAPI)
- JUnit & Mockito (Testing)

---

## 🔥 Future Enhancements
- 🔐 Implement authentication and role-based access control.
- 📩 Add user notifications for task updates.
- ⏰ Introduce task deadlines and automated reminders.
- 🎨 Develop a frontend for better user experience.

---

## 🚀 Getting Started

### 🛠 Prerequisites
- Java **17+**
- Maven
- PostgreSQL (for production)

## 📥 Clone the Repository
```sh
git clone https://github.com/your-username/task-management.git
cd task-management

