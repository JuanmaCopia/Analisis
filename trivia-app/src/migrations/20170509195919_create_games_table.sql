CREATE TABLE games (
  game_id  int NOT NULL auto_increment PRIMARY KEY,
  id  int(11),
  constraint fkusers FOREIGN KEY (id) REFERENCES users(id)
)ENGINE=InnoDB;