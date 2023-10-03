package com.modbus.reports.service;

import com.modbus.reports.model.modbusVariables.Variables;
import javafx.scene.control.TableView;
import org.apache.plc4x.java.api.exceptions.PlcConnectionException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ProtocolsService {


    void setTbVariables(TableView<Variables> tbVariables);
    void setNode(String node);
    List<Integer> connecting() throws InterruptedException, PlcConnectionException, ExecutionException;

    boolean isConnectProtocol();
}
