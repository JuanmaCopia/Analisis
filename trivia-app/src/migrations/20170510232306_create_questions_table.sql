create table questions (
id INT(11) NOT NULL auto_increment primary key,
pregunta VARCHAR(250),
option1 VARCHAR(35),
option2 VARCHAR(35),
option3 VARCHAR(35),
option4 VARCHAR(35),
correctOption INT(1),
category_id INT(11)
)ENGINE=InnoDB;

insert into questions (pregunta,option1,option2,option3,option4,correctOption,category_id) values
("¿Que pais es el mas grande?","Argentina","Brasil", "Usa","Rusia",4,1),
("¿En cuál de los siguientes países NO hay ningún desierto?","España","Chile", "Mongolia","Alemania",4,1),
("Cuanto es 8x4?","32","24", "40","48",1,2),
("¿A que pais pertenece la Isla de Tasmania?","Estados Unidos", "España", "Australia", "Portugal",3,1),
("¿Cuanto es (9-6)*5?","15","9","16","18",1,2),
("¿Cuanto es 7*7?","48","49","47","56",2,2),
("A medida que uno se aleja de la superficie terrestre, ¿Que sucede con la densidad del aire?","Se mantiene constante","Aumenta","Disminuye","Ninguna de las anteriores",3,6),
("¿Con que metal se protejen de las radiaciones los tecnicos y medicos radiólogos?","con hierro","con plomo","con oro","con cobre",2,6),
("¿Que medida mide doce pulgadas?","Un pie","Una yarda","Un palmo","Ninguna de las anteriores",1,6),
("¿Que contienen las bolsitas anti-humedad que vienen en las cajas de los aparatos electronicos?","Talco","Sal","Siliconas","Otras sales",3,6),
("¿De que continente es originario el garbanzo?","America","Europa","Africa","Asia",2,6),
("¿En que zona Argentina es caracteristico el viento zonda?","Cuyo","Patagonia","Mesopotamia","Ninguna de las anteriores",1,1),
("Si usted aterriza en el aeropuerto de sauce viejo, ¿En que provincia se encuentra?","Cordoba","Entre Rios","Buenos Aires","Santa Fe",4,4),
("¿Cual es la rama mayoritaria del islam?","Chiísmo","Sunismo","Jariyismo","Sufismo",2,4),
("¿Quién pronunció la frase: 'Bebamos de la copa de la destrucción'?","Mussolini","Hitler","Berlusconi","Gengis Kan",4,4),
("El Renacimiento marcó el inicio de la Edad...","Contemporanea","Media","Moderna","Antigua",3,4),
("¿Qué se celebra el 8 de Marzo?","El dia de la mujer","El dia del trabajo","El dia del medio ambiente","El dia del niño",1,4),
("¿Dónde surgió la filosofía?","España","Italia","Japon","Grecia",4,4),
("¿Cuántas finales del mundo jugó la Selección Argentina de fútbol?","3","4","5","6",2,3),
("¿Cuántos jugadores componen un equipo de rugby?","12","17","15","13",3,3),
("¿Qué selección acumula mayor cantidad de expulsados en  mundiales de fútbol?","Argentina","Brasil","Holanda","Italia",1,3),
("¿Quién es la mascota de SEGA?","Mario","Pacman","Ryu","Sonic",4,7),
("¿Cuál es el país menos turístico de Europa?","Armenia","Moldavia","Liechtenstein","Hungría",3,1),
("¿Cuál es el código internacional para Cuba?","CA","CU","CB","Ninguna es correcta",2,1),
("¿Cuál es la capital del estado de Arkansas?","Kansas","Little Rock","Hot Springs","Washington",2,1),
("¿En qué país situarías la ciudad de Cali?","Colombia","Venezuela","Costa Rica","Chile",1,1),
("¿Quién pintó el cuado El jardín de las delicias?","El Bosco","Carvaggio","Velázquez","Arcimboldo",1,5),
("¿Quién vivía en el 221B de Backer Street?","Sherlock Holmes","Truman Capote","Philip Marlowe","Arthur Conan Doyle",1,5),
("¿Quién es el autor de El retrato de Dorian Gray?","Oscar Wilde","Charles Dickens","Arthur Conan Doyle","George Orwell",1,5),
("¿Qué forma es característica de las plantas de las iglesias románicas?","Óvalo","Rectángulo","Cruz","Cuadrado",3,5),
("¿Qué odia Mafalda?","El Pájaro Loco","La sopa","Los panqueques","A Manolito",2,5),
("¿Quién compuso la famosa canción llamada Bohemian Rhapsody?","John Lennon","Elton John","Freddie Mercury","David Bowie",3,5),
("¿Cuál de estas características no pertenece al clima Mediterráneo?","Veranos secos y calurosos","Es un tipo de clima templado","Lluvias muy abundantes","Variables temperaturas en primavera",3,1),
("¿Qué es el Cabo de Creus?","El punto más oriental de España","El punto más oriental de la Península","El punto más oriental de Cataluña","Ninguna es correcta",2,1),
("¿Cuál es principal celebración holandesa?","Navidad","La llegada del verano","El día de la Reina","Hallowen",3,1),
("¿Cuál de las siguientes especialidades NO es típica de la cocina estadounidense?","La hamburguesa","El pastel de cangrejo","La tarta de manzana","Todas son típicas",4,1),
("¿Con cuántos países limita Argentina?","Tres","Cuatro","Cinco","Seis",3,1),
("¿Qué es la UA?","Unión Austrohúngara","Unión Africana","Unión Americana","Unión Afroamericana",2,1),
("¿Cuál es la capital de Suiza?","Berna","Zurich","Ginebra","Basilea",1,1),
("¿Qué separa las franjas de Gaza y Cisjordania?","Un muro","Nada","Israel","Un río",3,1),
("¿En qué país está Ushuaia, la ciudad más al sur del mundo?","Chile","Argentina","Sudáfrica","Nueva Zelanda",2,1),
("¿Quién fue galardonado con el premio Nobel de la Paz en 2007?","Albert Einstein","Albert Gore","Albert Schweitzer","Albert Van Bommel",2,4),
("¿Cómo llamaban a Dios los testigos de Jehová?","Mutante","Buda","Jehová","Jesús",3,4),
("¿Cómo se llama la sustancia que se utiliza en la Iglesia para hacer mucho humo?","Vino","Carbón","Aceite","Incienso",4,4),
("¿Qué modelo de Estado se propugna en la Constitución española?","Estado federal","Estado social y democrático de Derecho","Estado de Derecho","Estado liberal",2,4),
("¿Cómo se llama la capital del antiguo imperio azteca?","Quetzalcoatl","Tenochtitlan","Culhuacan","Texcoco",2,4),
("Los cuatro evangelistas de la Biblia son Mateo, Marcos, Lucas y...","Antonio","Jésus","José","Juan",4,4),
("¿Quién gobernó Francia desde 1799 a 1815?","Adam Smith","François Quesnay","Napoleón Bonaparte","Louis Bonaldgug",3,4),
("¿Qué modelo de Seat significó la motorización de los españoles?","500","Ibiza","600","León",3,4),
("¿Quién presidia España durante el fallido golpe de estado del 23 de Febrero de 1981?","Adolfo Suárez","Leopoldo Calvo Sotelo","Felipe Gonzalez","Manual Fraga Iribarne",1,4),
("¿Cuál es la capital del noroeste de España, la capital del reino homónimo medieval?","León","Salamanca","Valladolid","Zamora",1,4),
("¿De que estaba fabricado originalmente el maquillaje blanco de las Geishas?","Harina","Arroz molido","Plomo","Flores de loto",3,4),
("¿Cuál de estos nombres en clave no se corresponde con el de una playa del desembarco de Normandía?","Utah","Todos son correctos","Juno","Sword",2,4),
("¿Qué motivó la rebelión que dio lugar a la Guerra de la Independencia de EEUU?","Esclavitud","Impuesto","Corrupción","Represión",2,4);