package ru.akhitev.megaplan.report.calculator.controller;

import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;

public class FileDialog {
    public Path getFileFromDialog(Pane mainPane) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel 2007+ files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = (Stage) mainPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        Path path = file.toPath();
        return path;
    }
}
