package ru.akhitev.megaplan.report.calculator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import ru.akhitev.megaplan.report.calculator.controller.FileDialog;
import ru.akhitev.megaplan.report.calculator.controller.MainController;
import ru.akhitev.megaplan.report.calculator.parser.MegaPlanReportParserSpec;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LauncherSpec extends ApplicationTest {

    @Override
    public void start (Stage stage) throws Exception {
        final String testFileName = "/Выведенные кандидаты (5).xlsx";
        URL resource = MegaPlanReportParserSpec.class.getResource(testFileName);
        final Path correctFilePath = Paths.get(resource.toURI());
        FileDialog fileDialog = Mockito.mock(FileDialog.class);
        Mockito.when(fileDialog.getFileFromDialog(Mockito.any(Pane.class))).thenReturn(correctFilePath);
        Parent mainNode = FXMLLoader.load(Launcher.class.getResource("/main.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @Before
    public void setUp () throws Exception {
    }

    @After
    public void tearDown () throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void testEnglishInput () {
        clickOn("#bottomBorder");
        write("01.10.2018");
        clickOn("#calculateButton");
        clickOn(".file-path-textfield").write("/home/aleksei_khitev/IdeaProjects/megaplan_report_calculator/src/test/resources/Выведенные кандидаты (5).xlsx");
    }
}
