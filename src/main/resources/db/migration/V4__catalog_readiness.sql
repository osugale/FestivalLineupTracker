CREATE TABLE genre (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    slug VARCHAR(180) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_genre_name (name),
    UNIQUE KEY uk_genre_slug (slug)
);

CREATE TABLE genre_relation (
    id INT NOT NULL AUTO_INCREMENT,
    parent_genre_id INT NOT NULL,
    child_genre_id INT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_genre_relation_pair (parent_genre_id, child_genre_id),
    CONSTRAINT fk_genre_relation_parent
        FOREIGN KEY (parent_genre_id) REFERENCES genre (id) ON DELETE CASCADE,
    CONSTRAINT fk_genre_relation_child
        FOREIGN KEY (child_genre_id) REFERENCES genre (id) ON DELETE CASCADE,
    CONSTRAINT chk_genre_relation_not_self
        CHECK (parent_genre_id <> child_genre_id)
);

CREATE TABLE artist_genre (
    artist_id INT NOT NULL,
    genre_id INT NOT NULL,
    PRIMARY KEY (artist_id, genre_id),
    CONSTRAINT fk_artist_genre_artist
        FOREIGN KEY (artist_id) REFERENCES artist (id) ON DELETE CASCADE,
    CONSTRAINT fk_artist_genre_genre
        FOREIGN KEY (genre_id) REFERENCES genre (id)
);

CREATE TABLE festival_genre (
    festival_id INT NOT NULL,
    genre_id INT NOT NULL,
    PRIMARY KEY (festival_id, genre_id),
    CONSTRAINT fk_festival_genre_festival
        FOREIGN KEY (festival_id) REFERENCES festival (id) ON DELETE CASCADE,
    CONSTRAINT fk_festival_genre_genre
        FOREIGN KEY (genre_id) REFERENCES genre (id)
);

INSERT INTO genre (name, slug)
SELECT DISTINCT source.genre_name,
                LOWER(REPLACE(REPLACE(REPLACE(TRIM(source.genre_name), '&', 'and'), ' ', '-'), '--', '-'))
FROM (
    SELECT genre AS genre_name FROM artist WHERE genre IS NOT NULL AND TRIM(genre) <> ''
    UNION
    SELECT genre AS genre_name FROM festival WHERE genre IS NOT NULL AND TRIM(genre) <> ''
) source;

INSERT INTO artist_genre (artist_id, genre_id)
SELECT a.id, g.id
FROM artist a
JOIN genre g ON g.name = a.genre
WHERE a.genre IS NOT NULL AND TRIM(a.genre) <> '';

INSERT INTO festival_genre (festival_id, genre_id)
SELECT f.id, g.id
FROM festival f
JOIN genre g ON g.name = f.genre
WHERE f.genre IS NOT NULL AND TRIM(f.genre) <> '';

ALTER TABLE artist
    ADD COLUMN slug VARCHAR(180) NULL;

UPDATE artist
SET slug = LOWER(REPLACE(REPLACE(REPLACE(TRIM(name), '&', 'and'), ' ', '-'), '--', '-'))
WHERE slug IS NULL;

ALTER TABLE artist
    MODIFY COLUMN slug VARCHAR(180) NOT NULL,
    ADD UNIQUE KEY uk_artist_slug (slug),
    DROP COLUMN genre;

ALTER TABLE festival
    ADD COLUMN timezone VARCHAR(80) NULL,
    ADD COLUMN timetable_url VARCHAR(500) NULL;

UPDATE festival
SET timezone = 'UTC'
WHERE timezone IS NULL;

ALTER TABLE festival
    MODIFY COLUMN timezone VARCHAR(80) NOT NULL,
    DROP COLUMN genre,
    ADD UNIQUE KEY uk_festival_name_start_date (name, start_date);

ALTER TABLE performance
    ADD CONSTRAINT chk_scheduled_performance_fields
        CHECK (
            schedule_status = 'TBA'
            OR (
                stage_id IS NOT NULL
                AND starts_at IS NOT NULL
                AND ends_at IS NOT NULL
                AND ends_at > starts_at
            )
        );

CREATE TABLE festival_artist_v4 (
    festival_id INT NOT NULL,
    artist_id INT NOT NULL,
    PRIMARY KEY (festival_id, artist_id),
    CONSTRAINT fk_festival_artist_v4_festival
        FOREIGN KEY (festival_id) REFERENCES festival (id),
    CONSTRAINT fk_festival_artist_v4_artist
        FOREIGN KEY (artist_id) REFERENCES artist (id)
);

INSERT INTO festival_artist_v4 (festival_id, artist_id)
SELECT DISTINCT festival_id, artist_id
FROM festival_artist;

DROP TABLE festival_artist;

RENAME TABLE festival_artist_v4 TO festival_artist;
