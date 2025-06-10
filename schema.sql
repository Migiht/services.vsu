CREATE DATABASE IF NOT EXISTS company_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE company_db;

-- Drop existing procedures and functions if they exist to allow recreation
DROP PROCEDURE IF EXISTS GetProjectCost;
DROP PROCEDURE IF EXISTS GetProgrammerSalary;
DROP FUNCTION IF EXISTS CountWorkDays;

-- Drop tables if they exist to start fresh
DROP TABLE IF EXISTS programmers;
DROP TABLE IF EXISTS projects;

-- Create projects table (without cost column)
CREATE TABLE projects (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    customer VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create programmers table (without total_salary column)
CREATE TABLE programmers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_id BIGINT,
    last_name VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    patronymic VARCHAR(255),
    position VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    hourly_rate DECIMAL(10, 2) NOT NULL,
    full_time BOOLEAN NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE SET NULL
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Stored Function to calculate work days (excluding weekends)
DELIMITER $$
CREATE FUNCTION CountWorkDays(start_d DATE, end_d DATE)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE work_days INT DEFAULT 0;
    DECLARE current_d DATE;
    
    IF start_d IS NULL OR end_d IS NULL OR start_d > end_d THEN
        RETURN 0;
    END IF;

    SET current_d = start_d;
    WHILE current_d <= end_d DO
        -- DAYOFWEEK: 1=Sun, 2=Mon, ..., 7=Sat
        IF DAYOFWEEK(current_d) NOT IN (1, 7) THEN
            SET work_days = work_days + 1;
        END IF;
        SET current_d = DATE_ADD(current_d, INTERVAL 1 DAY);
    END WHILE;
    RETURN work_days;
END$$
DELIMITER ;

-- Stored Procedure for Programmer Salary
DELIMITER $$
CREATE PROCEDURE GetProgrammerSalary(IN prog_id BIGINT, OUT salary DECIMAL(15, 2))
BEGIN
    DECLARE v_start_date, v_end_date, v_effective_end_date DATE;
    DECLARE v_hourly_rate DECIMAL(10, 2);
    DECLARE v_full_time BOOLEAN;
    DECLARE v_work_days INT;
    DECLARE v_hours_per_day DECIMAL(4,1);
    
    SELECT start_date, end_date, hourly_rate, full_time
    INTO v_start_date, v_end_date, v_hourly_rate, v_full_time
    FROM programmers WHERE id = prog_id;

    SET v_effective_end_date = IFNULL(v_end_date, CURDATE());
    SET v_work_days = CountWorkDays(v_start_date, v_effective_end_date);
    SET v_hours_per_day = IF(v_full_time, 8.0, 4.0);
    
    SET salary = v_work_days * v_hours_per_day * v_hourly_rate * 1.77;
END$$
DELIMITER ;

-- Stored Procedure for Project Cost
DELIMITER $$
CREATE PROCEDURE GetProjectCost(IN proj_id BIGINT, OUT cost DECIMAL(15, 2))
BEGIN
    DECLARE total_salary_sum DECIMAL(15, 2) DEFAULT 0;
    DECLARE current_prog_id BIGINT;
    DECLARE current_prog_salary DECIMAL(15, 2);
    DECLARE done INT DEFAULT FALSE;
    
    DECLARE prog_cursor CURSOR FOR SELECT id FROM programmers WHERE project_id = proj_id;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN prog_cursor;

    read_loop: LOOP
        FETCH prog_cursor INTO current_prog_id;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        CALL GetProgrammerSalary(current_prog_id, current_prog_salary);
        IF current_prog_salary IS NOT NULL THEN
            SET total_salary_sum = total_salary_sum + current_prog_salary;
        END IF;
    END LOOP;

    CLOSE prog_cursor;

    SET cost = total_salary_sum * 2;
END$$
DELIMITER ; 