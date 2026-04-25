# 🎓 The Adaptive Scholar: Project Documentation

This document provides a comprehensive overview of the **Adaptive Scholar** platform, an AI-powered educational application designed to provide a seamless and intelligent learning experience.

---

## 🏗️ Technical Architecture

The project follows a modern **Decoupled Architecture**, separating the frontend user interface from the backend business logic and data management.

### 1. Frontend: Angular 17+
*   **Framework**: Angular (Standalone Components) for a modular and high-performance SPA (Single Page Application).
*   **State Management**: Optimized using **Signals** for real-time reactive updates (e.g., global search).
*   **Styling**: **Tailwind CSS** for a premium, responsive UI with custom animations and Glassmorphism effects.
*   **Routing**: Sophisticated routing with **Data Resolvers** to pre-fetch course data, eliminating "navigation buffering."

### 2. Backend: Spring Boot (Java)
*   **Core**: Spring Boot with Maven.
*   **Security**: **Spring Security + JWT (JSON Web Tokens)**. This ensures that only authenticated users can access curriculum data.
*   **Database**: **H2 In-Memory Database** (for development) with JPA/Hibernate for Object-Relational Mapping.
*   **API Design**: Clean RESTful endpoints for Courses, Lessons, and User Authentication.

### 3. AI Integration: Google Gemini
*   The platform uses the **Gemini API** to provide:
    *   Contextual lesson summaries.
    *   Real-time chat assistance.
    *   Automated HTML cheatsheet generation.

---

## 📂 File-Level Connection Map (The "Bridge")

If you are asked exactly **which files** connect the two systems, here is the map:

### 1. The Authentication Bridge (Login)
*   **Frontend**: `src/app/services/auth.service.ts`
    *   *Function*: Sends the login request and stores the JWT in `localStorage`.
*   **Backend**: `src/main/java/com/example/demo/controller/AuthController.java`
    *   *Function*: Verifies credentials and generates the JWT.

### 2. The Data Bridge (Curriculum)
*   **Frontend**: `src/app/services/course.service.ts`
    *   *Function*: Makes GET/POST requests to fetch courses, modules, and lessons.
*   **Backend**: `src/main/java/com/example/demo/controller/CourseController.java`
    *   *Function*: Provides the REST endpoints (`/api/courses`) that the frontend calls.

### 3. The Security & Permissions Bridge
*   **Backend**: `src/main/java/com/example/demo/security/SecurityConfig.java`
    *   *Function*: This is the most critical file. It contains the **CORS** (Cross-Origin Resource Sharing) settings that explicitly allow `http://localhost:4200` (the frontend) to talk to the backend.

---

## 🔗 How Frontend & Backend Connect

The connection is managed through a **RESTful Communication Layer**:

1.  **Authentication Flow**:
    *   User submits credentials via the Angular Login form.
    *   Spring Boot validates the user and signs a **JWT Token**.
    *   The frontend stores this token in `localStorage`.
2.  **Authorized Requests**:
    *   Every subsequent request (e.g., fetching courses) includes a `Bearer <token>` in the HTTP Header.
    *   The backend validates the token before serving the data.
3.  **Data Fetching**:
    *   Angular **Services** (`CourseService`) use the `HttpClient` to make asynchronous requests to `http://localhost:8080/api/...`.

---

## ✨ Key Features

| Feature | Description | Tech Used |
| :--- | :--- | :--- |
| **Adaptive Catalog** | Instant search and filtering of courses across the platform. | Angular Signals & Shared Services |
| **AI Study Partner** | A conversational AI sidebar that helps students understand complex topics. | Google Gemini SDK |
| **Smart Dashboard** | Tracks "Mastery Scores" and generates personalized insights. | RxJS & Custom Logic |
| **Cheatsheet Generator** | Instantly creates and downloads a study guide for any lesson. | AI Prompt Engineering & Blobs |
| **Dynamic Video Engine** | Embedded YouTube player with module-based navigation. | DomSanitizer & YouTube API |

---

## 🚀 The Development Flow (Visualizing the Request)

1.  **User Action**: User clicks "Go to Course" on the catalog.
2.  **Navigation**: The `CourseResolver` triggers, calling the backend `/api/courses/{id}`.
3.  **Backend Logic**: The `CourseRepository` fetches data from the H2 database.
4.  **Response**: Data returns as JSON.
5.  **Rendering**: Angular initializes the `CourseDetailsComponent` and displays the video and AI chat instantly.

---

## 🎙️ Presentation: Possible Q&A

**Q: Why use a "Resolver" for the courses page?**
*   **A**: Without a resolver, the page would load a blank "Loading" state first. The resolver pre-fetches the data *before* the component renders, making the app feel significantly faster and more "native."

**Q: How do you secure the student data?**
*   **A**: We use JWT (JSON Web Tokens). If a student tries to access an API without a valid token, the Spring Security layer blocks them with a `403 Forbidden` status before the request even reaches the business logic.

**Q: How does the AI know about the current lesson?**
*   **A**: When a student opens the chat, the frontend injects the current lesson title and description into the AI prompt, giving the Gemini model the "context" it needs to provide accurate answers.

**Q: What happens if the backend server goes down?**
*   **A**: The Angular frontend has error interceptors that catch `500` errors and provide a user-friendly message, ensuring the app doesn't just "crash" visually.

---
