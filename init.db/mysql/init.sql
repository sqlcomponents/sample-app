DROP TABLE IF EXISTS movie;
DROP TABLE IF EXISTS actor;

CREATE TABLE movie (
    id INT NOT NULL AUTO_INCREMENT,
    title VARCHAR(80),
    directed_by VARCHAR(80),
    year_of_release NUMERIC(4),
    rating NUMERIC(2,1),
    genre VARCHAR(150),
    imdb_id VARCHAR(15),
    UNIQUE(imdb_id),
    PRIMARY KEY ( id )
);

CREATE TABLE actor (
  actor_id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(45) NOT NULL,
  last_name VARCHAR(45) NOT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY  (actor_id),
  KEY idx_actor_last_name (last_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;