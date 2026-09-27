package com.gomz.festivallineuptracker.importdata;

public record CatalogImportReport(
        int genres,
        int genreRelations,
        int artists,
        int artistGenres,
        int festivals,
        int festivalGenres,
        int lineup,
        int stages,
        int performances
) {
}
