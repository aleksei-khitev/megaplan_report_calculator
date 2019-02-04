package ru.akhitev.megaplan.report.calculator.parser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.akhitev.megaplan.report.calculator.vo.CandidateValue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MegaPlanReportPasrer {
    private final Path filePath;
    private Row tableHeader;
    private List<CandidateValue> candidates;

    public MegaPlanReportPasrer(Path filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("Не могу получить файл для обработки.");
        }
        if (!filePath.toString().endsWith("xlsx")) {
            throw new IllegalArgumentException("Не правильный формат. Нужен xlsx.");
        }
        this.filePath = filePath;
        candidates = new ArrayList<>();
    }

    public List<CandidateValue> parse() {
        readBook();
        return candidates;
    }

    private void readBook() {
        try (final InputStream inputStream = Files.newInputStream(filePath)) {
            final XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            workbook.sheetIterator().forEachRemaining(this::readSheet);
        } catch (IOException e) {

        }
    }

    private void readSheet(Sheet sheet) {
        tableHeader = sheet.getRow(sheet.getFirstRowNum());
        sheet.iterator().forEachRemaining(this::readRow);
    }

    private void readRow(Row row) {
        if (row.equals(tableHeader)) {
            return;
        }
        candidates.add(new CandidateValue(prepareStringValue(row.getCell(1)),
                parseGroupDate(row.getCell(2)),
                parseStartDate(row.getCell(3)),
                prepareStringValue(row.getCell(4))));
    }

    private String prepareStringValue(Cell cell) {
        if (!commonCellValidation(cell)) {
            return null;
        }
        return cell.getStringCellValue();
    }

    private LocalDateTime parseGroupDate(Cell cell) {
        if (!commonCellValidation(cell)) {
            return null;
        }
        Date date = cell.getDateCellValue();
        if (date == null) {
            return null;
        }
        LocalDateTime localDate = Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return localDate;
    }

    private LocalDate parseStartDate(Cell cell) {
        if (!commonCellValidation(cell)) {
            return null;
        }
        String value = cell.getStringCellValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(value, formatter);
        return date;
    }

    private boolean commonCellValidation(Cell cell) {
        if (cell == null) {
            return false;
        }
        if (cell.getCellType().equals(CellType.STRING)) {
            return stringValueValidation(cell.getStringCellValue());
        }
        return true;
    }

    private boolean stringValueValidation(String value) {
        return value != null && !value.isEmpty();
    }

    private boolean numericValueValidation(double value) {
        return value != 0;
    }
}
