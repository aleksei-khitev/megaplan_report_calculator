package ru.akhitev.megaplan.report.calculator.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.akhitev.megaplan.report.calculator.parser.MegaPlanReportPasrer;
import ru.akhitev.megaplan.report.calculator.service.ReportCalculator;
import ru.akhitev.megaplan.report.calculator.vo.CandidateValue;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class MainController {
    @FXML
    private TextArea calculationResult;

    @FXML
    private TableView<Map.Entry<String, Double>> calculatedResultTable;

    @FXML
    private Pane mainPane;

    @FXML
    private DatePicker bottomBorder;

    @FXML
    public void calculate() {
        calculationResult.clear();
        FileDialog dialog = new FileDialog();
        Path filePath = dialog.getFileFromDialog(mainPane);
        calculationResult.appendText("Исходный файл\n");
        calculationResult.appendText(filePath.toString());
        calculationResult.appendText("\n\n");
        prepareTable(getAveragedCandidates(getCandidatesFromFile(filePath)));
    }

    private void prepareTable(Map<String, Double> averagedCandidates) {
        clearTable();
        prepareColumnsAndData(averagedCandidates);
        prepareCopyFeature();
    }

    private void prepareCopyFeature() {
        calculatedResultTable.getSelectionModel().setCellSelectionEnabled(true);
        calculatedResultTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        calculatedResultTable.setOnKeyPressed((keyEvent) -> {
            KeyCodeCombination copyKeyCodeCompination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);
            if (copyKeyCodeCompination.match(keyEvent)) {

                if( keyEvent.getSource() instanceof TableView) {

                    // copy to clipboard
                    copySelectionToClipboard( (TableView<?>) keyEvent.getSource());

                    System.out.println("Selection copied to clipboard");

                    // event is handled, consume it
                    keyEvent.consume();

                }

            }
        });
    }

    private void clearTable() {
        calculatedResultTable.getItems().clear();
        calculatedResultTable.getColumns().clear();
    }

    private void prepareColumnsAndData(Map<String, Double> averagedCandidates) {
        TableColumn<Map.Entry<String, Double>, String> managerColumn = new TableColumn<>("Manager");
        managerColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));

        TableColumn<Map.Entry<String, Double>, String> averageColumn = new TableColumn<>("Average Days");
        averageColumn.setCellValueFactory(p -> new SimpleStringProperty(String.format("%10.2f", p.getValue().getValue())));

        ObservableList<Map.Entry<String, Double>> items = FXCollections.observableArrayList(averagedCandidates.entrySet());
        calculatedResultTable.setItems(items);
        calculatedResultTable.getColumns().add(managerColumn);
        calculatedResultTable.getColumns().add(averageColumn);
    }


    private Map<String, Double> getAveragedCandidates(List<CandidateValue> candidates) {
        ReportCalculator calculator = new ReportCalculator(candidates, bottomBorder.getValue().atStartOfDay());
        return calculator.calculate();
    }

    private List<CandidateValue> getCandidatesFromFile(Path filePath) {
        MegaPlanReportPasrer parser = new MegaPlanReportPasrer(filePath);
        return parser.parse();
    }

    public static void copySelectionToClipboard(TableView<?> table) {

        StringBuilder clipboardString = new StringBuilder();

        ObservableList<TablePosition> positionList = table.getSelectionModel().getSelectedCells();

        int prevRow = -1;

        for (TablePosition position : positionList) {

            int row = position.getRow();
            int col = position.getColumn();

            Object cell = (Object) table.getColumns().get(col).getCellData(row);

            // null-check: provide empty string for nulls
            if (cell == null) {
                cell = "";
            }

            // determine whether we advance in a row (tab) or a column
            // (newline).
            if (prevRow == row) {

                clipboardString.append('\t');

            } else if (prevRow != -1) {

                clipboardString.append('\n');

            }

            // create string from cell
            String text = cell.toString();

            // add new item to clipboard
            clipboardString.append(text);

            // remember previous
            prevRow = row;
        }

        // create clipboard content
        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(clipboardString.toString());

        // set clipboard content
        Clipboard.getSystemClipboard().setContent(clipboardContent);
    }


}
