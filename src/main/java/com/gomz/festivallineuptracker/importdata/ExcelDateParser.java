package com.gomz.festivallineuptracker.importdata;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

final class ExcelDateParser {

    private static final DateTimeFormatter[] DATE_FORMATS = {
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ofPattern("yyyy/M/d"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("d-M-yyyy")
    };

    private static final DateTimeFormatter[] TIME_FORMATS = {
            DateTimeFormatter.ofPattern("H:mm"),
            DateTimeFormatter.ofPattern("H:mm:ss"),
            DateTimeFormatter.ofPattern("H:mm:ss.SSS")
    };

    private ExcelDateParser() {
    }

    static String readString(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return "";
        }
        CellType type = cell.getCellType() == CellType.FORMULA ? cell.getCachedFormulaResultType() : cell.getCellType();
        return switch (type) {
            case STRING -> trimToEmpty(cell.getStringCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case NUMERIC -> numericAsString(cell);
            case BLANK, _NONE, ERROR -> "";
            default -> trimToEmpty(cell.toString());
        };
    }

    static Optional<LocalDate> parseDate(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return Optional.empty();
        }
        CellType type = cell.getCellType() == CellType.FORMULA ? cell.getCachedFormulaResultType() : cell.getCellType();
        if (type == CellType.NUMERIC) {
            double value = cell.getNumericCellValue();
            if (DateUtil.isCellDateFormatted(cell) || isExcelSerialDate(value)) {
                return Optional.of(DateUtil.getLocalDateTime(value).toLocalDate());
            }
            return Optional.empty();
        }
        if (type == CellType.STRING) {
            return parseDate(cell.getStringCellValue());
        }
        return Optional.empty();
    }

    static Optional<LocalDate> parseDate(String raw) {
        String value = trimToEmpty(raw);
        if (value.isEmpty()) {
            return Optional.empty();
        }
        if (looksLikeSerial(value)) {
            double serial = Double.parseDouble(value);
            if (isExcelSerialDate(serial)) {
                return Optional.of(DateUtil.getLocalDateTime(serial).toLocalDate());
            }
        }
        String normalized = value.replace('.', '-');
        for (DateTimeFormatter formatter : DATE_FORMATS) {
            try {
                return Optional.of(LocalDate.parse(normalized, formatter));
            } catch (DateTimeParseException ignored) {
                // try next pattern
            }
        }
        try {
            return Optional.of(LocalDate.parse(value.replace('/', '-'), DateTimeFormatter.ISO_LOCAL_DATE));
        } catch (DateTimeParseException ignored) {
            return Optional.empty();
        }
    }

    static Optional<LocalTime> parseTime(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return Optional.empty();
        }
        CellType type = cell.getCellType() == CellType.FORMULA ? cell.getCachedFormulaResultType() : cell.getCellType();
        if (type == CellType.NUMERIC) {
            double value = cell.getNumericCellValue();
            if (DateUtil.isCellDateFormatted(cell) || value < 1.0) {
                return Optional.of(DateUtil.getLocalDateTime(value).toLocalTime());
            }
            if (isExcelSerialDate(value)) {
                return Optional.of(DateUtil.getLocalDateTime(value).toLocalTime());
            }
            return Optional.empty();
        }
        if (type == CellType.STRING) {
            return parseTime(cell.getStringCellValue());
        }
        return Optional.empty();
    }

    static Optional<LocalTime> parseTime(String raw) {
        String value = trimToEmpty(raw);
        if (value.isEmpty()) {
            return Optional.empty();
        }
        if (looksLikeSerial(value)) {
            double serial = Double.parseDouble(value);
            if (serial >= 0 && serial < 1.0) {
                return Optional.of(DateUtil.getLocalDateTime(serial).toLocalTime());
            }
        }
        for (DateTimeFormatter formatter : TIME_FORMATS) {
            try {
                return Optional.of(LocalTime.parse(value, formatter));
            } catch (DateTimeParseException ignored) {
                // try next pattern
            }
        }
        return Optional.empty();
    }

    static Optional<LocalDateTime> combine(LocalDate date, LocalTime time) {
        if (date == null || time == null) {
            return Optional.empty();
        }
        return Optional.of(LocalDateTime.of(date, time));
    }

    private static String numericAsString(Cell cell) {
        if (DateUtil.isCellDateFormatted(cell)) {
            return DateUtil.getLocalDateTime(cell.getNumericCellValue()).toLocalDate().toString();
        }
        double value = cell.getNumericCellValue();
        if (value == Math.rint(value) && Math.abs(value) < Long.MAX_VALUE) {
            return Long.toString((long) value);
        }
        return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
    }

    private static boolean isExcelSerialDate(double value) {
        return value >= 20_000 && value < 80_000;
    }

    private static boolean looksLikeSerial(String value) {
        return value.matches("\\d+(\\.\\d+)?");
    }

    private static String trimToEmpty(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }
}
