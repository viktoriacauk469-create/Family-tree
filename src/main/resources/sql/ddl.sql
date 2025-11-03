CREATE TABLE users
(
    id        BIGSERIAL PRIMARY KEY,
    email     VARCHAR(255) UNIQUE NOT NULL,
    password  VARCHAR(255)        NOT NULL,
    role      VARCHAR(20)         NOT NULL
);

CREATE TABLE user_verifications
(
    id                        BIGSERIAL PRIMARY KEY,
    user_id                   BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    verification_token        VARCHAR(512),
    verification_token_expiry TIMESTAMP,
    is_verified               BOOLEAN NOT NULL
);