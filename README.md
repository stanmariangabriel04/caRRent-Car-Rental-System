
# Car Rental Management System

This is a desktop application written in Java using JavaFX. The project implements a basic car rental system where users can create accounts, log in, and interact with a database to manage rentals.

## Features

* User registration and login
* Basic account handling
* Viewing available cars
* Renting cars
* Data persistence using PostgreSQL

[![Car Rental Project](https://img.youtube.com/vi/zRIldJ61eHo/maxresdefault.jpg)](https://www.youtube.com/watch?v=zRIldJ61eHo)

## Technologies

* Java
* JavaFX
* PostgreSQL
* JDBC
* Maven

## Project structure

src/main/java

* Main.java – entry point of the application
* Controller.java – handles login
* SignUpController.java – handles registration
* LoggedInController.java – user interface after login
* DBUtils.java – database connection and queries
* Customer, Car, Rent - model classes

src/main/resources

* FXML files for UI
* Images used in the interface
* CSS files

lib/

* PostgreSQL JDBC driver

## Setup

1. Clone the repository

git clone https://github.com/stanmariangabriel04/caRRent-Car-Rental-System

2. Create a PostgreSQL database named `carrental`

3. Open your pgAdmin (or any SQL client) and run the entire script found in the schema.sql file located in the root of this project. This will automatically create the tables and insert sample cars.

4. Update database connection details in `DBUtils.java`:

String url = "jdbc:postgresql://localhost:5432/carrental";
String user = "your_username";
String password = "your_password";



4. Run the application from `Main.java`

## Notes

This project was created to practice working with Java, JavaFX, and PostgreSQL. The database logic is handled through a utility class, and the UI is built using FXML.

## Possible improvements

* Separate database logic into DAO classes
* Add validation and better error handling
* Store passwords securely
* Improve UI design

## Author

Stan Marian Gabriel
https://github.com/stanmariangabriel04

