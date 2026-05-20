-- Create the database (Run this manually if needed, or just create it via pgAdmin)
-- CREATE DATABASE carrental;

-- Drop tables if they exist to allow clean re-runs
DROP TABLE IF EXISTS rents;
DROP TABLE IF EXISTS cars;
DROP TABLE IF EXISTS users;

-- 1. Table for User Accounts
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(20)
);

-- 2. Table for Car Inventory
CREATE TABLE cars (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    price_per_day DECIMAL(10, 2) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE
);

-- 3. Table for Rental Transactions (Foreign Keys link to users and cars)
CREATE TABLE rents (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    car_id INT REFERENCES cars(id) ON DELETE CASCADE,
    rental_date DATE DEFAULT CURRENT_DATE,
    return_date DATE NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL
);

-- Insert some sample data for testing
INSERT INTO cars (brand, model, year, price_per_day, is_available) VALUES
('Dacia', 'Logan', 2022, 30.00, TRUE),
('BMW', '3 Series', 2020, 70.00, TRUE),
('Audi', 'A4', 2021, 65.00, TRUE),
('Volkswagen', 'Golf', 2019, 40.00, TRUE);