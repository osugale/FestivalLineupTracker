package com.gomz.festivallineuptracker.importdata;

import com.gomz.festivallineuptracker.model.ScheduleStatus;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

final class CatalogWorkbookParser {

    private CatalogWorkbookParser() {
    }

    static CatalogSnapshot parse(Path file) throws IOException {
        try (InputStream in = Files.newInputStream(file); Workbook workbook = WorkbookFactory.create(in)) {
            List<String> errors = new ArrayList<>();
            CatalogSnapshot snapshot = new CatalogSnapshot();
            snapshot.genres.addAll(readGenres(requireSheet(workbook, "genres", errors), errors));
            snapshot.relations.addAll(readRelations(requireSheet(workbook, "genre_relations", errors), errors));
            snapshot.artists.addAll(readArtists(requireSheet(workbook, "artists", errors), errors));
            snapshot.festivals.addAll(readFestivals(requireSheet(workbook, "festivals", errors), errors));
            snapshot.lineup.addAll(readLineup(requireSheet(workbook, "lineup", errors), errors));
            snapshot.stages.addAll(readStages(requireSheet(workbook, "stages", errors), errors));
            snapshot.performances.addAll(readPerformances(requireSheet(workbook, "performances", errors), errors));
            snapshot.parseErrors.addAll(errors);
            return snapshot;
        }
    }

    private static Sheet requireSheet(Workbook workbook, String name, List<String> errors) {
        Sheet sheet = workbook.getSheet(name);
        if (sheet == null) {
            errors.add("workbook\t-\tsheet\t" + name + "\tmissing required sheet");
        }
        return sheet;
    }

    private static List<GenreRow> readGenres(Sheet sheet, List<String> errors) {
        List<GenreRow> rows = new ArrayList<>();
        if (sheet == null) {
            return rows;
        }
        Map<String, Integer> cols = headers(sheet);
        Integer nameCol = requireColumn(cols, "genres", "name", errors);
        if (nameCol == null) {
            return rows;
        }
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (isBlank(row)) {
                continue;
            }
            String name = ExcelDateParser.readString(cell(row, nameCol));
            rows.add(new GenreRow(r + 1, name));
        }
        return rows;
    }

    private static List<RelationRow> readRelations(Sheet sheet, List<String> errors) {
        List<RelationRow> rows = new ArrayList<>();
        if (sheet == null) {
            return rows;
        }
        Map<String, Integer> cols = headers(sheet);
        Integer parentCol = requireColumn(cols, "genre_relations", "parent_name", errors);
        Integer childCol = requireColumn(cols, "genre_relations", "child_name", errors);
        if (parentCol == null || childCol == null) {
            return rows;
        }
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (isBlank(row)) {
                continue;
            }
            rows.add(new RelationRow(r + 1,
                    ExcelDateParser.readString(cell(row, parentCol)),
                    ExcelDateParser.readString(cell(row, childCol))));
        }
        return rows;
    }

    private static List<ArtistRow> readArtists(Sheet sheet, List<String> errors) {
        List<ArtistRow> rows = new ArrayList<>();
        if (sheet == null) {
            return rows;
        }
        Map<String, Integer> cols = headers(sheet);
        Integer nameCol = requireColumn(cols, "artists", "artist_name", errors);
        Integer countryCol = requireColumn(cols, "artists", "country", errors);
        Integer genresCol = requireColumn(cols, "artists", "genre_names", errors);
        if (nameCol == null || countryCol == null || genresCol == null) {
            return rows;
        }
        Integer spotifyCol = cols.get("spotify_url");
        Integer instagramCol = cols.get("instagram_url");
        Integer soundcloudCol = cols.get("soundcloud_url");
        Integer youtubeCol = cols.get("youtube_url");
        Integer bioCol = cols.get("bio");
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (isBlank(row)) {
                continue;
            }
            rows.add(new ArtistRow(
                    r + 1,
                    ExcelDateParser.readString(cell(row, nameCol)),
                    ExcelDateParser.readString(cell(row, countryCol)),
                    splitGenres(ExcelDateParser.readString(cell(row, genresCol))),
                    optionalString(row, spotifyCol),
                    optionalString(row, instagramCol),
                    optionalString(row, soundcloudCol),
                    optionalString(row, youtubeCol),
                    optionalString(row, bioCol)
            ));
        }
        return rows;
    }

    private static List<FestivalRow> readFestivals(Sheet sheet, List<String> errors) {
        List<FestivalRow> rows = new ArrayList<>();
        if (sheet == null) {
            return rows;
        }
        Map<String, Integer> cols = headers(sheet);
        Integer nameCol = requireColumn(cols, "festivals", "festival_name", errors);
        Integer cityCol = requireColumn(cols, "festivals", "city", errors);
        Integer countryCol = requireColumn(cols, "festivals", "country", errors);
        Integer venueCol = requireColumn(cols, "festivals", "venue", errors);
        Integer startCol = requireColumn(cols, "festivals", "start_date", errors);
        Integer endCol = requireColumn(cols, "festivals", "end_date", errors);
        Integer tzCol = requireColumn(cols, "festivals", "timezone", errors);
        Integer genresCol = requireColumn(cols, "festivals", "genre_names", errors);
        if (nameCol == null || cityCol == null || countryCol == null || venueCol == null
                || startCol == null || endCol == null || tzCol == null || genresCol == null) {
            return rows;
        }
        Integer websiteCol = cols.get("official_website");
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (isBlank(row)) {
                continue;
            }
            Cell startCell = cell(row, startCol);
            Cell endCell = cell(row, endCol);
            Optional<LocalDate> startDate = ExcelDateParser.parseDate(startCell);
            Optional<LocalDate> endDate = ExcelDateParser.parseDate(endCell);
            if (cellHasValue(startCell) && startDate.isEmpty()) {
                errors.add(error("festivals", r + 1, "start_date", ExcelDateParser.readString(startCell),
                        "unparseable date"));
            }
            if (cellHasValue(endCell) && endDate.isEmpty()) {
                errors.add(error("festivals", r + 1, "end_date", ExcelDateParser.readString(endCell),
                        "unparseable date"));
            }
            rows.add(new FestivalRow(
                    r + 1,
                    ExcelDateParser.readString(cell(row, nameCol)),
                    ExcelDateParser.readString(cell(row, cityCol)),
                    ExcelDateParser.readString(cell(row, countryCol)),
                    ExcelDateParser.readString(cell(row, venueCol)),
                    startDate.orElse(null),
                    endDate.orElse(null),
                    ExcelDateParser.readString(startCell),
                    ExcelDateParser.readString(endCell),
                    ExcelDateParser.readString(cell(row, tzCol)),
                    splitGenres(ExcelDateParser.readString(cell(row, genresCol))),
                    optionalString(row, websiteCol)
            ));
        }
        return rows;
    }

    private static List<LineupRow> readLineup(Sheet sheet, List<String> errors) {
        List<LineupRow> rows = new ArrayList<>();
        if (sheet == null) {
            return rows;
        }
        Map<String, Integer> cols = headers(sheet);
        Integer nameCol = requireColumn(cols, "lineup", "festival_name", errors);
        Integer startCol = requireColumn(cols, "lineup", "start_date", errors);
        Integer artistCol = requireColumn(cols, "lineup", "artist_name", errors);
        if (nameCol == null || startCol == null || artistCol == null) {
            return rows;
        }
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (isBlank(row)) {
                continue;
            }
            Cell startCell = cell(row, startCol);
            Optional<LocalDate> startDate = ExcelDateParser.parseDate(startCell);
            if (cellHasValue(startCell) && startDate.isEmpty()) {
                errors.add(error("lineup", r + 1, "start_date", ExcelDateParser.readString(startCell),
                        "unparseable date"));
            }
            rows.add(new LineupRow(
                    r + 1,
                    ExcelDateParser.readString(cell(row, nameCol)),
                    startDate.orElse(null),
                    ExcelDateParser.readString(startCell),
                    ExcelDateParser.readString(cell(row, artistCol))
            ));
        }
        return rows;
    }

    private static List<StageRow> readStages(Sheet sheet, List<String> errors) {
        List<StageRow> rows = new ArrayList<>();
        if (sheet == null) {
            return rows;
        }
        Map<String, Integer> cols = headers(sheet);
        Integer nameCol = requireColumn(cols, "stages", "festival_name", errors);
        Integer startCol = requireColumn(cols, "stages", "start_date", errors);
        Integer stageCol = requireColumn(cols, "stages", "stage_name", errors);
        if (nameCol == null || startCol == null || stageCol == null) {
            return rows;
        }
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (isBlank(row)) {
                continue;
            }
            Cell startCell = cell(row, startCol);
            Optional<LocalDate> startDate = ExcelDateParser.parseDate(startCell);
            if (cellHasValue(startCell) && startDate.isEmpty()) {
                errors.add(error("stages", r + 1, "start_date", ExcelDateParser.readString(startCell),
                        "unparseable date"));
            }
            rows.add(new StageRow(
                    r + 1,
                    ExcelDateParser.readString(cell(row, nameCol)),
                    startDate.orElse(null),
                    ExcelDateParser.readString(startCell),
                    ExcelDateParser.readString(cell(row, stageCol))
            ));
        }
        return rows;
    }

    private static List<PerformanceRow> readPerformances(Sheet sheet, List<String> errors) {
        List<PerformanceRow> rows = new ArrayList<>();
        if (sheet == null) {
            return rows;
        }
        Map<String, Integer> cols = headers(sheet);
        Integer festCol = requireColumn(cols, "performances", "festival_name", errors);
        Integer startCol = requireColumn(cols, "performances", "start_date", errors);
        Integer artistCol = requireColumn(cols, "performances", "artist_name", errors);
        Integer stageCol = requireColumn(cols, "performances", "stage_name", errors);
        Integer dateCol = requireColumn(cols, "performances", "performance_date", errors);
        Integer startTimeCol = requireColumn(cols, "performances", "start_time", errors);
        Integer endTimeCol = requireColumn(cols, "performances", "end_time", errors);
        Integer statusCol = requireColumn(cols, "performances", "status", errors);
        if (festCol == null || startCol == null || artistCol == null || stageCol == null
                || dateCol == null || startTimeCol == null || endTimeCol == null || statusCol == null) {
            return rows;
        }
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (isBlank(row)) {
                continue;
            }
            Cell editionStartCell = cell(row, startCol);
            Cell performanceDateCell = cell(row, dateCol);
            Cell startTimeCell = cell(row, startTimeCol);
            Cell endTimeCell = cell(row, endTimeCol);
            Optional<LocalDate> editionStart = ExcelDateParser.parseDate(editionStartCell);
            Optional<LocalDate> performanceDate = ExcelDateParser.parseDate(performanceDateCell);
            Optional<LocalTime> startTime = ExcelDateParser.parseTime(startTimeCell);
            Optional<LocalTime> endTime = ExcelDateParser.parseTime(endTimeCell);
            if (cellHasValue(editionStartCell) && editionStart.isEmpty()) {
                errors.add(error("performances", r + 1, "start_date", ExcelDateParser.readString(editionStartCell),
                        "unparseable date"));
            }
            if (cellHasValue(performanceDateCell) && performanceDate.isEmpty()) {
                errors.add(error("performances", r + 1, "performance_date",
                        ExcelDateParser.readString(performanceDateCell), "unparseable date"));
            }
            if (cellHasValue(startTimeCell) && startTime.isEmpty()) {
                errors.add(error("performances", r + 1, "start_time", ExcelDateParser.readString(startTimeCell),
                        "unparseable time"));
            }
            if (cellHasValue(endTimeCell) && endTime.isEmpty()) {
                errors.add(error("performances", r + 1, "end_time", ExcelDateParser.readString(endTimeCell),
                        "unparseable time"));
            }
            String statusRaw = ExcelDateParser.readString(cell(row, statusCol));
            ScheduleStatus status = null;
            if (!statusRaw.isEmpty()) {
                try {
                    status = ScheduleStatus.valueOf(statusRaw.trim().toUpperCase());
                } catch (IllegalArgumentException ex) {
                    errors.add(error("performances", r + 1, "status", statusRaw, "must be TBA or SCHEDULED"));
                }
            }
            rows.add(new PerformanceRow(
                    r + 1,
                    ExcelDateParser.readString(cell(row, festCol)),
                    editionStart.orElse(null),
                    ExcelDateParser.readString(editionStartCell),
                    ExcelDateParser.readString(cell(row, artistCol)),
                    ExcelDateParser.readString(cell(row, stageCol)),
                    performanceDate.orElse(null),
                    startTime.orElse(null),
                    endTime.orElse(null),
                    status,
                    statusRaw
            ));
        }
        return rows;
    }

    private static Map<String, Integer> headers(Sheet sheet) {
        Map<String, Integer> cols = new LinkedHashMap<>();
        Row header = sheet.getRow(0);
        if (header == null) {
            return cols;
        }
        short last = header.getLastCellNum();
        for (int i = 0; i < last; i++) {
            String name = ExcelDateParser.readString(header.getCell(i));
            if (!name.isEmpty()) {
                cols.put(name, i);
            }
        }
        return cols;
    }

    private static Integer requireColumn(Map<String, Integer> cols, String sheet, String name, List<String> errors) {
        Integer index = cols.get(name);
        if (index == null) {
            errors.add(sheet + "\t1\t" + name + "\t\tmissing required column");
        }
        return index;
    }

    static List<String> splitGenres(String raw) {
        List<String> genres = new ArrayList<>();
        if (raw == null || raw.isBlank()) {
            return genres;
        }
        for (String part : raw.split(";")) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                genres.add(trimmed);
            }
        }
        return genres;
    }

    private static String optionalString(Row row, Integer col) {
        if (col == null) {
            return null;
        }
        String value = ExcelDateParser.readString(cell(row, col));
        return value.isEmpty() ? null : value;
    }

    private static Cell cell(Row row, int col) {
        return row == null ? null : row.getCell(col);
    }

    private static boolean isBlank(Row row) {
        if (row == null) {
            return true;
        }
        short last = row.getLastCellNum();
        if (last < 0) {
            return true;
        }
        for (int i = 0; i < last; i++) {
            if (!ExcelDateParser.readString(row.getCell(i)).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static boolean cellHasValue(Cell cell) {
        return !ExcelDateParser.readString(cell).isEmpty();
    }

    private static String error(String sheet, int row, String column, String value, String message) {
        return sheet + "\t" + row + "\t" + column + "\t" + value + "\t" + message;
    }

    static final class CatalogSnapshot {
        final List<String> parseErrors = new ArrayList<>();
        final List<GenreRow> genres = new ArrayList<>();
        final List<RelationRow> relations = new ArrayList<>();
        final List<ArtistRow> artists = new ArrayList<>();
        final List<FestivalRow> festivals = new ArrayList<>();
        final List<LineupRow> lineup = new ArrayList<>();
        final List<StageRow> stages = new ArrayList<>();
        final List<PerformanceRow> performances = new ArrayList<>();
    }

    record GenreRow(int excelRow, String name) {
    }

    record RelationRow(int excelRow, String parentName, String childName) {
    }

    record ArtistRow(int excelRow, String name, String country, List<String> genreNames,
                     String spotifyUrl, String instagramUrl, String soundcloudUrl, String youtubeUrl, String bio) {
    }

    record FestivalRow(int excelRow, String name, String city, String country, String venue,
                       LocalDate startDate, LocalDate endDate, String startRaw, String endRaw,
                       String timezone, List<String> genreNames, String officialWebsite) {
    }

    record LineupRow(int excelRow, String festivalName, LocalDate startDate, String startRaw, String artistName) {
    }

    record StageRow(int excelRow, String festivalName, LocalDate startDate, String startRaw, String stageName) {
    }

    record PerformanceRow(int excelRow, String festivalName, LocalDate startDate, String startRaw,
                          String artistName, String stageName, LocalDate performanceDate,
                          LocalTime startTime, LocalTime endTime, ScheduleStatus status, String statusRaw) {
    }
}
