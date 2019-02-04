package ru.akhitev.megaplan.report.calculator.service;

import ru.akhitev.megaplan.report.calculator.vo.CandidateValue;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class ReportCalculator {
    private final List<CandidateValue> baseTestData;
    private final Map<String, Double> averagedData;
    private final LocalDateTime bottomBorder;

    public ReportCalculator(List<CandidateValue> baseTestData, LocalDateTime bottomBorder) {
        this.baseTestData = baseTestData;
        this.bottomBorder = bottomBorder;
        averagedData = new HashMap<>();
    }

    public Map<String, Double> calculate() {
        Map<String, List<CandidateValue>> groupedMap = baseTestData.stream()
                .filter((candidate) -> candidate.groupDate() != null)
                .filter((candidate) -> candidate.groupDate().isEqual(bottomBorder) || candidate.groupDate().isAfter(bottomBorder))
                .collect(Collectors.groupingBy(CandidateValue::manager));
        groupedMap.forEach(this::calculateAverageForManager);
        return averagedData;
    }

    private void calculateAverageForManager(String manager, List<CandidateValue> candidates) {
        //List<Long> daysBetween = candidates.stream().map((candidate) -> ChronoUnit.DAYS.between(candidate.groupDate(), candidate.startDate())).collect(Collectors.toList());
        List<Double> daysBetween = candidates.stream().map(this::daysBetween).collect(Collectors.toList());
        OptionalDouble average = daysBetween.stream().mapToDouble((e) -> e).average();
        average.ifPresent((a) -> averagedData.put(manager, a));
    }

    private Double daysBetween(CandidateValue candidate) {
        long hours = Duration.between(candidate.groupDate(), candidate.startDate().atStartOfDay()).toHours();
        return (double)hours/24;
    }
}
