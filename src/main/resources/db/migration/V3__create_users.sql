-- Создание таблицы users
CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Создание индексов
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);

-- Удаляем старых пользователей если есть
DELETE FROM users WHERE username IN ('admin', 'user');

-- Вставляем пользователей с простыми паролями для тестирования
INSERT INTO users (username, password, role) VALUES
    ('admin', 'admin123', 'ADMIN');

INSERT INTO users (username, password, role) VALUES
    ('user', 'user123', 'USER');