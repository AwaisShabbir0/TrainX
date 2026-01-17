# How to Run the Airline Management System in Eclipse

## Prerequisites

1.  **Eclipse IDE**: Installed and ready.
2.  **Java JDK**: JDK 8 or a newer version installed.
3.  **MySQL Server**: Running on port `3306`.
4.  **Required Libraries**: You need to download the following JAR files, as they are not included in the repository:
    *   **mysql-connector-java**: For connecting to the MySQL database.
    *   **rs2xml.jar**: For displaying data in tables (`DbUtils`).
    *   **jcalendar.jar**: For the date picker component.
    *   **itextpdf.jar**: For generating PDF boarding passes.

## Step 1: Database Setup

1.  Open your MySQL command line client or a GUI tool like MySQL Workbench.
2.  Run the provided `database_setup.sql` script located in the root of this project.
    *   This script creates the `airlinemanagementsystem` database, tables, and a default admin account.
    
    **Command Line Example:**
    ```bash
    mysql -u root -p < database_setup.sql
    ```

## Step 2: Import Project into Eclipse

1.  Open Eclipse.
2.  Go to **File > New > Java Project**.
3.  Uncheck "Use default location" and browse to the **root folder** of this project (where `src` is located).
    *   *Alternatively, create a new Java Project and copy the `src` folder contents into it.*
4.  Click **Finish**.

## Step 3: Configure Build Path (Libraries)

Since the project uses external libraries, you must add them to your Eclipse project:

1.  Right-click on your project in the **Package Explorer**.
2.  Select **Build Path > Configure Build Path...**
3.  Go to the **Libraries** tab (or "Classpath" in newer Eclipse versions).
4.  Click **Add External JARs...**
5.  Select the JAR files you downloaded (**mysql-connector**, **rs2xml**, **jcalendar**, **itextpdf**).
6.  Click **Apply and Close**.

## Step 4: Configure Database Connection

1.  Open `src/airlinemanagementsystem/Conn.java` in Eclipse.
2.  Update the connection string with your MySQL username and password:
    ```java
    c = DriverManager.getConnection("jdbc:mysql://localhost:3306/airlinemanagementsystem", "YOUR_USERNAME", "YOUR_PASSWORD");
    ```
    *   Default is `root` / `@waisshabbir0810`. Update it to match your local MySQL setup.

## Step 5: Run the Application

1.  Navigate to `src/airlinemanagementsystem/Login.java`.
2.  Right-click on the file and select **Run As > Java Application**.
3.  Login with:
    *   **Username**: `admin`
    *   **Password**: `admin`

## Troubleshooting

*   **"package net.proteanit.sql does not exist"**: You are missing `rs2xml.jar`.
*   **"package com.toedter.calendar does not exist"**: You are missing `jcalendar.jarjcalendar.jar`.
*   **"package com.itextpdf.text does not exist"**: You are missing `itextpdf.jar`.
*   **"No suitable driver found"**: You are missing `mysql-connector-java.jar` or have an issue with your connection string.
