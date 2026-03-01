-- Central Authentication Table
CREATE TABLE auth_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(15) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('FARMER', 'CORPORATE', 'WORKER', 'OWNER') NOT NULL,
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Farmer Profile
CREATE TABLE farmer_profiles (
    user_id BIGINT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    location VARCHAR(255),
    land_size_acres DECIMAL(10,2),
    FOREIGN KEY (user_id) REFERENCES auth_users(id) ON DELETE CASCADE
);

-- Corporate Profile
CREATE TABLE corporate_profiles (
    user_id BIGINT PRIMARY KEY,
    company_name VARCHAR(150) NOT NULL,
    gst_number VARCHAR(50),
    headquarters_address VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES auth_users(id) ON DELETE CASCADE
);

-- Worker Profile
CREATE TABLE worker_profiles (
    user_id BIGINT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    primary_skill VARCHAR(100), -- e.g., 'Sowing', 'Harvesting'
    daily_wage DECIMAL(10,2),
    experience_years INT,
    FOREIGN KEY (user_id) REFERENCES auth_users(id) ON DELETE CASCADE
);

-- Equipment Owner Profile
CREATE TABLE owner_profiles (
    user_id BIGINT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    fleet_size INT DEFAULT 0,
    base_location VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES auth_users(id) ON DELETE CASCADE
);
