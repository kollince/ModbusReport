package com.modbus.reports.service;

import com.modbus.reports.model.modbusVariables.Variables;
import javafx.scene.control.TableView;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.nio.file.Path;


public interface ParserService {


    void setTbVariables(TableView<Variables> tbVariables);

    void setPathService(Path pathService);
    boolean read () throws IOException, InvalidFormatException;

    // getTest();
}
