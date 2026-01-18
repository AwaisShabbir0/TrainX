# TrainX - Railway Management System 🚆

## Project Overview
**TrainX** is a modern, feature-rich **Railway Management System** built with **JavaFX** and **MySQL**. It is designed to automate railway operations, offering a seamless experience for both Administrators and Passengers. The system focuses on **efficiency, visual appeal, and data integrity**, featuring real-time analytics, secure wallet transactions, and robust validation mechanisms.

---

## 🚀 Key Features

### 👤 User Module
*   **Secure Authentication**: Role-based login (User/Admin) with strict validation.
*   **Ticket Booking**: Search trains, select seats, and book tickets instantly.
*   **E-Wallet System**: Built-in wallet to manage funds, add money via card (16-digit validation), and pay for tickets.
*   **Booking History**: View past journeys and transaction history.
*   **Profile Management**: Update personal details secure and easily.
*   **Ticket Cancellation**: Easy cancellation process with strict policies.

### 🛠 Admin Module
*   **Dashboard Analytics**: Real-time visualization of Total Revenue, Bookings, Active Trains, and Passenger Count.
*   **Train Management**: Add, Edit, and Remove trains with proper schedule validation (HH:mm format).
*   **Passenger Management**: View and manage registered passengers.
*   **Kitchen/Meal Management**: Oversee onboard meal services.

### ✨ Technical Enhancements
*   **Global Validations**:
    *   **Wallet**: Enforced 16-digit Card & 3-digit CVV checks.
    *   **Phone/CNIC**: Strict 11-digit Phone and 13-digit CNIC verification.
    *   **Inputs**: Numeric checks for prices and amounts.
*   **Modern UI/UX**:
    *   Unified **Light Green (#E1FFE1)** Branding.
    *   Responsive layouts with prominent, scalable vector icons.
    *   Professional "White Card" dashboard aesthetics.

---

## 🛠 Technologies Used
*   **Frontend**: JavaFX (FXML)
*   **Backend**: Core Java
*   **Database**: MySQL
*   **Design Pattern**: MVC (Model-View-Controller), DAO, Singleton
*   **Tools**: NetBeans / Eclipse / VS Code

---

## ⚙️ How to Run
1.  **Database Setup**:
    *   Import the provided `railway_setup.sql` (or `pakrailways.sql`) into your MySQL Server.
    *   Database Name: `pakrailways`
2.  **Configuration**:
    *   Open `src/airlinemanagementsystem/Conn.java`.
    *   Update the database username and password:
        ```java
        c = DriverManager.getConnection("jdbc:mysql:///pakrailways", "root", "YOUR_PASSWORD");
        ```
3.  **Build & Run**:
    *   Open the project in your IDE.
    *   Clean and Build the project to generate `build/classes`.
    *   Run `MainApp.java` as the entry point.

---

## 👥 Contributors
*   **M. Owais (50907)** - *Lead Developer*
*   **Team TrainX** - *Development & Testing*

---
*© 2026 TrainX Management System. All Rights Reserved.*
