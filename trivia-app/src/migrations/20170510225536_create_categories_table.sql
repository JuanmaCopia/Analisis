create table categories (
id INT(11) NOT NULL auto_increment PRIMARY KEY,
name VARCHAR(20)
)ENGINE=InnoDB;

insert into categories (name) values
("Geografia"),
("Matematica"),
("Deporte"),
("Historia"),
("Arte"),
("Ciencia"),
("Entretenimiento");