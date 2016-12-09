CREATE TABLE artists_by_movie
(
    movie_id INT NOT NULL,
    artist_id INT NOT NULL,
    PRIMARY KEY(movie_id, artist_id)
);

CREATE INDEX movie_id_id_index ON artists_by_movie (movie_id);
CREATE INDEX artist_id_index ON artists_by_movie (artist_id);

CREATE TABLE artists
(
    artist_id INT PRIMARY KEY NOT NULL,
    stage_name VARCHAR NOT NULL,
    year_of_birth INT NOT NULL
);

CREATE TABLE movies
(
    movie_id INT PRIMARY KEY NOT NULL,
    release_year INT NOT NULL,
    title VARCHAR NOT NULL,
    score DOUBLE NOT NULL,
    synopsis VARCHAR NOT NULL
);

CREATE INDEX year_index ON movies (release_year);
