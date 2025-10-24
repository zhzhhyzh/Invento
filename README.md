# Invento: Inventory Management System

**Invento** is a robust Inventory Management System built using **Java** and **Spring Boot**. It is designed to track stock levels, manage product sourcing, and maintain a detailed audit trail of all inventory movements.

---

## 1. System Overview

Invento models its core functionality around five primary entities:

| Entity | Purpose | Key Relationships |
| :--- | :--- | :--- |
| **Product** | The central item being tracked, containing stock level, SKU, and price details. | Linked to **Category**, **Supplier**, and **Transaction**. |
| **Category** | Organizes products into logical groups (e.g., Electronics, Apparel) for easier management. | One-to-Many with **Product**. |
| **Supplier** | Tracks the entities that provide the products, essential for reordering and procurement. | One-to-Many with **Product**. |
| **Transaction** | Records every inventory change (inbound/outbound), serving as the system's audit trail. | Linked to **Product** and the recording **User**. |
| **User** | Manages system access, roles, and permissions, ensuring accountability for transactions. | One-to-Many with **Transaction**. |

### Key Features

* **Real-time Stock Tracking:** Stock levels are dynamically updated based on **Transaction** entries.
* **Supplier Management:** Efficiently link products to their respective suppliers.
* **Audit Trail:** Every change is logged via the **Transaction** entity, detailing *who* (User) and *when* the change occurred.

---

## 2. Project Setup: IntelliJ IDEA

This guide provides the steps required to set up and run the Invento project locally using **IntelliJ IDEA**.

### 2.1. Prerequisites

* **Java Development Kit (JDK):** Version **17** or newer is required.
* **IntelliJ IDEA:** The Ultimate edition is recommended for full Spring Boot tooling, but the Community edition works fine with the necessary plugins.
* **Database:** (Specify your database here, e.g., MySQL, PostgreSQL, or H2 for development)

### 2.2. Opening and Importing the Project

1.  **Clone the Repository** (If applicable):
    ```bash
    git clone https://github.com/zhzhhyzh/Invento.git
    ```
2.  **Open IntelliJ IDEA.**
3.  On the welcome screen, click **Open** or go to **File $\rightarrow$ Open...**
4.  Navigate to the root directory of the cloned project (where `pom.xml` or `build.gradle` is located) and click **OK**.

### 2.3. Dependency Loading

IntelliJ should automatically detect your build system (Maven or Gradle).

* If a notification appears, click **"Load Maven Changes"** or **"Load Gradle Changes."**
* If no notification appears, manually click the **Reload** button (circular arrow icon) in the **Maven** or **Gradle** tool window.

### 2.4. Configuring the JDK

Ensure the correct Java Development Kit is linked to the project:

1.  Go to **File $\rightarrow$ Project Structure** (`Ctrl+Alt+Shift+S`).
2.  Under **Project Settings $\rightarrow$ Project**:
    * Set the **SDK** to your installed JDK (e.g., JDK 17).
    * Set the **Project language level** to match your target Java version.
3.  Click **OK**.

### 2.5. Running the Application

1.  Locate the main application class: `InventoApplication.java` (the class annotated with `@SpringBootApplication`).
2.  **Right-click** the file in the Project window.
3.  Select **"Run 'InventoApplication.main()'"** from the context menu.

The application will start, and you will see the Spring Boot logging output in the Run tool window, indicating that the application is live, typically on `http://localhost:5050`.

---

## 3. Configuration

### Database Settings

The database connection details are configured in the `src/main/resources/application.properties` (or `application.yml`) file. Modify these settings to connect to your local database instance:

```properties
# Example for H2 (In-memory development database)
spring.datasource.url=jdbc:h2:mem:inventodb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# Example for PostgreSQL
# spring.datasource.url=jdbc:postgresql://localhost:5432/inventodb
# spring.datasource.username=youruser
# spring.datasource.password=yourpassword
