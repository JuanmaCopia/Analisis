CREATE TABLE matches (
  id  INT(11) NOT NULL auto_increment PRIMARY KEY,
  user1Id INT(11),
  user2Id INT(11),
  winnerId INT(11),
  state enum('Game_Over','Game_In_Progress'),
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;
