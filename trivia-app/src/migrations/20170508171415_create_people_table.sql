CREATE TABLE users (
  id INT(11) NOT NULL auto_increment PRIMARY KEY,
  username VARCHAR(128),
  password  VARCHAR(128),
  email VARCHAR(128),
  rightAnswers INT,
  wrongAnswers INT,
  admin BOOLEAN,
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;

insert into users (username,password,email,rightAnswers,wrongAnswers,admin) values
("seba","seba","seba@gmail.com",12,0,true),
("juanma","juanma","juanma@gmail.com",25,0,true),
("pepe","seba","pepe@gmail.com",66,0,false),
("jose","seba","jose@gmail.com",54,0,false),
("cacho","cacho","cacho@gmail.com",125,0,false);