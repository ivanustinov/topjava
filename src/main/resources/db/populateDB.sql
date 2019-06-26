DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (user_id, datetime, description, calories) VALUES
(100000, '2015-05-30T10:00', 'Завтрак', 200),
(100000, '2015-05-30T13:30', 'Обед', 600),
(100000, '2015-06-30T18:00', 'Ужин', 700),
(100001, '2015-06-29T10:00', 'Завтрак', 200),
(100001, '2015-06-29T13:30', 'Обед', 600),
(100001, '2015-06-29T18:00', 'Ужин', 700),
(100001, '2015-06-30T09:00', 'Завтрак', 1000),
(100001, '2015-06-30T13:00', 'Обед', 700),
(100001, '2015-06-30T17:00', 'Ужин', 800);
