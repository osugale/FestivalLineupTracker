CREATE INDEX idx_artist_genre_genre_artist ON artist_genre (genre_id, artist_id);

CREATE INDEX idx_festival_artist_artist_festival ON festival_artist (artist_id, festival_id);
