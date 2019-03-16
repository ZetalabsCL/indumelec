insert ignore into role values (1, 'Admin');
insert ignore into role values (2, 'User');


INSERT INTO user (name, email, password, active, updated_by, updated_on)
SELECT * FROM (SELECT 'Admin' name, 'admin@zetalabs.cl','$2a$10$Pbx6eOGwJAhapg36tz8mU.YgOgWDnleR4kBMZXRwPYChRVweu34j2','Y','system', now()) AS tmp
WHERE NOT EXISTS (
  SELECT name FROM user WHERE name = 'Admin'
) LIMIT 1;

insert ignore into user_role values (1,1);