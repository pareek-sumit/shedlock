# Docker Compose Setup for MySQL and Adminer

This guide explains how to set up a Docker Compose environment with MySQL and Adminer, create tables, and insert sample data into the database.

## Prerequisites

- **Docker**: Ensure Docker is installed and running on your machine.
- **Docker Compose**: Ensure Docker Compose is installed.

## Docker Compose Configuration

1. **Create a `docker-compose.yml` File**

   Create a file named `docker-compose.yml` with the following content:

   ```yaml
   version: '3.8'

   services:
     mysql:
       image: mysql:8.0
       container_name: mysql-db
       environment:
         MYSQL_ROOT_PASSWORD: rootpassword
         MYSQL_DATABASE: mydatabase
         MYSQL_USER: user
         MYSQL_PASSWORD: password
       ports:
         - "3306:3306"
       volumes:
         - mysql_data:/var/lib/mysql

     adminer:
       image: adminer
       container_name: adminer
       ports:
         - "8080:8080"

   volumes:
     mysql_data:
****
    docker-compose up -d
****
      drop table My_entity
      drop table numbers;
      drop table Tracking;
   
****
# Entity Table
    CREATE TABLE My_entity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data VARCHAR(255)
    );
#   Insert 5000 rows into My_entity with sequential IDs and example data
      Create the numbers table if it doesn't exist
      CREATE TABLE IF NOT EXISTS numbers (
      number INT PRIMARY KEY
      );
      
      -- Populate the numbers table with values from 1 to 5000
      INSERT IGNORE INTO numbers (number)
      SELECT a.N + b.N * 10 + c.N * 100 + d.N * 1000 + 1 AS number
      FROM (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a,
      (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b,
      (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) c,
      (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) d
      ORDER BY number
      LIMIT 5000;


      INSERT INTO My_entity (data)
      SELECT CONCAT('Sample Data ', number)
      FROM numbers;

# Tracking table
    CREATE TABLE Tracking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    from_id BIGINT,
    to_id BIGINT,
    created_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_updated_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    instance_id VARCHAR(255),
    status ENUM('New', 'Processing', 'Success', 'Failure') NOT NULL
   );

# ShedLock table
      CREATE TABLE shedlock (
      name VARCHAR(64) PRIMARY KEY,
      lock_until TIMESTAMP(6) NOT NULL,
      locked_at TIMESTAMP(6) NOT NULL,
      locked_by VARCHAR(255) NOT NULL
      );

