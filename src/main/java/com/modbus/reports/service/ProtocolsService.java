package com.modbus.reports.service;

import com.modbus.reports.model.modbusVariables.Variables;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ProtocolsService {
    void setTbVariables(TableView<Variables> tbVariables);
    void setNode(String node);
    List<Float> connecting() throws InterruptedException, ExecutionException;
    public List<Float> getOutValuesProtocol();
    boolean isConnectProtocol();
}
