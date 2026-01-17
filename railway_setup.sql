CREATE DATABASE IF NOT EXISTS pakrailways;
USE pakrailways;

CREATE TABLE IF NOT EXISTS login (
    username VARCHAR(50),
    password VARCHAR(50)
);

INSERT INTO login (username, password) VALUES ('admin', 'awais0810');

CREATE TABLE IF NOT EXISTS passenger (
    name VARCHAR(50),
    nationality VARCHAR(50),
    phone VARCHAR(20),
    address VARCHAR(100),
    cnic VARCHAR(20),
    gender VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS train (
    train_code VARCHAR(20),
    train_name VARCHAR(50),
    source VARCHAR(50),
    destination VARCHAR(50),
    price DECIMAL(10,2)
);

INSERT INTO train VALUES ('101', 'Green Line', 'Karachi', 'Islamabad', 6000);
INSERT INTO train VALUES ('102', 'Tezgam', 'Karachi', 'Rawalpindi', 3500);
INSERT INTO train VALUES ('103', 'Khyber Mail', 'Karachi', 'Peshawar', 4000);
INSERT INTO train VALUES ('104', 'Karakoram Express', 'Karachi', 'Lahore', 5000);
INSERT INTO train VALUES ('105', 'Allama Iqbal Express', 'Sialkot', 'Karachi', 3000);

CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50),
    full_name VARCHAR(50),
    cnic VARCHAR(20),
    phone VARCHAR(20),
    address VARCHAR(100)
);

DROP TABLE IF EXISTS reservation;
CREATE TABLE IF NOT EXISTS reservation (
    PNR VARCHAR(20),
    ticket VARCHAR(20),
    account_username VARCHAR(50), 
    cnic VARCHAR(20), 
    name VARCHAR(50),
    nationality VARCHAR(50),
    train_name VARCHAR(50),
    train_code VARCHAR(20),
    src VARCHAR(50),
    des VARCHAR(50),
    ddate VARCHAR(30),
    tickets VARCHAR(5),
    class VARCHAR(20),
    seats VARCHAR(100)
);


CREATE TABLE IF NOT EXISTS cancel (
    pnr VARCHAR(20),
    name VARCHAR(50),
    cancel_id VARCHAR(20),
    train_code VARCHAR(20),
    date VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS payments (
    pnr VARCHAR(20),
    card_number VARCHAR(20),
    card_holder_name VARCHAR(50),
    expiry_date VARCHAR(10),
    cvv VARCHAR(5),
    amount DECIMAL(10,2)
);
