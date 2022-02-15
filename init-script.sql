CREATE DATABASE IF NOT EXISTS demo;
USE demo;

CREATE TABLE IF NOT EXISTS demo.quotes (
    id int NOT NULL AUTO_INCREMENT,
    quote varchar(255) NOT NULL,
    PRIMARY KEY (id)
);