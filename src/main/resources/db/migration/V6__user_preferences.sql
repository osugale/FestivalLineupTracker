CREATE TABLE user_genre_preference (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    genre_id INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_user_genre_preference UNIQUE (user_id, genre_id),
    CONSTRAINT fk_user_genre_preference_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_genre_preference_genre
        FOREIGN KEY (genre_id) REFERENCES genre (id)
);

CREATE TABLE user_artist_favorite (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    artist_id INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_user_artist_favorite UNIQUE (user_id, artist_id),
    CONSTRAINT fk_user_artist_favorite_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_artist_favorite_artist
        FOREIGN KEY (artist_id) REFERENCES artist (id)
);
