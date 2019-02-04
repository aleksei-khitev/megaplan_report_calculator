package ru.akhitev.megaplan.report.calculator.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CandidateValue {
    private final String client;
    private final LocalDateTime groupDate;
    private final LocalDate startDate;
    private final String manager;

    public CandidateValue(String client, LocalDateTime groupDate, LocalDate startDate, String manager) {
        this.client = client;
        this.groupDate = groupDate;
        this.startDate = startDate;
        this.manager = manager;
    }

    public String client() {
        return client;
    }

    public LocalDateTime groupDate() {
        return groupDate;
    }

    public LocalDate startDate() {
        return startDate;
    }

    public String manager() {
        return manager;
    }

    @Override
    public String toString() {
        return "CandidateValue{" +
                "client='" + client + '\'' +
                ", groupDate=" + groupDate +
                ", startDate=" + startDate +
                ", manager='" + manager + '\'' +
                '}';
    }
}
