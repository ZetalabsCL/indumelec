insert ignore into role values (1, 'Admin');
insert ignore into role values (2, 'User');


INSERT INTO user (name, mail, password, status, updated_by, updated_time)
SELECT * FROM (SELECT 'Admin' name, 'admin@indumelec.com','admin',1,'system', now()) AS tmp
WHERE NOT EXISTS (
  SELECT name FROM user WHERE name = 'Admin'
) LIMIT 1;

insert ignore into user_role values (1,1);