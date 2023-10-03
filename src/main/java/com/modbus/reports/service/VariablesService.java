package com.modbus.reports.service;

import com.modbus.reports.model.modbusVariables.Variables;
import javafx.collections.ObservableList;

public interface VariablesService {
    //получить список modbus переменных
    ObservableList<Variables> getVariables();

    //создать modbus переменную, возвращает id новой переменной
    ObservableList<Variables> createVariables(String name, String address, String description, String alias, String value);
    //удалить modbus переменную по id, вернет удаленного игрока
    ObservableList<Variables> deleteVariables(int id);
    ObservableList<Variables> updateVariables(int id, String name, String address, String description, String alias, String value);
    ObservableList<Variables> updateValue(int id, String value);
}
