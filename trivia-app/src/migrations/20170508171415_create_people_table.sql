CREATE TABLE users (
  id  int(11) NOT NULL auto_increment PRIMARY KEY,
  username VARCHAR(128),
  password  VARCHAR(128),
  score int,
  admin BOOL,
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB
