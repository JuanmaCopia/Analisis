CREATE TABLE games (
  id  INT(11) NOT NULL auto_increment PRIMARY KEY,
  user_id INT(11),
  cantPreg INT(11),
  state enum('Game_Over','Game_In_Progress'),
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;