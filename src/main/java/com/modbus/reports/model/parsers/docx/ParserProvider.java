package com.modbus.reports.model.parsers.docx;

import com.modbus.reports.model.modbusVariables.Variables;
import javafx.scene.control.TableView;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.nio.file.Path;

public interface ParserProvider {
    void readDocument(Path path, TableView<Variables> tableView) throws IOException, InvalidFormatException;
}
