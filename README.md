🔐 Secure Feedback Application
A full-stack secure feedback management system built using Spring Boot, Spring Security, MySQL, and Docker.
This application allows users to submit feedback securely and enables administrators to manage and review submissions.

🚀 Features

👤 User Features
User registration and login
Secure authentication using Spring Security
Submit feedback with validation
Password reset using security questions

🛠️ Admin Features
Admin login with role-based access
View all submitted feedback
Manage users and feedback data

🔐 Security Features
Password encryption using BCrypt
Login attempt tracking & account locking
Input validation to prevent invalid data
Role-based authorization (USER / ADMIN)


## 🧱 Tech Stack

| Layer      | Technology                  |
| ---------- | --------------------------- |
| Backend    | Spring Boot 4               |
| Security   | Spring Security             |
| Database   | MySQL 8                     |
| ORM        | Spring Data JPA (Hibernate) |
| Frontend   | HTML, CSS                   |
| Build Tool | Maven                       |
| Container  | Docker & Docker Compose     |

## 📂 Project Structure

```
securefeedbackAPP/
│── src/
│   ├── main/
│   │   ├── java/com/securefeedback/
│   │   │   ├── Model/
│   │   │   ├── Repository/
│   │   │   ├── Service/
│   │   │   ├── Controller/
│   │   │   └── Security/
│   │   └── resources/
│   │       ├── templates/
│   │       ├── static/
│   │       ├── application.properties
│   │       ├── schema.sql
│   │       └── data.sql
│
│── Dockerfile
│── docker-compose.yml
│── pom.xml
```

## ⚙️ How to Run the Project

### 🐳 Run with Docker (Recommended)

1. Clone the repository:

2. Build and run containers:

```bash
docker compose up --build
```

3. Open in browser:

```
http://localhost:8080
```


### 💻 Run Without Docker

1. Make sure you have:

   * Java 21
   * Maven
   * MySQL running

2. Update `application.properties` with your DB credentials

3. Run the project:

```bash
./mvnw spring-boot:run
```

---

## 🔑 Default Credentials

| Role  | Username | Password |
| ----- | -------- | -------- |
| Admin | admin    | admin123 |
| User  | testuser | test123  |


## 🗄️ Database

* Automatically created using `schema.sql`
* Initial data inserted via `data.sql`
* Uses **MySQL container** in Docker setup


## 👨‍💻 Author

**Your Name**
📧 aremon.099@unb.ca / aremon099@gmail.com
🔗 https://github.com/aremon

---

## 📜 License

This project is for educational purposes.
