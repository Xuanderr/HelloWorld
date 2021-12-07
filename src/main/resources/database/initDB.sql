CREATE TABLE IF NOT EXISTS clients
(
    id  BIGSERIAL PRIMARY KEY ,
    user_name  text NOT NULL ,
    email text NOT NULL ,
    phone text  NOT NULL
);