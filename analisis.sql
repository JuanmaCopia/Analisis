insert into questions (pregunta,option1,option2,option3,option4,correctOption,category_id) values
("¿Que pais es el mas grande?","Argentina","Brasil", "Usa","Rusia",4,1),
("¿En cuál de los siguientes países NO hay ningún desierto?","España","Chile", "Mongolia","Alemania",1,1),
("Cuanto es 8x4?","32","24", "40","48",1,2),
("¿A que pais pertenece la Isla de Tasmania?","Estados Unidos", "España", "Australia", "Portugal",3,1),
("¿Cuanto es (9-6)*5?","15","9","16","18",1,2),
("¿Cuanto es 7*7?","48","49","47","56",2,2);

insert into categories (name) values 
("Geografia"),
("Deporte"),
("Matematica"),
("Historia"),
("Arte"),
("Ciencia");

insert into users (username,password,email,rightAnswers,wrongAnswers,admin) values
("seba","seba","sebaemail",0,0,true),
("juanma","juanma","juanmaemail",0,0,true);

select * from users