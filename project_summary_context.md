# Project `serviceArchitectury` - Development Summary & Context

This document summarizes the development process of the Java web application for managing projects and programmers, capturing all key architectural decisions and feature implementations.

## 1. Initial Development & Core Components

*   **Project Setup:** The project was initialized as a Maven project with dependencies for Tomcat, JSP, and MySQL (`pom.xml`).
*   **Database Schema (`schema.sql`):**
    *   Created `projects` and `programmers` tables.
    *   Initially, these tables contained calculated columns (`cost`, `total_salary`).
*   **Core Models (`Project.java`, `Programmer.java`):** Standard Java beans to represent the main entities.
*   **Data Access Objects (`ProjectDao`, `ProgrammerDao`):** Classes responsible for all JDBC interactions.
*   **Service Layer (`CalculationService.java`):** A dedicated service was created to encapsulate business logic for calculating project costs and programmer salaries.
*   **User Interface:** The UI was built using Servlets to handle HTTP requests and JSP pages for rendering HTML.

## 2. Iterative Improvements & Bug Fixes

*   **Date Formatting Error:** An issue with `java.time.LocalDate` and an old JSTL tag library was resolved by removing `<fmt:formatDate>` and displaying the date objects directly.
*   **Optional End Dates:** The application was modified to allow `NULL` values for `end_date` in both the `programmers` and `projects` tables. This required changes to:
    *   UI: Removing the `required` attribute from date inputs.
    *   Database: Allowing `NULL` in the schema.
    *   DAO: Handling `null` values correctly when reading/writing dates.
    *   Logic: Salary and cost calculations were updated to use the current date if the end date was not specified.

## 3. Major Refactoring: Logic Migration to Database

This was a key architectural decision to improve performance and centralize business logic.

*   **`CalculationService.java` Removed:** The Java service layer for calculations was completely deleted.
*   **Database Logic (`schema.sql`):**
    *   Stored columns (`cost`, `total_salary`) were removed from the tables.
    *   A stored function `CountWorkDays` was created to calculate business days.
    *   Stored procedures `GetProgrammerSalary` and `GetProjectCost` were implemented to perform the calculations directly within the database.
*   **DAO & Model Updates:**
    *   DAOs were updated to call the new stored procedures.
    *   Models were adapted to hold the calculated values fetched from the database.

## 4. Feature Expansion

*   **Sorting:** All tables (projects, programmers) were enhanced with clickable column headers for dynamic sorting (`ORDER BY` clauses in DAO methods).
*   **Deletion:** UI forms and corresponding servlets (`DeleteProjectServlet`, `DeleteProgrammerServlet`) were added to allow for the deletion of projects and programmers.
*   **Character Encoding Fix:** To solve issues with displaying Russian characters ("кракозябры"), a comprehensive fix was implemented:
    *   The database connection URL was updated.
    *   The schema's default character set was changed to `utf8mb4`.
    *   An `EncodingFilter` was created to enforce `UTF-8` on all HTTP requests and responses.

## 5. Authentication & Authorization (Role-Based Access Control)

This was the final major feature set implemented.

*   **User Entity:** A `User` entity was created (model, DAO, and `users` table in `schema.sql`) with roles (`ADMIN`, `MANAGER`, `USER`).
*   **Authentication Flow:**
    *   A `login.jsp` page and `LoginServlet` were created for user authentication.
    *   A `LogoutServlet` was added to invalidate the user session.
*   **Authorization Refactoring (Two Phases):**
    1.  **Initial Filter:** An `AuthenticationFilter` was created to protect all pages. It also incorporated the encoding logic from the now-deleted `EncodingFilter`. It checked for a valid session and had hardcoded path rules for different roles.
    2.  **Annotation-Based Security (Current):** The system was refactored for a cleaner, more declarative security model.
        *   A custom annotation `@AuthRequired` was created.
        *   An `AuthBaseServlet` was implemented to process this annotation and check user roles before executing the servlet's logic.
        *   All servlets handling sensitive operations were made to extend `AuthBaseServlet` and were decorated with `@AuthRequired(roles = {"..."})`.
        *   `AuthenticationFilter` was simplified to only handle character encoding.
*   **UI Adaptation for Roles:** JSP pages were updated to use JSTL tags (`<c:if>`) to conditionally render UI elements (like "Add", "Edit", "Delete" buttons) based on the logged-in user's role.
*   **Full CRUD for Entities:**
    *   **Manager Role:** Implemented full CRUD (Create, Read, Update, Delete) for Projects and Programmers.
    *   **Admin Role:** Implemented full CRUD for Users.

## 6. DAO Transaction Management

*   To enhance data integrity, all data modification methods in the DAOs (`UserDao`, `ProjectDao`, `ProgrammerDao`) were refactored to use manual transaction management.
*   This involves setting `autoCommit(false)`, explicitly calling `commit()` on success, and `rollback()` on any `SQLException`.

## 7. Utility Scripts

*   A `remove_comments.bat` script was created to strip all Java comments from the project source files, using PowerShell for robust parsing.

This summary represents the complete state and history of the project up to this point. 