package com.modbus.reports.service;

import com.modbus.reports.view.Alert;
import com.modbus.reports.model.data.DataProvider;
import com.modbus.reports.model.data.DataProviderJSON;
import com.modbus.reports.model.modbusVariables.Variables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;
import java.util.Collections;

public class VariableServiceImpl implements VariablesService{
    private final DataProvider dataProvider = new DataProviderJSON();

    private final ObservableList<Variables> variablesData = FXCollections.observableArrayList();
    private int counter;
    public VariableServiceImpl() {
        counter = 0;
        initStorages();
    }

    @Override
    public ObservableList<Variables> getVariables() {

        return variablesData;
    }

    @Override
    public ObservableList<Variables> createVariables(String name, String address, String description, String alias, String value) {
        counter++;
        variablesData.add(new Variables(counter,name, address, description, alias, value));
        saveToFile();
        return variablesData;
    }

    @Override
    public ObservableList<Variables> deleteVariables(int id) {
        for (int i = 0; i < variablesData.size(); i++) {
            if(variablesData.get(i).getId()==id){
                variablesData.remove(i);
            }
        }
        saveToFile();
        if (variablesData.size() != 0){
            counter = variablesData.get(variablesData.size()-1).getId();
        } else {
            counter = 0;
        }

        return variablesData;
    }
    @Override
    public ObservableList<Variables> updateVariables(int id, String name, String address, String description, String alias, String value){
        for (int i = 0; i < variablesData.size(); i++) {
            if(variablesData.get(i).getId()==id){
                variablesData.get(i).setName(name);
                variablesData.get(i).setAddress(address);
                variablesData.get(i).setDescription(description);
                variablesData.get(i).setAlias(alias);
                variablesData.get(i).setValue(value);
                variablesData.set(i,variablesData.get(i));
            }
        }
        saveToFile();
        return variablesData;
    }

    @Override
    public ObservableList<Variables> updateValue(int id, String value) {
        for (int i = 0;  i < variablesData.size(); i++){
            if(variablesData.get(i).getId()==id){
                variablesData.get(i).setValue(value);
                variablesData.set(i,variablesData.get(i));
            }
        }
        saveToFile();
        return variablesData;
    }

    public void  initStorages() {
        Collection<Variables> currentList = Collections.EMPTY_LIST;
        try {
            currentList = dataProvider.load();
        } catch (Exception ex){
            Alert.display("Error", "File loading error.");
        }
        int i = 0;
        for (Variables variables : currentList) {
            i=i+1;
            variablesData.add(variables);
            if (variables.getId() > counter){
                counter = variables.getId();
            }
        }
    }
    private void saveToFile() {
        try {
            this.dataProvider.save(variablesData);
        } catch (Exception ex){
            Alert.display("Error", "File saving error.");
        }
    }
}
