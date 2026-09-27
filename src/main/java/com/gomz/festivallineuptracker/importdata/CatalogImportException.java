package com.gomz.festivallineuptracker.importdata;

import java.util.List;

public class CatalogImportException extends RuntimeException {

    private final List<String> errors;

    public CatalogImportException(List<String> errors) {
        super("Catalog import validation failed with " + errors.size() + " error(s):"
                + System.lineSeparator() + String.join(System.lineSeparator(), errors));
        this.errors = List.copyOf(errors);
    }

    public List<String> getErrors() {
        return errors;
    }
}
