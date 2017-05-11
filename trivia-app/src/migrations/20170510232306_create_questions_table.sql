create table questions (
question_id INT NOT NULL auto_increment primary key,
pregunta VARCHAR(250),
option1 VARCHAR(35),
option2 VARCHAR(35),
option3 VARCHAR(35),
correct_option VARCHAR(35),
category_name VARCHAR(20),
constraint fkcategories FOREIGN KEY (category_name) REFERENCES categories(category_name)
on delete no action
)ENGINE=InnoDB;