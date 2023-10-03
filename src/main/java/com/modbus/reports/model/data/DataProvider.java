package com.modbus.reports.model.data;

import com.modbus.reports.model.modbusVariables.Variables;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.Collection;

public interface DataProvider {
    void save(ObservableList<Variables> dataListVariables) throws IOException;
    Collection<Variables> load() throws IOException;
}
