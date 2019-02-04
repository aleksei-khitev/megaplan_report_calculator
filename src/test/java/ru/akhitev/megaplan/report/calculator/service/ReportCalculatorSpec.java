package ru.akhitev.megaplan.report.calculator.service;

import org.junit.Before;
import org.junit.Test;
import ru.akhitev.megaplan.report.calculator.vo.CandidateValue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportCalculatorSpec {
    private List<CandidateValue> baseTestData;
    private ReportCalculator calculator;
    private LocalDate bottomBorder;

    @Before
    public void setUp() {
        bottomBorder = prepareDate("2018-11-01");
        baseTestData = new ArrayList<>();
        baseTestData.add(new CandidateValue("Client1", prepareDate("2018-11-01").atStartOfDay(), prepareDate("2018-12-01"), "Manager1"));
        baseTestData.add(new CandidateValue("Client2", prepareDate("2018-11-15").atStartOfDay(), prepareDate("2018-12-02"), "Manager1"));
        baseTestData.add(new CandidateValue("Client3", prepareDate("2018-11-01").atStartOfDay(), prepareDate("2018-12-03"), "Manager2"));
        baseTestData.add(new CandidateValue("Client4", prepareDate("2018-11-01").atStartOfDay(), prepareDate("2018-12-01"), "Manager2"));
        baseTestData.add(new CandidateValue("Client4", prepareDate("2018-12-01").atStartOfDay(), prepareDate("2018-12-05"), "Manager2"));
    }

    @Test
    public void whenCorrectListThenCalculateAverageTime() {
        calculator = new ReportCalculator(baseTestData, bottomBorder.atStartOfDay());
        Map<String, Double> averageTimes = calculator.calculate();
        assertThat(averageTimes)
                .as("Must Not Be null list")
                .isNotNull()
                .as("All candidates must be calculated")
                .hasSize(2)
                .as("Grouping by managers must be correct")
                .containsKeys("Manager1", "Manager2")
                .as("Average values must be calculated correctly")
                .containsValues(22.0, 23.5);
    }

    @Test
    public void whenCountainsGroupDatesAreNullThenIgnoreThem() {
        baseTestData.add(new CandidateValue("Client5", null, prepareDate("2018-12-05"), "Manager2"));
        calculator = new ReportCalculator(baseTestData, bottomBorder.atStartOfDay());
        Map<String, Double> averageTimes = calculator.calculate();
        assertThat(averageTimes)
                .as("Must Not Be null list")
                .isNotNull()
                .as("All candidates must be calculated")
                .hasSize(2)
                .as("Grouping by managers must be correct")
                .containsKeys("Manager1", "Manager2")
                .as("Average values must be calculated correctly")
                .containsValues(22.0, 23.5);
    }

    @Test
    public void whenCountainsGroupDatesAreLowerBottomThenIgnoreThem() {
        baseTestData.add(new CandidateValue("Client5", prepareDate("2018-10-05").atStartOfDay(), prepareDate("2018-12-05"), "Manager2"));
        calculator = new ReportCalculator(baseTestData, bottomBorder.atStartOfDay());
        Map<String, Double> averageTimes = calculator.calculate();
        assertThat(averageTimes)
                .as("Must Not Be null list")
                .isNotNull()
                .as("All candidates must be calculated")
                .hasSize(2)
                .as("Grouping by managers must be correct")
                .containsKeys("Manager1", "Manager2")
                .as("Average values must be calculated correctly")
                .containsValues(22.0, 23.5);
    }

    private LocalDate prepareDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
