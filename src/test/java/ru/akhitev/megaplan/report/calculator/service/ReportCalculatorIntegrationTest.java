package ru.akhitev.megaplan.report.calculator.service;

import org.junit.Before;
import org.junit.Test;
import ru.akhitev.megaplan.report.calculator.parser.MegaPlanReportParserSpec;
import ru.akhitev.megaplan.report.calculator.parser.MegaPlanReportPasrer;
import ru.akhitev.megaplan.report.calculator.vo.CandidateValue;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportCalculatorIntegrationTest {
    private MegaPlanReportPasrer parser;

    @Before
    public void setUp() throws URISyntaxException {
        final String testFileName = "/Выведенные кандидаты (5).xlsx";
        URL resource = MegaPlanReportParserSpec.class.getResource(testFileName);
        final Path correctFilePath = Paths.get(resource.toURI());
        parser = new MegaPlanReportPasrer(correctFilePath);

    }

    @Test
    public void whenFileIsCorrectThenPrepareCorrectReport() {
        List<CandidateValue> parsedValues = parser.parse();
        LocalDate bottomBorder = LocalDate.parse("2018-10-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ReportCalculator calculator = new ReportCalculator(parsedValues, bottomBorder.atStartOfDay());
        Map<String, Double> averageTimes = calculator.calculate();
        assertThat(averageTimes)
                .as("Must Not Be null list")
                .isNotNull()
                .as("All candidates must be calculated")
                .hasSize(6)
                .as("Average for 'Богдан Долгих' must be calculated correctly")
                .containsEntry("Богдан Долгих", 20.3)
                .as("Average for 'Гужова Валерия' must be calculated correctly")
                .containsEntry("Гужова Валерия", 20.3125)
                .as("Average for 'Дидишвили Мира' must be calculated correctly")
                .containsEntry("Дидишвили Мира", 16.845)
                .as("Average for 'Литвиненко Юлия' must be calculated correctly")
                .containsEntry("Литвиненко Юлия", 17.80753968253968)
                .as("Average for 'Осовец Анастасия' must be calculated correctly")
                .containsEntry("Осовец Анастасия", 16.766129032258064)
                .as("Average for 'Пашковец Алена' must be calculated correctly")
                .containsEntry("Пашковец Алена", 15.595238095238095);
    }
}
