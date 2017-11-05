CREATE TABLE tables (
    id  INT(11) NOT NULL auto_increment PRIMARY KEY,
    owner_id INT(11),
    guest_id  INT(11),
    is_full BOOLEAN
)ENGINE=InnoDB;

insert into tables (owner_id,guest_id,is_full) values
(2,1,true),
(3,4,true),
(1,2,true),
(3,4,true),
(3,4,true),
(3,4,true),
(3,4,true);