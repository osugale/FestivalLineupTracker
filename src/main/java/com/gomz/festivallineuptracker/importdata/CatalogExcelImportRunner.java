package com.gomz.festivallineuptracker.importdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("import")
@Component
public class CatalogExcelImportRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(CatalogExcelImportRunner.class);

    private final CatalogImportProperties properties;
    private final CatalogExcelImportService importService;

    public CatalogExcelImportRunner(CatalogImportProperties properties, CatalogExcelImportService importService) {
        this.properties = properties;
        this.importService = importService;
    }

    @Override
    public void run(ApplicationArguments args) {

        if (!properties.isEnabled()) {
            log.info("Import profile is active but app.catalog-import.enabled=false; skipping catalog import.");
            return;
        }
        if (properties.isDryRun()) {
            log.info("Starting catalog import DRY-RUN from {}", properties.getFilePath());
            CatalogImportReport report = importService.dryRun();
            log.info("DRY-RUN expected counts: genres={} genre_relations={} artists={} artist_genre={} "
                            + "festivals={} festival_genre={} lineup={} stages={} performances={}",
                    report.genres(), report.genreRelations(), report.artists(), report.artistGenres(),
                    report.festivals(), report.festivalGenres(), report.lineup(), report.stages(),
                    report.performances());
            return;
        }


        log.info("Starting catalog import from {}", properties.getFilePath());
        CatalogImportReport report = importService.importCatalog();
        log.info("Imported counts: genres={} genre_relations={} artists={} artist_genre={} "
                        + "festivals={} festival_genre={} lineup={} stages={} performances={}",
                report.genres(), report.genreRelations(), report.artists(), report.artistGenres(),
                report.festivals(), report.festivalGenres(), report.lineup(), report.stages(),
                report.performances());
    }
}
