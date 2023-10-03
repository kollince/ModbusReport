package com.modbus.reports.model.controllers;

import com.modbus.reports.model.automationProtocols.modbus.ModbusJLib;
import com.modbus.reports.model.automationProtocols.modbus.Protocols;
import com.modbus.reports.model.parsers.docx.ParserProvider;
import com.modbus.reports.model.parsers.docx.ParserProviderDOCX;
import com.modbus.reports.service.*;
import com.modbus.reports.view.Alert;

import com.modbus.reports.model.modbusVariables.Variables;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Ellipse;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.apache.plc4x.java.api.exceptions.PlcConnectionException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Controller {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Ellipse ellipseConnect;
    @FXML
    private TextField ipAddress;
    @FXML
    private TextField svAddress;
    @FXML
    private TextField svAlias;
    @FXML
    private TextField svDescription;
    @FXML
    private TextField svName;
    @FXML
    private TableView<Variables> tbVariables;
    @FXML
    private TableColumn<Variables, Integer> tbId;
    @FXML
    private TableColumn<Variables, String> tbName;
    @FXML
    private TableColumn<Variables, Integer> tbAddress;
    @FXML
    private TableColumn<Variables, String> tbDescription;
    @FXML
    private TableColumn<Variables, String> tbAlias;
    @FXML
    private TableColumn<Variables, String> tbValue;
    @FXML
    private Label labelForTemplatesDocx;
    @FXML
    private Label reportSaveSuccessfully;
    @FXML
    private Button checkConnectButton;
    @FXML
    private Button saveReports;
    @FXML
    private Button svSave;
    @FXML
    private Button svUpdate;
    private String svValue;
    private int idForService = 0;
    private Path filePath = new File("src/main/resources").toPath();
    private Path selFile;
    VariablesService variableService = new VariableServiceImpl();
    private final ParserProvider parserProvider = new ParserProviderDOCX();
    private final ParserService parserService = new ParserServiceImpl(parserProvider);
    private final Protocols protocols = new ModbusJLib();
    private final ProtocolsService protocolsService = new ProtocolsServiceImpl(protocols);

    public Controller() {
    }
    @FXML
    protected void onSvSave () {
        variableService.createVariables(svName.getText(),svAddress.getText(),svDescription.getText(),svAlias.getText(), "нет данных");
        showData();
    }
    @FXML
    protected void onSvDelete () {
        variableService.deleteVariables(idForService);
        showData();
    }
    @FXML
    protected void onSvUpdate (){
        Variables variables = tbVariables.getSelectionModel().getSelectedItem();
        variableService.updateVariables(idForService,svName.getText(),svAddress.getText(),svDescription.getText(),svAlias.getText(), variables.getValue());
        showData();
    }
    @FXML
    protected void enteredConnectModbus() {
        protocolsService.setNode(ipAddress.getText());
    }

    @FXML
    protected void onResetConnectModBus(){
        ellipseConnect.setStyle("-fx-fill: #f4f4f4; -fx-stroke: #FFFFFF; ");
    }

    @FXML
    protected void onCheckConnect() {
        protocolsService.setNode(ipAddress.getText());
        checkConnectButton.setDisable(true);
        if (svValue == null) {
            svValue = "0";
        }
        Thread connect = new Thread(() -> {
            protocolsService.setTbVariables(tbVariables);
            try {
                if(protocolsService.connecting()!=null) {
                    for (int i = 0; i < protocolsService.connecting().size(); i++) {
                        svValue = String.valueOf(protocolsService.connecting().get(i));
                        int id = tbVariables.getItems().get(i).getId();
                        variableService.updateValue(id, svValue);
                    }
                    if (protocolsService.isConnectProtocol()) {
                        ellipseConnect.setStyle("-fx-fill: #00CF00; -fx-stroke: #00FF00; ");
                        ellipseConnect.setEffect(new GaussianBlur(3));
                    } else {
                        ellipseConnect.setStyle("-fx-fill: #FF0000; -fx-stroke: #CF0000; ");
                        ellipseConnect.setEffect(new GaussianBlur(3));
                    }
                    checkConnectButton.setDisable(false);
                } else {
                    ellipseConnect.setStyle("-fx-fill: #FF0000; -fx-stroke: #CF0000; ");
                    ellipseConnect.setEffect(new GaussianBlur(3));
                    checkConnectButton.setDisable(false);

                }
            } catch (InterruptedException | PlcConnectionException | ExecutionException e) {
                Alert.display("Ethernet error", String.valueOf(e));
            }

        });
        connect.start();
    }
    @FXML
    private void initialize() {
        protocolsService.setNode(ipAddress.getText());
        Thread connect = new Thread(() -> {
            protocolsService.setTbVariables(tbVariables);
            try {
                protocolsService.connecting();
             } catch (InterruptedException | PlcConnectionException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        connect.start();
        svSave.setDisable(true);
        svUpdate.setDisable(true);
//        svName.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                svSave.setDisable(!newValue.matches(".+"));
//            } else {
//                svSave.setDisable(true);
//            }
//        });
//        svAlias.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                svSave.setDisable(!newValue.matches(".+"));
//            } else {
//                svSave.setDisable(true);
//            }
//        });
//        svAddress.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                svSave.setDisable(!newValue.matches("\\d+"));
//                System.out.println(newValue);
//            } else {
//                svSave.setDisable(true);
//            }
//        });
        System.out.println("Итог: "+addListenerFields().get());
        svSave.setDisable(addListenerFields().get());
        showData();
        saveReports.setDisable(true);
        FadeTransition ft = new FadeTransition(Duration.millis(1000), anchorPane);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setAutoReverse(false);
        ft.play();
        Thread checkConnect = new Thread(() -> {
            anchorPane.setDisable(true);
            try {
                TimeUnit.MILLISECONDS.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            anchorPane.setDisable(false);
        });
        checkConnect.start();
    }
    private AtomicBoolean addListenerFields(){
        AtomicBoolean isEmptyFields = new AtomicBoolean(false);
        svName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                isEmptyFields.set(!newValue.matches(".+"));
                System.out.println("ввод svName: "+isEmptyFields);
            } else {
                isEmptyFields.set(true);
                System.out.println("ввод svName: "+isEmptyFields);
            }
        });
        svAlias.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                isEmptyFields.set(!newValue.matches(".+"));
                System.out.println("ввод svAlias: "+isEmptyFields);
                } else {
                isEmptyFields.set(true);
                System.out.println("ввод svAlias: "+isEmptyFields);
            }
        });
        svAddress.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                isEmptyFields.set(!newValue.matches("\\d+"));
                System.out.println("ввод svAddress: "+isEmptyFields);
            } else {
                isEmptyFields.set(true);
                System.out.println("ввод svAddress: "+isEmptyFields);
            }
        });

        return isEmptyFields;
    }
    private void showData(){
        tbId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tbDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tbAlias.setCellValueFactory(new PropertyValueFactory<>("alias"));
        tbValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        tbVariables.setItems(variableService.getVariables());
    }
    @FXML
    private void handleMouseAction(){
        Variables variables = tbVariables.getSelectionModel().getSelectedItem();
        svName.setText(variables.getName());
        svAddress.setText(variables.getAddress());
        svDescription.setText(variables.getDescription());
        svAlias.setText(variables.getAlias());
        idForService = variables.getId();
    }
    @FXML
    public void onSelectFile () {
        reportSaveSuccessfully.setStyle("-fx-text-fill: Red;");
        reportSaveSuccessfully.setText("Отчет не сохранен");
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("src/main/resources"));
        fc.setTitle("Select file");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.docx","*.docx"));
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile!=null && Files.isRegularFile(Paths.get(selectedFile.getAbsolutePath()))){
            labelForTemplatesDocx.setText(selectedFile.getName());
            labelForTemplatesDocx.setStyle("-fx-text-fill: Green;");
            filePath = Paths.get(selectedFile.getAbsolutePath());
            selFile = Paths.get(selectedFile.getAbsolutePath());
            saveReports.setDisable(false);
        } else {
            Alert.display("File error", "Select *.docx file");
        }
    }
    @FXML void onCreateReport () {
        parserService.setPathService(filePath);
        parserService.setTbVariables(tbVariables);
        anchorPane.setCursor(Cursor.WAIT);
        Thread thread = new Thread(() -> {
            Platform.runLater(() -> {
                try {
                    parserService.read();
                } catch (IOException | InvalidFormatException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (parserService.read()) {
                        anchorPane.setCursor(Cursor.DEFAULT);
                        reportSaveSuccessfully.setStyle("-fx-text-fill: Green;");
                        reportSaveSuccessfully.setText("Отчет сохранен");
                    }
                } catch (IOException | InvalidFormatException e) {
                    throw new RuntimeException(e);
                }
                labelForTemplatesDocx.setStyle("-fx-text-fill: Red;");
                labelForTemplatesDocx.setText("Шаблон не загружен");
                filePath = null;
                saveReports.setDisable(true);
            });
        });
        thread.start();
    }

}