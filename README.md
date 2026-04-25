# 🎓 The Adaptive Scholar — AI-Powered Education Platform

The **Adaptive Scholar** is a premium, full-stack learning management system (LMS) designed to provide a personalized and intelligent educational experience. It integrates advanced AI capabilities, structured curriculum modules, and interactive assessments to help students master complex subjects.

---

## 🚀 Key Features

- **🤖 Intelligent AI Assistant**: Powered by Llama 3 (via Groq), providing real-time explanations, analogies, and chat history.
- **📚 Structured Curriculum**: 11+ professional courses (MERN, AI, Cybersecurity, Ethical Hacking, etc.) organized into distinct modules.
- **🛠️ Hands-on Learning**: Each lesson includes high-quality video content, descriptions, and resource downloads.
- **📝 Automated Assessments**: Integrated quiz system for every course with immediate feedback and performance tracking.
- **📊 Progress Tracking**: Visual dashboards showing course completion, active lessons, and recent achievements.
- **💼 Admin Suite**: Dedicated administrative tools to manage courses, lessons, and user data.
- **🛡️ Secure Foundation**: Built with Spring Security, JWT authentication, and MySQL persistence.

---

## 🛠️ Technology Stack

### Frontend
- **Framework**: Angular 17+ (Standalone Components)
- **Styling**: Tailwind CSS
- **State Management**: RxJS & Services
- **AI Integration**: Groq API (Llama 3.3 70B)

### Backend
- **Framework**: Spring Boot 3
- **Language**: Java 17
- **Security**: Spring Security & JWT
- **Database**: MySQL 8
- **ORM**: Hibernate / Spring Data JPA

---

## 📦 Project Structure

```text
AI_learning/
├── adaptive-scholar/          # Angular Frontend
│   ├── src/app/pages/         # UI Components (Dashboard, Courses, etc.)
│   └── src/app/services/      # API & AI logic
└── adaptive-scholar-backend/  # Spring Boot Backend
    ├── src/main/java/         # Controllers, Models, Security
    └── src/main/resources/    # Configuration (application.properties)
```

---

## ⚙️ Setup & Installation

### Prerequisites
- Node.js (v18+)
- Java JDK 17
- MySQL Server

### 1. Backend Setup
1. Open `adaptive-scholar-backend/src/main/resources/application.properties`.
2. Update `spring.datasource.password` with your MySQL password.
3. Run the backend:
   ```bash
   ./mvnw.cmd spring-boot:run
   ```
   *The database will automatically seed with 11 courses and an admin user on the first run.*

### 2. Frontend Setup
1. Navigate to the `adaptive-scholar` directory.
2. Install dependencies:
   ```bash
   npm install
   ```
3. Run the development server:
   ```bash
   npm run dev
   ```

---

## 🔑 Default Credentials

| Role | Email | Password |
|---|---|---|
| **Admin** | `admin@scholar.com` | `admin123` |
| **Student** | *Create your own account via Register* | |

---

## 📄 License
This project is for educational purposes. Built with ❤️ for the future of AI-driven education.
