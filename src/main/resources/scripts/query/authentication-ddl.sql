CREATE DATABASE authentication
;

CREATE TABLE authentication.login (
    login_id int unsigned NOT NULL AUTO_INCREMENT
    , user_id int unsigned NOT NULL
    , salt varchar(128) NOT NULL
    , password varchar(128) NOT NULL
    , updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    , PRIMARY KEY (login_id)
    , UNIQUE INDEX unique_user_id (user_id)
) ENGINE = InnoDB
;