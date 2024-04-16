CREATE TABLE User (
                             ID_USER INT AUTO_INCREMENT PRIMARY KEY,
                             NOM_USER VARCHAR(255),
                             EMAIL VARCHAR(255),
                             UNIQUE KEY email_unique (EMAIL)
);
insert into User values (1,'omar','omar@gmail.com');
insert into User values (2,'loubna','loubna@gmail.com');
insert into User values (3,'latifa','latifa@gmail.com');
insert into User values (4,'belcaid','belcaid@gmail.com');
