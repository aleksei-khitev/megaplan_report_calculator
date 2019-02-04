package ru.akhitev.megaplan.report.calculator.parser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.akhitev.megaplan.report.calculator.vo.CandidateValue;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MegaPlanReportParserSpec {
    private Path correctFilePath;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws URISyntaxException {
        final String testFileName = "/Выведенные кандидаты (5).xlsx";
        URL resource = MegaPlanReportParserSpec.class.getResource(testFileName);
        correctFilePath = Paths.get(resource.toURI());
    }

    @Test
    public void whenFileExistsThenParserConstracted() {
        MegaPlanReportPasrer parser = new MegaPlanReportPasrer(correctFilePath);
    }

    @Test
    public void whenFileNotExistsThenExceptionThrows() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Не могу получить файл для обработки.");
        MegaPlanReportPasrer parser = new MegaPlanReportPasrer(null);
    }

    @Test
    public void whenFileNotXlsxThenExceptionThrows() throws URISyntaxException {
        URL resource = MegaPlanReportParserSpec.class.getResource("/wrongTestFile.txt");
        Path filePath = Paths.get(resource.toURI());
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Не правильный формат. Нужен xlsx.");
        MegaPlanReportPasrer parser = new MegaPlanReportPasrer(filePath);
    }

    @Test
    public void givenParserExistWhenFileHasContentThenParseIt() {
        MegaPlanReportPasrer parser = new MegaPlanReportPasrer(correctFilePath);
        List<CandidateValue> parsedList = parser.parse();
        assertThat(parsedList)
                .as("Must Not Be null list")
                .isNotNull()
                .as("All entries must be parsed")
                .hasSize(146);
    }

    @Test
    public void testBogdan() throws URISyntaxException {
        String fileName = "/special_test_data.xlsx";
        URL resource = MegaPlanReportParserSpec.class.getResource(fileName);
        Path filePath = Paths.get(resource.toURI());
        MegaPlanReportPasrer parser = new MegaPlanReportPasrer(filePath);
        List<CandidateValue> parsedList = parser.parse();
        for(CandidateValue candidate : parsedList) {
            System.out.println(candidate);
        }
        assertThat(parsedList)
                .as("Must Not Be null list")
                .isNotNull()
                .as("All entries must be parsed")
                .hasSize(15);
    }
}
