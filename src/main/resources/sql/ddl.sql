-- ============================================
-- DDL: family-tree Database Schema
-- Author: Andrii Kachmar
-- ============================================

-- ============================================
-- 1️⃣ USERS
-- ============================================
CREATE TABLE users
(
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(255) UNIQUE NOT NULL,
    password      VARCHAR(255)        NOT NULL,
    role          VARCHAR(20)         NOT NULL,
    blood_type    VARCHAR(20),
    rhesus_factor VARCHAR(10)
);

-- ============================================
-- 2️⃣ USER_VERIFICATIONS
-- ============================================
CREATE TABLE user_verifications
(
    id                        BIGSERIAL PRIMARY KEY,
    user_id                   BIGINT  NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    verification_token        VARCHAR(512),
    verification_token_expiry TIMESTAMP,
    is_verified               BOOLEAN NOT NULL
);

-- ============================================
-- 3️⃣ PERSONAL_INFORMATION
-- ============================================
CREATE TABLE personal_information
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT REFERENCES users (id) ON DELETE CASCADE, -- nullable, person may not be registered
    first_name VARCHAR(100),
    last_name  VARCHAR(100),
    age        INT
);

-- ============================================
-- 4️⃣ RELATIVES (Self-referencing many-to-many)
-- ============================================
CREATE TABLE relatives
(
    person_id   BIGINT NOT NULL REFERENCES personal_information (id) ON DELETE CASCADE,
    relative_id BIGINT NOT NULL REFERENCES personal_information (id) ON DELETE CASCADE,
    PRIMARY KEY (person_id, relative_id)
);
