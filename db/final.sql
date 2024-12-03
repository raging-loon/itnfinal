
CREATE DATABASE IF NOT EXISTS ITN261;

USE ITN261;

CREATE TABLE IF NOT EXISTS products (
	p_id 		INT PRIMARY KEY AUTO_INCREMENT,
    p_desc 		TEXT,
    p_brand 	TEXT,
    p_price 	DECIMAL(6,2),
    p_weight 	DECIMAL(6,2),
    p_rating 	DECIMAL(3,2)
);

CREATE TABLE IF NOT EXISTS users (
	u_id		INT PRIMARY KEY AUTO_INCREMENT,
    u_name		TEXT,
    u_passwd 	CHAR(64) -- 64 for SHA256
);

CREATE TABLE IF NOT EXISTS attendance_tracker (
	row_id 			INT PRIMARY KEY AUTO_INCREMENT,
    at_id  			INT UNIQUE,
    at_firstname 	TEXT,
    at_lastname  	TEXT,
    at_phone 	 	TEXT,
    at_email	 	TEXT,
    at_address	 	TEXT,
    at_zipcode	 	INT,
    at_arrival_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE USER IF NOT EXISTS '<YOUR SERVICE ACCOUNT>'@'localhost' IDENTIFIED BY '<YOUR PASSWORD>';
GRANT SELECT, UPDATE ON ITN261.products TO '<YOUR SERVICE ACCOUNT>'@'localhost';
GRANT SELECT ON ITN261.users TO '<YOUR SERVICE ACCOUNT>'@'localhost';

INSERT INTO users (u_name, u_passwd) values ('<USERNAME>','<PASSWORD HASH>');
