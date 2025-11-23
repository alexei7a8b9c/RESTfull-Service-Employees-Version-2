-- Создание таблиц
CREATE TABLE departments (
                             id BIGSERIAL PRIMARY KEY,
                             name VARCHAR(255) NOT NULL UNIQUE,
                             description TEXT,
                             is_deleted BOOLEAN DEFAULT FALSE,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE employees (
                           id BIGSERIAL PRIMARY KEY,
                           first_name VARCHAR(255) NOT NULL,
                           last_name VARCHAR(255) NOT NULL,
                           email VARCHAR(255) UNIQUE,
                           phone VARCHAR(50),
                           status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
                           photo_url TEXT,
                           hire_date DATE DEFAULT CURRENT_DATE,
                           manager_id BIGINT REFERENCES employees(id),
                           is_deleted BOOLEAN DEFAULT FALSE,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE employee_departments (
                                      employee_id BIGINT NOT NULL REFERENCES employees(id),
                                      department_id BIGINT NOT NULL REFERENCES departments(id),
                                      PRIMARY KEY (employee_id, department_id)
);

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для улучшения производительности
CREATE INDEX idx_employees_status ON employees(status);
CREATE INDEX idx_employees_is_deleted ON employees(is_deleted);
CREATE INDEX idx_departments_is_deleted ON departments(is_deleted);
CREATE INDEX idx_employees_manager_id ON employees(manager_id);
CREATE INDEX idx_employee_departments_employee_id ON employee_departments(employee_id);
CREATE INDEX idx_employee_departments_department_id ON employee_departments(department_id);