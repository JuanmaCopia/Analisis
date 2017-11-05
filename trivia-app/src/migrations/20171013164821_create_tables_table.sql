CREATE TABLE tables (
    id  INT(11) NOT NULL auto_increment PRIMARY KEY,
    owner_id INT(11),
    guest_id  INT(11),
    is_full BOOLEAN
)ENGINE=InnoDB;
