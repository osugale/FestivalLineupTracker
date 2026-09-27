package com.gomz.festivallineuptracker.importdata;

import com.gomz.festivallineuptracker.model.ScheduleStatus;
import com.gomz.festivallineuptracker.repository.ArtistRepository;
import com.gomz.festivallineuptracker.repository.FestivalRepository;
import com.gomz.festivallineuptracker.repository.GenreRelationRepository;
import com.gomz.festivallineuptracker.repository.GenreRepository;
import com.gomz.festivallineuptracker.repository.PerformanceRepository;
import com.gomz.festivallineuptracker.repository.StageRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class CatalogExcelImportServiceTest {

    @Autowired
    private CatalogExcelImportService importService;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private GenreRelationRepository genreRelationRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private FestivalRepository festivalRepository;
    @Autowired
    private StageRepository stageRepository;
    @Autowired
    private PerformanceRepository performanceRepository;

    @TempDir
    Path tempDir;

    @Test
    void dryRun_doesNotWrite() throws Exception {
        Path file = writeWorkbook(false);
        CatalogImportReport report = importService.dryRun(file);
        assertEquals(2, report.genres());
        assertEquals(1, report.genreRelations());
        assertEquals(1, report.artists());
        assertEquals(2, report.artistGenres());
        assertEquals(1, report.festivals());
        assertEquals(1, report.festivalGenres());
        assertEquals(1, report.lineup());
        assertEquals(2, report.stages());
        assertEquals(1, report.performances());
        assertEquals(0, genreRepository.count());
        assertEquals(0, artistRepository.count());
        assertEquals(0, festivalRepository.count());
    }

    @Test
    void importCatalog_insertsFullGraph() throws Exception {
        Path file = writeWorkbook(false);
        CatalogImportReport report = importService.importCatalog(file);
        assertEquals(2, report.genres());
        assertEquals(1, genreRelationRepository.count());
        assertEquals(1, artistRepository.count());
        assertEquals(1, festivalRepository.count());
        assertEquals(2, stageRepository.count());
        assertEquals(1, performanceRepository.count());
        assertEquals(ScheduleStatus.TBA, performanceRepository.findAll().getFirst().getScheduleStatus());
        assertEquals("Europe/Amsterdam", festivalRepository.findAll().getFirst().getTimezone());
        assertEquals(1, festivalRepository.findAll().getFirst().getArtists().size());
    }

    @Test
    void importCatalog_rejectsUnknownGenreBeforeWriting() throws Exception {
        Path file = writeWorkbook(true);
        assertThrows(CatalogImportException.class, () -> importService.importCatalog(file));
        assertEquals(0, genreRepository.count());
        assertEquals(0, artistRepository.count());
    }

    @Test
    void dryRun_allowsMultipleTbaPerformancesForSameFestivalAndArtist() throws Exception {
        Path file = writeWorkbook(false,
                performance("Amelie Lens", "", "2026-07-10", "", "", "TBA"),
                performance("Amelie Lens", "", "2026-07-11", "", "", "TBA"));
        CatalogImportReport report = importService.dryRun(file);
        assertEquals(2, report.performances());
        assertEquals(0, performanceRepository.count());
    }

    @Test
    void dryRun_allowsSameArtistDifferentDays() throws Exception {
        Path file = writeWorkbook(false,
                performance("Amelie Lens", "Area Y", "2026-07-10", "22:00", "23:00", "SCHEDULED"),
                performance("Amelie Lens", "Area Y", "2026-07-11", "22:00", "23:00", "SCHEDULED"));
        CatalogImportReport report = importService.dryRun(file);
        assertEquals(2, report.performances());
    }

    @Test
    void dryRun_allowsSameArtistDifferentStagesAndTimes() throws Exception {
        Path file = writeWorkbook(false,
                performance("Amelie Lens", "Area Y", "2026-07-10", "20:00", "21:00", "SCHEDULED"),
                performance("Amelie Lens", "Area X", "2026-07-10", "22:00", "23:00", "SCHEDULED"));
        CatalogImportReport report = importService.dryRun(file);
        assertEquals(2, report.performances());
    }

    @Test
    void dryRun_rejectsExactDuplicateScheduledPerformances() throws Exception {
        Path file = writeWorkbook(false,
                performance("Amelie Lens", "Area Y", "2026-07-10", "22:00", "23:00", "SCHEDULED"),
                performance("Amelie Lens", "Area Y", "2026-07-10", "22:00", "23:00", "SCHEDULED"));
        CatalogImportException ex = assertThrows(CatalogImportException.class, () -> importService.dryRun(file));
        assertTrue(ex.getErrors().stream().anyMatch(error -> error.contains("duplicate scheduled performance")));
        assertEquals(0, performanceRepository.count());
    }

    @Test
    void dryRun_rejectsOverlappingScheduledPerformances() throws Exception {
        Path file = writeWorkbook(false,
                performance("Amelie Lens", "Area Y", "2026-07-10", "22:00", "23:30", "SCHEDULED"),
                performance("Amelie Lens", "Area Y", "2026-07-10", "23:00", "23:45", "SCHEDULED"));
        CatalogImportException ex = assertThrows(CatalogImportException.class, () -> importService.dryRun(file));
        assertTrue(ex.getErrors().stream().anyMatch(error ->
                error.contains("overlaps artist time") || error.contains("overlaps stage time")));
        assertEquals(0, performanceRepository.count());
    }

    private Path writeWorkbook(boolean unknownGenre) throws Exception {
        return writeWorkbook(unknownGenre, performance("Amelie Lens", "", "2026-07-10", "", "", "TBA"));
    }

    private Path writeWorkbook(boolean unknownGenre, String[]... performances) throws Exception {
        Path file = tempDir.resolve("catalog-" + System.nanoTime() + ".xlsx");
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet genres = workbook.createSheet("genres");
            header(genres, "name");
            data(genres, 1, "House");
            data(genres, 2, "Techno");

            Sheet relations = workbook.createSheet("genre_relations");
            header(relations, "parent_name", "child_name");
            data(relations, 1, "House", "Techno");

            Sheet artists = workbook.createSheet("artists");
            header(artists, "artist_name", "country", "genre_names", "spotify_url", "instagram_url",
                    "soundcloud_url", "youtube_url", "bio");
            String genreNames = unknownGenre ? "House;MissingGenre" : "House;Techno";
            data(artists, 1, "Amelie Lens", "Belgium", genreNames, "https://open.spotify.com/artist/x", "",
                    "", "", "Belgian DJ");

            Sheet festivals = workbook.createSheet("festivals");
            header(festivals, "festival_name", "edition", "city", "country", "venue", "start_date", "end_date",
                    "timezone", "genre_names", "official_website", "data_tier", "notes");
            Row festival = festivals.createRow(1);
            festival.createCell(0).setCellValue("Awakenings Festival");
            festival.createCell(1).setCellValue(2026);
            festival.createCell(2).setCellValue("Hilvarenbeek");
            festival.createCell(3).setCellValue("Netherlands");
            festival.createCell(4).setCellValue("Beekse Bergen");
            festival.createCell(5).setCellValue(LocalDate.of(2026, 7, 10).toString());
            festival.createCell(6).setCellValue(46369);
            festival.createCell(7).setCellValue("Europe/Amsterdam");
            festival.createCell(8).setCellValue("Techno");
            festival.createCell(9).setCellValue("https://awakenings.com");
            festival.createCell(10).setCellValue("A");
            festival.createCell(11).setCellValue("ignored");

            Sheet lineup = workbook.createSheet("lineup");
            header(lineup, "festival_name", "start_date", "artist_name");
            Row lineupRow = lineup.createRow(1);
            lineupRow.createCell(0).setCellValue("Awakenings Festival");
            lineupRow.createCell(1).setCellValue(46213);
            lineupRow.createCell(2).setCellValue("Amelie Lens");

            Sheet stages = workbook.createSheet("stages");
            header(stages, "festival_name", "start_date", "stage_name");
            data(stages, 1, "Awakenings Festival", "2026-07-10", "Area Y");
            data(stages, 2, "Awakenings Festival", "2026-07-10", "Area X");

            Sheet performancesSheet = workbook.createSheet("performances");
            header(performancesSheet, "festival_name", "start_date", "artist_name", "stage_name",
                    "performance_date", "start_time", "end_time", "status");
            for (int i = 0; i < performances.length; i++) {
                String[] p = performances[i];
                data(performancesSheet, i + 1, "Awakenings Festival", "2026-07-10", p[0], p[1], p[2], p[3], p[4], p[5]);
            }

            workbook.createSheet("sources");
            try (var out = Files.newOutputStream(file)) {
                workbook.write(out);
            }
        }
        return file;
    }

    private static String[] performance(String artist, String stage, String date, String start, String end,
                                        String status) {
        return new String[] {artist, stage, date, start, end, status};
    }

    private static void header(Sheet sheet, String... names) {
        Row row = sheet.createRow(0);
        for (int i = 0; i < names.length; i++) {
            row.createCell(i).setCellValue(names[i]);
        }
    }

    private static void data(Sheet sheet, int rowIndex, String... values) {
        Row row = sheet.createRow(rowIndex);
        for (int i = 0; i < values.length; i++) {
            row.createCell(i).setCellValue(values[i]);
        }
    }
}
