insert into questions (pregunta,option1,option2,option3,option4,correctOption,category_id) values
("¿Que pais es el mas grande?","Argentina","Brasil", "Usa","Rusia",4,1),
("¿En cuál de los siguientes países NO hay ningún desierto?","España","Chile", "Mongolia","Alemania",4,1),
("Cuanto es 8x4?","32","24", "40","48",1,2),
("¿A que pais pertenece la Isla de Tasmania?","Estados Unidos", "España", "Australia", "Portugal",3,1),
("¿Cuanto es (9-6)*5?","15","9","16","18",1,2),
("¿Cuanto es 7*7?","48","49","47","56",2,2),
("A medida que uno se aleja de la superficie terrestre, ¿Que sucede con la densidad del aire?","Se mantiene constante","Aumenta","Disminuye","Varia segun el punto geografico",3,6),
("¿Con que metal se protejen de las radiaciones los tecnicos y medicos radiólogos?","con hierro","con plomo","con oro","con cobre",2,6),
("¿Que medida mide doce pulgadas?","Un pie","Una yarda","Un palmo","Ninguna de las anteriores",1,6),
("¿Que contienen las bolsitas anti-humedad que vienen en las cajas de los aparatos electronicos?","Talco","Sal","Siliconas","Otras sales",3,6),
("¿De que continente es originario el garbanzo?","America","Europa","Africa","Asia",2,6),
("¿En que zona Argentina es caracteristico el viento zonda?","Cuyo","Patagonia","Mesopotamia","Ninguna de las anteriores",1,1),
("Si usted aterriza en el aeropuerto de sauce viejo, ¿En que provincia se encuentra?","Cordoba","Entre Rios","Buenos Aires","Santa Fe",1,4),
("¿Cual es la rama mayoritaria del islam?","Chiísmo","Sunismo","Jariyismo","Sufismo",2,4),
("¿Quién pronunció la frase: 'Bebamos de la copa de la destrucción'?","Mussolini","Hitler","Berlusconi","Gengis Kan",4,4),
("El Renacimiento marcó el inicio de la Edad...","Contemporanea","Media","Moderna","Antigua",3,4),
("¿Qué se celebra el 8 de Marzo?","El dia de la mujer","El dia del trabajo","El dia del medio ambiente","El dia del niño",1,4),
("¿Dónde surgió la filosofía?","España","Italia","Japon","Grecia",4,4),
("¿Cuántas finales del mundo jugó la Selección Argentina de fútbol?","3","4","5","6",3,3),
(" ¿Cuántos jugadores componen un equipo de rugby?","12","17","15","13",3,3),
("¿Qué selección acumula mayor cantidad de expulsados en  mundiales de fútbol?","Argentina","Brasil","Holanda","Italia",1,2),
("¿Quién es la mascota de SEGA?","Mario","Pacman","Ryu","Sonic",4,7);


insert into categories (name) values 
("Geografia"),
("Matematica"),
("Deporte"),
("Historia"),
("Arte"),
("Ciencia"),
("Entretenimiento");

insert into users (username,password,email,rightAnswers,wrongAnswers,admin) values
("seba","seba","sebaemail",0,0,true),
("juanma","juanma","juanmaemail",0,0,true);

select * from users