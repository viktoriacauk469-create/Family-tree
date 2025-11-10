-- ============================================
-- DDL: family-tree Database Schema
-- Author: Andrii Kachmar
-- ============================================

-- ============================================
-- 1️⃣ USERS
-- ============================================
CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    email    VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    role     VARCHAR(20)         NOT NULL
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
    first_name VARCHAR(100),
    last_name  VARCHAR(100),
    age        INT
);

-- ============================================
-- 4️⃣ RELATIVES
-- ============================================
CREATE TABLE relatives
(
    person_id   BIGINT NOT NULL,
    relative_id BIGINT NOT NULL,
    PRIMARY KEY (person_id, relative_id),
    FOREIGN KEY (person_id) REFERENCES personal_information (id),
    FOREIGN KEY (relative_id) REFERENCES personal_information (id)
);
