DROP TABLE IF EXISTS "user" CASCADE;
CREATE TABLE "user"
(
    id          SERIAL PRIMARY KEY,
    email       VARCHAR(255) NOT NULL,
    first_name  VARCHAR(255) NOT NULL,
    last_name   VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    create_time TIMESTAMP(0) NOT NULL DEFAULT NOW(),
    update_time TIMESTAMP(0) NOT NULL DEFAULT NOW()
);

DROP TABLE IF EXISTS user_role CASCADE;
CREATE TABLE user_role
(
    user_id INT REFERENCES "user" (id) NOT NULL,
    role    VARCHAR(255)               NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE
);

CREATE INDEX user_role_to_user ON user_role (user_id);