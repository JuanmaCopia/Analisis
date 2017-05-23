CREATE TABLE games (
  id  INT(11) NOT NULL auto_increment PRIMARY KEY,
  user_id INT(11),
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;