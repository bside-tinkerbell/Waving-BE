CREATE DATABASE user
;

CREATE TABLE `user`.`user` (
    user_id int unsigned NOT NULL AUTO_INCREMENT
    , username varchar(50) NOT NULL
    , login_type tinyint unsigned NOT NULL
    , joined_at datetime DEFAULT CURRENT_TIMESTAMP
    , PRIMARY KEY (user_id)
    , UNIQUE INDEX unique_username (username)
) ENGINE = InnoDB
;

CREATE TABLE `user`.`self_authentication` (
    self_authentication_id int unsigned NOT NULL AUTO_INCREMENT
    , user_id int unsigned NOT NULL
    , gather_agree tinyint unsigned NOT NULL
    , first_name varchar(128) NOT NULL
    , last_name varchar(128) NOT NULL
    , birthday date NOT NULL
    , cellphone varchar(20) NOT NULL
    , authenticated_at datetime ON UPDATE CURRENT_TIMESTAMP
    , PRIMARY KEY (self_authentication_id)
    , UNIQUE INDEX unique_user_id (user_id)
) ENGINE = InnoDB
;