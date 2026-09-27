package com.gomz.festivallineuptracker.importdata;

import com.gomz.festivallineuptracker.importdata.CatalogWorkbookParser.ArtistRow;
import com.gomz.festivallineuptracker.importdata.CatalogWorkbookParser.CatalogSnapshot;
import com.gomz.festivallineuptracker.importdata.CatalogWorkbookParser.FestivalRow;
import com.gomz.festivallineuptracker.importdata.CatalogWorkbookParser.GenreRow;
import com.gomz.festivallineuptracker.importdata.CatalogWorkbookParser.LineupRow;
import com.gomz.festivallineuptracker.importdata.CatalogWorkbookParser.PerformanceRow;
import com.gomz.festivallineuptracker.importdata.CatalogWorkbookParser.RelationRow;
import com.gomz.festivallineuptracker.importdata.CatalogWorkbookParser.StageRow;
import com.gomz.festivallineuptracker.model.Artist;
import com.gomz.festivallineuptracker.model.Festival;
import com.gomz.festivallineuptracker.model.Genre;
import com.gomz.festivallineuptracker.model.GenreRelation;
import com.gomz.festivallineuptracker.model.Performance;
import com.gomz.festivallineuptracker.model.ScheduleStatus;
import com.gomz.festivallineuptracker.model.Stage;
import com.gomz.festivallineuptracker.repository.ArtistRepository;
import com.gomz.festivallineuptracker.repository.FestivalRepository;
import com.gomz.festivallineuptracker.repository.GenreRelationRepository;
import com.gomz.festivallineuptracker.repository.GenreRepository;
import com.gomz.festivallineuptracker.repository.PerformanceRepository;
import com.gomz.festivallineuptracker.repository.StageRepository;
import com.gomz.festivallineuptracker.util.SlugNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CatalogExcelImportService {

    private static final Logger log = LoggerFactory.getLogger(CatalogExcelImportService.class);

    private final CatalogImportProperties properties;
    private final GenreRepository genreRepository;
    private final GenreRelationRepository genreRelationRepository;
    private final ArtistRepository artistRepository;
    private final FestivalRepository festivalRepository;
    private final StageRepository stageRepository;
    private final PerformanceRepository performanceRepository;
    private final TransactionTemplate transactionTemplate;

    public CatalogExcelImportService(CatalogImportProperties properties, GenreRepository genreRepository, GenreRelationRepository genreRelationRepository, ArtistRepository artistRepository,
                                     FestivalRepository festivalRepository, StageRepository stageRepository, PerformanceRepository performanceRepository,
                                     PlatformTransactionManager transactionManager) {


        this.properties = properties;
        this.genreRepository = genreRepository;
        this.genreRelationRepository = genreRelationRepository;
        this.artistRepository = artistRepository;
        this.festivalRepository = festivalRepository;
        this.stageRepository = stageRepository;
        this.performanceRepository = performanceRepository;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }










    public CatalogImportReport dryRun() {
        return dryRun(resolvePath());
    }

    public CatalogImportReport dryRun(Path file) {
        ValidatedCatalog catalog = readAndValidate(file);
        CatalogImportReport report = catalog.report();
        log.info("Catalog import dry-run passed. No database writes. Expected counts: {}", report);
        return report;
    }







    public CatalogImportReport importCatalog() {
        return importCatalog(resolvePath());
    }

    public CatalogImportReport importCatalog(Path file) {
        ValidatedCatalog catalog = readAndValidate(file);
        log.info("Catalog validation passed. Inserting in one transaction from {}", file.toAbsolutePath());
        CatalogImportReport report = transactionTemplate.execute(status -> persist(catalog));
        log.info("Catalog import complete: {}", report);
        return report;
    }













    private Path resolvePath() {
        if (properties.getFilePath() == null || properties.getFilePath().isBlank()) {
            throw new CatalogImportException(List.of(
                    "config\t-\tfile-path\t\tapp.catalog-import.file-path is required"));
        }
        return Path.of(properties.getFilePath());
    }

    private ValidatedCatalog readAndValidate(Path file) {
        if (!Files.isRegularFile(file)) {
            throw new CatalogImportException(List.of(
                    "workbook\t-\tfile-path\t" + file + "\tfile does not exist"));
        }
        CatalogSnapshot snapshot;
        try {
            snapshot = CatalogWorkbookParser.parse(file);
        } catch (IOException ex) {
            throw new CatalogImportException(List.of(
                    "workbook\t-\tfile\t" + file + "\tcannot read workbook: " + ex.getMessage()));
        }

        List<String> errors = new ArrayList<>(snapshot.parseErrors);
        ValidatedCatalog catalog = validate(snapshot, errors);
        if (!errors.isEmpty()) {
            errors.forEach(error -> log.error("Catalog import validation: {}", error));
            throw new CatalogImportException(errors);
        }
        return catalog;
    }

    private ValidatedCatalog validate(CatalogSnapshot snapshot, List<String> errors) {
        Map<String, GenreRow> genresByName = new LinkedHashMap<>();
        Map<String, String> genreSlugToName = new HashMap<>();
        Map<String, Integer> genreNameRow = new HashMap<>();
        for (GenreRow row : snapshot.genres) {
            String name = row.name().trim();
            if (name.isEmpty()) {
                errors.add(err("genres", row.excelRow(), "name", "", "genre name is required"));
                continue;
            }
            if (name.length() > 150) {
                errors.add(err("genres", row.excelRow(), "name", name, "exceeds 150 characters"));
            }
            String lower = name.toLowerCase();
            if (genreNameRow.containsKey(lower)) {
                errors.add(err("genres", row.excelRow(), "name", name,
                        "duplicate of row " + genreNameRow.get(lower)));
                continue;
            }
            String slug = SlugNormalizer.fromName(name);
            if (slug.isBlank()) {
                errors.add(err("genres", row.excelRow(), "name", name, "does not produce a valid slug"));
                continue;
            }
            if (genreSlugToName.containsKey(slug)) {
                errors.add(err("genres", row.excelRow(), "name", name,
                        "slug '" + slug + "' collides with '" + genreSlugToName.get(slug) + "'"));
                continue;
            }
            genreNameRow.put(lower, row.excelRow());
            genreSlugToName.put(slug, name);
            genresByName.put(name, row);
        }

        Map<String, Integer> relationSeen = new HashMap<>();
        Map<String, List<String>> childrenByParent = new HashMap<>();
        for (RelationRow row : snapshot.relations) {
            String parent = row.parentName().trim();
            String child = row.childName().trim();
            if (parent.isEmpty() || child.isEmpty()) {
                errors.add(err("genre_relations", row.excelRow(), "parent_name/child_name",
                        parent + " -> " + child, "parent and child are required"));
                continue;
            }
            if (!genresByName.containsKey(parent)) {
                errors.add(err("genre_relations", row.excelRow(), "parent_name", parent, "not in genres"));
            }
            if (!genresByName.containsKey(child)) {
                errors.add(err("genre_relations", row.excelRow(), "child_name", child, "not in genres"));
            }
            if (parent.equals(child)) {
                errors.add(err("genre_relations", row.excelRow(), "parent_name", parent, "self relation"));
            }
            String key = parent + "\0" + child;
            if (relationSeen.containsKey(key)) {
                errors.add(err("genre_relations", row.excelRow(), "pair", parent + " -> " + child,
                        "duplicate of row " + relationSeen.get(key)));
            } else {
                relationSeen.put(key, row.excelRow());
                childrenByParent.computeIfAbsent(parent, ignored -> new ArrayList<>()).add(child);
            }
        }
        for (RelationRow row : snapshot.relations) {
            String parent = row.parentName().trim();
            String child = row.childName().trim();
            if (parent.isEmpty() || child.isEmpty() || parent.equals(child)) {
                continue;
            }
            if (hasPath(child, parent, childrenByParent)) {
                errors.add(err("genre_relations", row.excelRow(), "pair", parent + " -> " + child,
                        "would create a cycle"));
            }
        }

        Map<String, ArtistRow> artistsByName = new LinkedHashMap<>();
        Map<String, String> artistSlugToName = new HashMap<>();
        int artistGenrePairs = 0;
        for (ArtistRow row : snapshot.artists) {
            String name = row.name().trim();
            String country = row.country().trim();
            if (name.isEmpty()) {
                errors.add(err("artists", row.excelRow(), "artist_name", "", "artist name is required"));
                continue;
            }
            if (name.length() > 150) {
                errors.add(err("artists", row.excelRow(), "artist_name", name, "exceeds 150 characters"));
            }
            if (country.isEmpty()) {
                errors.add(err("artists", row.excelRow(), "country", "", "country is required"));
            } else if (country.length() > 100) {
                errors.add(err("artists", row.excelRow(), "country", country, "exceeds 100 characters"));
            }
            checkLength(errors, "artists", row.excelRow(), "spotify_url", row.spotifyUrl(), 500);
            checkLength(errors, "artists", row.excelRow(), "instagram_url", row.instagramUrl(), 500);
            checkLength(errors, "artists", row.excelRow(), "soundcloud_url", row.soundcloudUrl(), 500);
            checkLength(errors, "artists", row.excelRow(), "youtube_url", row.youtubeUrl(), 500);
            checkLength(errors, "artists", row.excelRow(), "bio", row.bio(), 1000);
            if (artistsByName.containsKey(name)) {
                errors.add(err("artists", row.excelRow(), "artist_name", name,
                        "duplicate exact name of row " + artistsByName.get(name).excelRow()));
            }
            String slug = SlugNormalizer.fromName(name);
            if (slug.isBlank()) {
                errors.add(err("artists", row.excelRow(), "artist_name", name, "does not produce a valid slug"));
            } else if (artistSlugToName.containsKey(slug)) {
                errors.add(err("artists", row.excelRow(), "artist_name", name,
                        "slug '" + slug + "' collides with '" + artistSlugToName.get(slug) + "'"));
            } else {
                artistSlugToName.put(slug, name);
            }
            Set<String> uniqueGenres = new LinkedHashSet<>();
            for (String genreName : row.genreNames()) {
                if (!genresByName.containsKey(genreName)) {
                    errors.add(err("artists", row.excelRow(), "genre_names", genreName, "not in genres"));
                } else {
                    uniqueGenres.add(genreName);
                }
            }
            artistGenrePairs += uniqueGenres.size();
            artistsByName.putIfAbsent(name, row);
        }

        Map<String, FestivalRow> festivalsByKey = new LinkedHashMap<>();
        int festivalGenrePairs = 0;
        for (FestivalRow row : snapshot.festivals) {
            String name = row.name().trim();
            if (name.isEmpty()) {
                errors.add(err("festivals", row.excelRow(), "festival_name", "", "festival name is required"));
            } else if (name.length() > 150) {
                errors.add(err("festivals", row.excelRow(), "festival_name", name, "exceeds 150 characters"));
            }
            requireMax(errors, "festivals", row.excelRow(), "city", row.city(), 100, true);
            requireMax(errors, "festivals", row.excelRow(), "country", row.country(), 100, true);
            requireMax(errors, "festivals", row.excelRow(), "venue", row.venue(), 150, true);
            checkLength(errors, "festivals", row.excelRow(), "official_website", row.officialWebsite(), 500);
            if (row.startDate() == null) {
                errors.add(err("festivals", row.excelRow(), "start_date", row.startRaw(), "start date is required"));
            }
            if (row.endDate() == null) {
                errors.add(err("festivals", row.excelRow(), "end_date", row.endRaw(), "end date is required"));
            }
            if (row.startDate() != null && row.endDate() != null && row.endDate().isBefore(row.startDate())) {
                errors.add(err("festivals", row.excelRow(), "end_date",
                        row.endDate().toString(), "endDate must be on or after startDate"));
            }
            String timezone = row.timezone().trim();
            if (timezone.isEmpty()) {
                errors.add(err("festivals", row.excelRow(), "timezone", "", "timezone is required"));
            } else if (timezone.length() > 80) {
                errors.add(err("festivals", row.excelRow(), "timezone", timezone, "exceeds 80 characters"));
            } else {
                try {
                    ZoneId.of(timezone);
                } catch (DateTimeException ex) {
                    errors.add(err("festivals", row.excelRow(), "timezone", timezone, "invalid IANA timezone"));
                }
            }
            Set<String> uniqueGenres = new LinkedHashSet<>();
            for (String genreName : row.genreNames()) {
                if (!genresByName.containsKey(genreName)) {
                    errors.add(err("festivals", row.excelRow(), "genre_names", genreName, "not in genres"));
                } else {
                    uniqueGenres.add(genreName);
                }
            }
            festivalGenrePairs += uniqueGenres.size();
            if (name.isEmpty() || row.startDate() == null) {
                continue;
            }
            String key = editionKey(name, row.startDate());
            if (festivalsByKey.containsKey(key)) {
                errors.add(err("festivals", row.excelRow(), "festival_name+start_date", key,
                        "duplicate edition of row " + festivalsByKey.get(key).excelRow()));
            } else {
                festivalsByKey.put(key, row);
            }
        }

        Map<String, Integer> lineupSeen = new HashMap<>();
        Set<String> lineupKeys = new HashSet<>();
        for (LineupRow row : snapshot.lineup) {
            String festivalName = row.festivalName().trim();
            String artistName = row.artistName().trim();
            if (row.startDate() == null) {
                errors.add(err("lineup", row.excelRow(), "start_date", row.startRaw(), "start date is required"));
            }
            String key = row.startDate() == null ? null : editionKey(festivalName, row.startDate());
            if (key != null && !festivalsByKey.containsKey(key)) {
                errors.add(err("lineup", row.excelRow(), "festival_name+start_date",
                        festivalName + " / " + row.startDate(), "festival edition not found"));
            }
            if (!artistsByName.containsKey(artistName)) {
                errors.add(err("lineup", row.excelRow(), "artist_name", artistName, "artist not found"));
            }
            if (key == null || artistName.isEmpty()) {
                continue;
            }
            String pair = key + "\0" + artistName;
            if (lineupSeen.containsKey(pair)) {
                errors.add(err("lineup", row.excelRow(), "artist_name", artistName,
                        "duplicate lineup pair of row " + lineupSeen.get(pair)));
            } else {
                lineupSeen.put(pair, row.excelRow());
                lineupKeys.add(pair);
            }
        }

        Map<String, Integer> stageSeen = new HashMap<>();
        Set<String> stageKeys = new HashSet<>();
        for (StageRow row : snapshot.stages) {
            String festivalName = row.festivalName().trim();
            String stageName = row.stageName().trim();
            if (stageName.isEmpty()) {
                errors.add(err("stages", row.excelRow(), "stage_name", "", "stage name is required"));
            } else if (stageName.length() > 150) {
                errors.add(err("stages", row.excelRow(), "stage_name", stageName, "exceeds 150 characters"));
            }
            if (row.startDate() == null) {
                errors.add(err("stages", row.excelRow(), "start_date", row.startRaw(), "start date is required"));
                continue;
            }
            String festKey = editionKey(festivalName, row.startDate());
            if (!festivalsByKey.containsKey(festKey)) {
                errors.add(err("stages", row.excelRow(), "festival_name+start_date",
                        festivalName + " / " + row.startDate(), "festival edition not found"));
            }
            String stageKey = festKey + "\0" + stageName;
            if (stageSeen.containsKey(stageKey)) {
                errors.add(err("stages", row.excelRow(), "stage_name", stageName,
                        "duplicate stage of row " + stageSeen.get(stageKey)));
            } else {
                stageSeen.put(stageKey, row.excelRow());
                stageKeys.add(stageKey);
            }
        }

        Map<String, Integer> performanceSeen = new HashMap<>();
        List<PerformanceRow> scheduled = new ArrayList<>();
        for (PerformanceRow row : snapshot.performances) {
            String festivalName = row.festivalName().trim();
            String artistName = row.artistName().trim();
            String stageName = row.stageName().trim();
            if (row.startDate() == null) {
                errors.add(err("performances", row.excelRow(), "start_date", row.startRaw(),
                        "festival start date is required"));
                continue;
            }
            String festKey = editionKey(festivalName, row.startDate());
            FestivalRow festival = festivalsByKey.get(festKey);
            if (festival == null) {
                errors.add(err("performances", row.excelRow(), "festival_name+start_date",
                        festivalName + " / " + row.startDate(), "festival edition not found"));
            }
            if (!artistsByName.containsKey(artistName)) {
                errors.add(err("performances", row.excelRow(), "artist_name", artistName, "artist not found"));
            }
            String lineupKey = festKey + "\0" + artistName;
            if (!lineupKeys.contains(lineupKey)) {
                errors.add(err("performances", row.excelRow(), "artist_name", artistName,
                        "artist is not on this festival lineup"));
            }
            if (!stageName.isEmpty()) {
                String stageKey = festKey + "\0" + stageName;
                if (!stageKeys.contains(stageKey)) {
                    errors.add(err("performances", row.excelRow(), "stage_name", stageName,
                            "stage does not belong to this festival edition"));
                }
            }
            if (row.status() == null) {
                errors.add(err("performances", row.excelRow(), "status", row.statusRaw(),
                        "status is required (TBA or SCHEDULED)"));
            }
            LocalDateTime startsAt = ExcelDateParser.combine(row.performanceDate(), row.startTime()).orElse(null);
            LocalDateTime endsAt = ExcelDateParser.combine(row.performanceDate(), row.endTime()).orElse(null);
            if (row.status() == ScheduleStatus.SCHEDULED) {
                if (stageName.isEmpty()) {
                    errors.add(err("performances", row.excelRow(), "stage_name", "",
                            "stage is required when status is SCHEDULED"));
                }
                if (startsAt == null || endsAt == null) {
                    errors.add(err("performances", row.excelRow(), "performance_date/start_time/end_time", "",
                            "date, start_time and end_time are required when status is SCHEDULED"));
                }
                scheduled.add(row);
            }
            if (startsAt != null && endsAt != null) {
                if (!endsAt.isAfter(startsAt)) {
                    errors.add(err("performances", row.excelRow(), "end_time", String.valueOf(row.endTime()),
                            "end must be after start"));
                }
                if (festival != null && row.status() == ScheduleStatus.SCHEDULED) {
                    LocalDateTime festivalStart = festival.startDate().atStartOfDay();
                    LocalDateTime festivalEndExclusive = festival.endDate().plusDays(1).atStartOfDay();
                    if (startsAt.isBefore(festivalStart) || endsAt.isAfter(festivalEndExclusive)) {
                        errors.add(err("performances", row.excelRow(), "performance_date",
                                String.valueOf(row.performanceDate()),
                                "scheduled performance must fall within the festival date range"));
                    }
                }
            }
            if (row.status() == ScheduleStatus.SCHEDULED) {
                String dupKey = festKey + "\0" + artistName + "\0" + stageName + "\0" + startsAt + "\0" + endsAt;
                if (performanceSeen.containsKey(dupKey)) {
                    errors.add(err("performances", row.excelRow(), "record", artistName,
                            "duplicate scheduled performance of row " + performanceSeen.get(dupKey)));
                } else {
                    performanceSeen.put(dupKey, row.excelRow());
                }
            }
        }

        for (int i = 0; i < scheduled.size(); i++) {
            PerformanceRow left = scheduled.get(i);
            LocalDateTime leftStart = ExcelDateParser.combine(left.performanceDate(), left.startTime()).orElse(null);
            LocalDateTime leftEnd = ExcelDateParser.combine(left.performanceDate(), left.endTime()).orElse(null);
            if (leftStart == null || leftEnd == null) {
                continue;
            }
            for (int j = i + 1; j < scheduled.size(); j++) {
                PerformanceRow right = scheduled.get(j);
                LocalDateTime rightStart = ExcelDateParser.combine(right.performanceDate(), right.startTime())
                        .orElse(null);
                LocalDateTime rightEnd = ExcelDateParser.combine(right.performanceDate(), right.endTime()).orElse(null);
                if (rightStart == null || rightEnd == null) {
                    continue;
                }
                if (!leftStart.isBefore(rightEnd) || !rightStart.isBefore(leftEnd)) {
                    continue;
                }
                boolean sameFestival = editionKey(left.festivalName().trim(), left.startDate())
                        .equals(editionKey(right.festivalName().trim(), right.startDate()));
                boolean sameStage = !left.stageName().trim().isEmpty()
                        && left.stageName().trim().equals(right.stageName().trim());
                if (!sameFestival || !sameStage) {
                    continue;
                }
                if (left.artistName().trim().equals(right.artistName().trim())) {
                    errors.add(err("performances", right.excelRow(), "artist_name", right.artistName(),
                            "overlaps artist time of row " + left.excelRow()));
                }
                errors.add(err("performances", right.excelRow(), "stage_name", right.stageName(),
                        "overlaps stage time of row " + left.excelRow()));
            }
        }

        return new ValidatedCatalog(snapshot, genresByName, artistsByName, festivalsByKey, lineupKeys,
                artistGenrePairs, festivalGenrePairs);
    }

    private CatalogImportReport persist(ValidatedCatalog catalog) {
        CatalogSnapshot snapshot = catalog.snapshot;

        Map<String, Genre> genreEntities = new LinkedHashMap<>();
        log.info("Inserting {} genres", snapshot.genres.size());
        for (GenreRow row : snapshot.genres) {
            String name = row.name().trim();
            if (!catalog.genresByName.containsKey(name) || genreEntities.containsKey(name)) {
                continue;
            }
            Genre genre = new Genre();
            genre.setName(name);
            genre.setSlug(SlugNormalizer.fromName(name));
            genreEntities.put(name, genreRepository.save(genre));
        }

        log.info("Inserting {} genre relations", snapshot.relations.size());
        Set<String> savedRelations = new HashSet<>();
        for (RelationRow row : snapshot.relations) {
            String parent = row.parentName().trim();
            String child = row.childName().trim();
            String key = parent + "\0" + child;
            if (!savedRelations.add(key)) {
                continue;
            }
            genreRelationRepository.save(new GenreRelation(genreEntities.get(parent), genreEntities.get(child)));
        }

        Map<String, Artist> artistEntities = new LinkedHashMap<>();
        log.info("Inserting {} artists", snapshot.artists.size());
        for (ArtistRow row : snapshot.artists) {
            String name = row.name().trim();
            if (artistEntities.containsKey(name)) {
                continue;
            }
            Artist artist = new Artist();
            artist.setName(name);
            artist.setSlug(SlugNormalizer.fromName(name));
            artist.setCountry(row.country().trim());
            artist.setSpotifyUrl(emptyToNull(row.spotifyUrl()));
            artist.setInstagramUrl(emptyToNull(row.instagramUrl()));
            artist.setSoundcloudUrl(emptyToNull(row.soundcloudUrl()));
            artist.setYoutubeUrl(emptyToNull(row.youtubeUrl()));
            artist.setBio(emptyToNull(row.bio()));
            artist.setImageUrl(null);
            Set<Genre> genres = new LinkedHashSet<>();
            for (String genreName : row.genreNames()) {
                genres.add(genreEntities.get(genreName));
            }
            artist.setGenres(genres);
            artistEntities.put(name, artistRepository.save(artist));
        }

        Map<String, Festival> festivalEntities = new LinkedHashMap<>();
        log.info("Inserting {} festivals", snapshot.festivals.size());
        for (FestivalRow row : snapshot.festivals) {
            String key = editionKey(row.name().trim(), row.startDate());
            if (festivalEntities.containsKey(key)) {
                continue;
            }
            Festival festival = new Festival();
            festival.setName(row.name().trim());
            festival.setCity(row.city().trim());
            festival.setCountry(row.country().trim());
            festival.setVenue(row.venue().trim());
            festival.setStartDate(row.startDate());
            festival.setEndDate(row.endDate());
            festival.setTimezone(row.timezone().trim());
            festival.setOfficialWebsite(emptyToNull(row.officialWebsite()));
            festival.setDescription(null);
            festival.setImageUrl(null);
            Set<Genre> genres = new LinkedHashSet<>();
            for (String genreName : row.genreNames()) {
                genres.add(genreEntities.get(genreName));
            }
            festival.setGenres(genres);
            festivalEntities.put(key, festivalRepository.save(festival));
        }

        log.info("Inserting {} lineup rows", snapshot.lineup.size());
        Set<String> attachedLineup = new HashSet<>();
        for (LineupRow row : snapshot.lineup) {
            String key = editionKey(row.festivalName().trim(), row.startDate());
            String pair = key + "\0" + row.artistName().trim();
            if (!attachedLineup.add(pair)) {
                continue;
            }
            Festival festival = festivalEntities.get(key);
            festival.getArtists().add(artistEntities.get(row.artistName().trim()));
        }
        for (Festival festival : festivalEntities.values()) {
            if (!festival.getArtists().isEmpty()) {
                festivalRepository.save(festival);
            }
        }

        Map<String, Stage> stageEntities = new LinkedHashMap<>();
        log.info("Inserting {} stages", snapshot.stages.size());
        for (StageRow row : snapshot.stages) {
            String festKey = editionKey(row.festivalName().trim(), row.startDate());
            String stageKey = festKey + "\0" + row.stageName().trim();
            if (stageEntities.containsKey(stageKey)) {
                continue;
            }
            Stage stage = new Stage();
            stage.setFestival(festivalEntities.get(festKey));
            stage.setName(row.stageName().trim());
            stageEntities.put(stageKey, stageRepository.save(stage));
        }

        log.info("Inserting {} performances", snapshot.performances.size());
        for (PerformanceRow row : snapshot.performances) {
            String festKey = editionKey(row.festivalName().trim(), row.startDate());
            Festival festival = festivalEntities.get(festKey);
            Artist artist = artistEntities.get(row.artistName().trim());
            Stage stage = null;
            if (!row.stageName().trim().isEmpty()) {
                stage = stageEntities.get(festKey + "\0" + row.stageName().trim());
            }
            LocalDateTime startsAt = ExcelDateParser.combine(row.performanceDate(), row.startTime()).orElse(null);
            LocalDateTime endsAt = ExcelDateParser.combine(row.performanceDate(), row.endTime()).orElse(null);
            Performance performance = new Performance();
            performance.setFestival(festival);
            performance.setArtist(artist);
            performance.setStage(stage);
            performance.setScheduleStatus(row.status());
            performance.setStartsAt(startsAt);
            performance.setEndsAt(endsAt);
            performanceRepository.save(performance);
        }

        return new CatalogImportReport(
                (int) genreRepository.count(),
                (int) genreRelationRepository.count(),
                (int) artistRepository.count(),
                catalog.artistGenrePairs,
                (int) festivalRepository.count(),
                catalog.festivalGenrePairs,
                catalog.lineupKeys.size(),
                (int) stageRepository.count(),
                (int) performanceRepository.count()
        );
    }

    private static boolean hasPath(String start, String target, Map<String, List<String>> childrenByParent) {
        ArrayDeque<String> toVisit = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        toVisit.add(start);
        while (!toVisit.isEmpty()) {
            String current = toVisit.removeFirst();
            if (!visited.add(current)) {
                continue;
            }
            if (current.equals(target)) {
                return true;
            }
            List<String> children = childrenByParent.get(current);
            if (children != null) {
                toVisit.addAll(children);
            }
        }
        return false;
    }

    private static String editionKey(String festivalName, LocalDate startDate) {
        return festivalName + "\0" + startDate;
    }

    private static void checkLength(List<String> errors, String sheet, int row, String column, String value, int max) {
        if (value != null && value.length() > max) {
            errors.add(err(sheet, row, column, value.substring(0, Math.min(40, value.length())),
                    "exceeds " + max + " characters"));
        }
    }

    private static void requireMax(List<String> errors, String sheet, int row, String column, String value,
                                   int max, boolean required) {
        String trimmed = value == null ? "" : value.trim();
        if (required && trimmed.isEmpty()) {
            errors.add(err(sheet, row, column, "", column + " is required"));
        } else if (trimmed.length() > max) {
            errors.add(err(sheet, row, column, trimmed, "exceeds " + max + " characters"));
        }
    }

    private static String emptyToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value;
    }

    private static String err(String sheet, int row, String column, String value, String message) {
        return sheet + "\t" + row + "\t" + column + "\t" + value + "\t" + message;
    }

    private record ValidatedCatalog(
            CatalogSnapshot snapshot,
            Map<String, GenreRow> genresByName,
            Map<String, ArtistRow> artistsByName,
            Map<String, FestivalRow> festivalsByKey,
            Set<String> lineupKeys,
            int artistGenrePairs,
            int festivalGenrePairs
    ) {
        CatalogImportReport report() {
            return new CatalogImportReport(
                    genresByName.size(),
                    snapshot.relations.size(),
                    artistsByName.size(),
                    artistGenrePairs,
                    festivalsByKey.size(),
                    festivalGenrePairs,
                    lineupKeys.size(),
                    snapshot.stages.size(),
                    snapshot.performances.size()
            );
        }
    }
}
