CREATE DATABASE greeting
;

CREATE TABLE greeting.greeting_category (
                                            greeting_category_id int unsigned NOT NULL
    , category varchar(128) NOT NULL
    , created_at datetime DEFAULT CURRENT_TIMESTAMP
    , updated_at datetime NULL ON UPDATE CURRENT_TIMESTAMP
    , PRIMARY KEY (greeting_category_id)
    , UNIQUE INDEX unique_category (category)
) ENGINE = InnoDB
;

CREATE TABLE greeting.greeting (
                                   greeting_id int unsigned NOT NULL AUTO_INCREMENT
    , greeting_category_id int unsigned NOT NULL
    , greeting varchar(128) NOT NULL
    , created_at datetime DEFAULT CURRENT_TIMESTAMP
    , updated_at datetime NULL ON UPDATE CURRENT_TIMESTAMP
    , PRIMARY KEY (greeting_id)
    , UNIQUE INDEX unique_greeting (greeting)
) ENGINE = InnoDB
;

-- CREATE TABLE greeting.greeting (
--    greeting_id int unsigned NOT NULL AUTO_INCREMENT
--     , greeting varchar(128) NOT NULL
--     , category varchar(128) NOT NULL
--     , created_at datetime DEFAULT CURRENT_TIMESTAMP
--     , updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
--     , PRIMARY KEY (greeting_id)
--     , UNIQUE INDEX unique_greeting (greeting)
-- ) ENGINE = InnoDB
-- ;