-- Вставка тестовых данных для отделов
INSERT INTO departments (name, description) VALUES
                                                ('IT', 'Information Technology Department'),
                                                ('HR', 'Human Resources Department'),
                                                ('Finance', 'Finance and Accounting Department'),
                                                ('Marketing', 'Marketing and Sales Department'),
                                                ('Operations', 'Operations Department');

-- Вставка тестовых данных для сотрудников
INSERT INTO employees (first_name, last_name, email, phone, status, hire_date) VALUES
                                                                                   ('John', 'Doe', 'john.doe@company.com', '+1234567890', 'ACTIVE', '2020-01-15'),
                                                                                   ('Jane', 'Smith', 'jane.smith@company.com', '+1234567891', 'ACTIVE', '2019-03-20'),
                                                                                   ('Mike', 'Johnson', 'mike.johnson@company.com', '+1234567892', 'ACTIVE', '2021-06-10'),
                                                                                   ('Sarah', 'Wilson', 'sarah.wilson@company.com', '+1234567893', 'ON_LEAVE', '2018-11-05'),
                                                                                   ('David', 'Brown', 'david.brown@company.com', '+1234567894', 'ACTIVE', '2022-02-28');

-- Обновление менеджеров (John Doe менеджер для других)
UPDATE employees SET manager_id = 1 WHERE id IN (2, 3, 4, 5);

-- Связи сотрудников с отделами
INSERT INTO employee_departments (employee_id, department_id) VALUES
                                                                  (1, 1), (1, 3), -- John Doe в IT и Finance
                                                                  (2, 1), (2, 4), -- Jane Smith в IT и Marketing
                                                                  (3, 2),         -- Mike Johnson в HR
                                                                  (4, 4), (4, 5), -- Sarah Wilson в Marketing и Operations
                                                                  (5, 3), (5, 5); -- David Brown в Finance и Operations

-- Вставка тестовых пользователей
INSERT INTO users (username, password, role) VALUES
                                                 ('admin', 'admin123', 'ADMIN'),
                                                 ('user', 'user123', 'USER');