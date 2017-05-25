create table questions (
id INT(11) NOT NULL auto_increment primary key,
pregunta VARCHAR(250),
option1 VARCHAR(35),
option2 VARCHAR(35),
option3 VARCHAR(35),
corrrectOption VARCHAR(35),
category_id INT(11),
created_at DATETIME,
updated_at DATETIME
)ENGINE=InnoDB;