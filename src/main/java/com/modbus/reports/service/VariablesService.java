package com.modbus.reports.service;

import com.modbus.reports.model.modbusVariables.Variables;
import javafx.collections.ObservableList;

public interface VariablesService {
    ObservableList<Variables> getVariables();
    ObservableList<Variables> createVariables(String name, String address, String description, String alias, String value);
    ObservableList<Variables> deleteVariables(int id);
    ObservableList<Variables> updateVariables(int id, String name, String address, String description, String alias, String value);
    ObservableList<Variables> updateValue(int id, String value);
}
