CREATE TABLE artist (
                        id INT NOT NULL AUTO_INCREMENT,
                        bio VARCHAR(1000) DEFAULT NULL,
                        country VARCHAR(100) NOT NULL,
                        genre VARCHAR(100) NOT NULL,
                        image_url VARCHAR(500) DEFAULT NULL,
                        instagram_url VARCHAR(500) DEFAULT NULL,
                        name VARCHAR(150) NOT NULL,
                        soundcloud_url VARCHAR(500) DEFAULT NULL,
                        spotify_url VARCHAR(500) DEFAULT NULL,
                        youtube_url VARCHAR(500) DEFAULT NULL,
                        PRIMARY KEY (id)
);

CREATE TABLE festival (
                          id INT NOT NULL AUTO_INCREMENT,
                          city VARCHAR(100) NOT NULL,
                          country VARCHAR(100) NOT NULL,
                          description VARCHAR(1000) DEFAULT NULL,
                          end_date DATE DEFAULT NULL,
                          genre VARCHAR(100) DEFAULT NULL,
                          image_url VARCHAR(500) DEFAULT NULL,
                          name VARCHAR(150) NOT NULL,
                          official_website VARCHAR(500) DEFAULT NULL,
                          start_date DATE DEFAULT NULL,
                          venue VARCHAR(150) NOT NULL,
                          PRIMARY KEY (id)
);

CREATE TABLE users (
                       id INT NOT NULL AUTO_INCREMENT,
                       created_at DATETIME(6) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role ENUM('ADMIN', 'USER') NOT NULL,
                       updated_at DATETIME(6) NOT NULL,
                       username VARCHAR(50) NOT NULL,
                       PRIMARY KEY (id),
                       UNIQUE KEY UK_username (username),
                       UNIQUE KEY UK_email (email)
);

CREATE TABLE festival_artist (
                                 festival_id INT NOT NULL,
                                 artist_id INT NOT NULL,
                                 KEY FK_festival_artist_artist (artist_id),
                                 KEY FK_festival_artist_festival (festival_id),
                                 CONSTRAINT FK_festival_artist_festival
                                     FOREIGN KEY (festival_id) REFERENCES festival (id),
                                 CONSTRAINT FK_festival_artist_artist
                                     FOREIGN KEY (artist_id) REFERENCES artist (id)
);