CREATE TABLE stage (
                       id INT NOT NULL AUTO_INCREMENT, festival_id INT NOT NULL,
                       name VARCHAR(150) NOT NULL,
                       PRIMARY KEY (id),
                       CONSTRAINT fk_stage_festival
                           FOREIGN KEY (festival_id)
                               REFERENCES festival(id),
                       CONSTRAINT uq_stage_festival_name
                           UNIQUE (festival_id, name)
);

CREATE TABLE performance (
                             id INT NOT NULL AUTO_INCREMENT,
                             festival_id INT NOT NULL,
                             artist_id INT NOT NULL,
                             stage_id INT NULL,
                             schedule_status ENUM('TBA', 'SCHEDULED') NOT NULL DEFAULT 'TBA',
                             starts_at DATETIME(6) NULL,
                             ends_at DATETIME(6) NULL,
                             PRIMARY KEY (id),

                             CONSTRAINT fk_performance_festival
                                 FOREIGN KEY (festival_id)
                                     REFERENCES festival(id),

                             CONSTRAINT fk_performance_artist
                                 FOREIGN KEY (artist_id)
                                     REFERENCES artist(id),

                             CONSTRAINT fk_performance_stage
                                 FOREIGN KEY (stage_id)
                                     REFERENCES stage(id),

                             INDEX idx_performance_festival_starts (festival_id, starts_at),
                             INDEX idx_performance_stage_starts (stage_id, starts_at),
                             INDEX idx_performance_artist (artist_id)
);

INSERT INTO performance (
    festival_id,
    artist_id,
    schedule_status
)
SELECT DISTINCT
    festival_id,
    artist_id,
    'TBA'
FROM festival_artist;