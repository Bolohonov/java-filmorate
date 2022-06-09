CREATE TABLE IF NOT EXISTS director
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(50) NOT NULL
    );

CREATE TABLE IF NOT EXISTS user_filmorate
(
    id       INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name     varchar(50),
    login    varchar(30) NOT NULL,
    email    varchar(30) NOT NULL,
    birthday date
    );

CREATE TABLE IF NOT EXISTS film
(
    id           INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name         varchar(50)  NOT NULL,
    description  varchar(150) NOT NULL,
    release_date date,
    duration     int,
    rate         int,
    mpa          int,
    director_id  int references director(id)
);

CREATE TABLE IF NOT EXISTS Mpa (
   id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   title varchar(50) NOT NULL
);

ALTER TABLE film ADD CONSTRAINT IF NOT EXISTS film_mpa_to_mpa_id FOREIGN KEY (mpa)
    REFERENCES mpa;

CREATE TABLE IF NOT EXISTS friends (
    first_user_id int,
    second_user_id int,
    accept_first boolean,
    accept_second boolean
);

ALTER TABLE friends ADD CONSTRAINT IF NOT EXISTS friends_id_to_user FOREIGN KEY (first_user_id)
    REFERENCES user_filmorate ON DELETE CASCADE;
ALTER TABLE friends ADD CONSTRAINT IF NOT EXISTS friends_second_id_to_user FOREIGN KEY (second_user_id)
    REFERENCES user_filmorate ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS likes (
     film_id int,
     user_id int
);

ALTER TABLE likes ADD CONSTRAINT IF NOT EXISTS film_id_to_film FOREIGN KEY (film_id)
    REFERENCES film ON DELETE CASCADE;
ALTER TABLE likes ADD CONSTRAINT IF NOT EXISTS user_id_to_user FOREIGN KEY (user_id)
    REFERENCES user_filmorate ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS genre (
   id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   name varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre (
    film_id int PRIMARY KEY,
    genre_id int
);

ALTER TABLE film_genre ADD CONSTRAINT IF NOT EXISTS film_id_to_film FOREIGN KEY (film_id)
    REFERENCES film ON DELETE CASCADE;
ALTER TABLE film_genre ADD CONSTRAINT IF NOT EXISTS genre_id_to_genre FOREIGN KEY (genre_id)
    REFERENCES genre ON DELETE CASCADE;