package com.modbus.reports.service;

import com.modbus.reports.model.modbusVariables.Variables;
import com.modbus.reports.view.Alert;
import com.modbus.reports.model.parsers.docx.ParserProvider;
import javafx.scene.control.TableView;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.nio.file.Path;

public class ParserServiceImpl implements ParserService{
    private Path pathService;
    private TableView<Variables> tbVariables;

    private final ParserProvider parserProvider;
    @Override
    public void setTbVariables(TableView<Variables> tbVariables) {
        this.tbVariables = tbVariables;
    }

    @Override
    public void setPathService(Path pathService) {
        this.pathService = pathService;
    }

    public ParserServiceImpl(ParserProvider parserProvider) {
        this.parserProvider = parserProvider;
    }
    @Override
    public boolean read()
            throws IOException, InvalidFormatException {
        boolean isRead;
        if (pathService == null || !pathService.toFile().exists() || !pathService.toFile().canRead()) {
              Alert.display("Error", "Path must not be null, must exist and must be readable");
              isRead = false;
        } else {
            parserProvider.readDocument(pathService, tbVariables);
            isRead = true;
        }
        return isRead;
    }
}
