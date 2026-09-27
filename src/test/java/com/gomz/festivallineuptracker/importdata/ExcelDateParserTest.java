package com.gomz.festivallineuptracker.importdata;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExcelDateParserTest {

    @Test
    void parseDate_acceptsIsoStringSerialAndFormattedCell() throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet();
            Row row = sheet.createRow(0);

            Cell iso = row.createCell(0);
            iso.setCellValue("2026-12-11");
            assertEquals(Optional.of(LocalDate.of(2026, 12, 11)), ExcelDateParser.parseDate(iso));

            Cell slash = row.createCell(1);
            slash.setCellValue("2026/12/13");
            assertEquals(Optional.of(LocalDate.of(2026, 12, 13)), ExcelDateParser.parseDate(slash));

            Cell serial = row.createCell(2);
            serial.setCellValue(46367);
            assertEquals(Optional.of(LocalDate.of(2026, 12, 11)), ExcelDateParser.parseDate(serial));

            Cell serialText = row.createCell(3);
            serialText.setCellValue("46367");
            assertEquals(Optional.of(LocalDate.of(2026, 12, 11)), ExcelDateParser.parseDate(serialText));

            CreationHelper helper = workbook.getCreationHelper();
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(helper.createDataFormat().getFormat("yyyy-mm-dd"));
            Cell formatted = row.createCell(4);
            formatted.setCellValue(LocalDate.of(2026, 2, 20));
            formatted.setCellStyle(dateStyle);
            assertEquals(Optional.of(LocalDate.of(2026, 2, 20)), ExcelDateParser.parseDate(formatted));
        }
    }

    @Test
    void parseTime_acceptsFractionAndClockString() throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet();
            Row row = sheet.createRow(0);

            Cell fraction = row.createCell(0);
            fraction.setCellValue(0.5);
            assertEquals(Optional.of(LocalTime.NOON), ExcelDateParser.parseTime(fraction));

            Cell clock = row.createCell(1);
            clock.setCellValue("21:30");
            assertEquals(Optional.of(LocalTime.of(21, 30)), ExcelDateParser.parseTime(clock));
        }
    }

    @Test
    void combine_buildsLocalDateTimeWithoutInventingTime() {
        assertTrue(ExcelDateParser.combine(LocalDate.of(2026, 12, 11), null).isEmpty());
        assertEquals(
                Optional.of(LocalDate.of(2026, 12, 11).atTime(21, 30)),
                ExcelDateParser.combine(LocalDate.of(2026, 12, 11), LocalTime.of(21, 30))
        );
    }

    @Test
    void readString_keepsIntegerArtistNames() throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Cell cell = workbook.createSheet().createRow(0).createCell(0);
            cell.setCellValue(1991);
            assertEquals("1991", ExcelDateParser.readString(cell));
        }
    }
}
