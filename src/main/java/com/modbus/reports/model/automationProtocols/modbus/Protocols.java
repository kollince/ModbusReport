package com.modbus.reports.model.automationProtocols.modbus;

import com.modbus.reports.model.modbusVariables.Variables;
import javafx.scene.control.TableView;
import org.apache.plc4x.java.api.exceptions.PlcConnectionException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface Protocols {
    List<Integer> transfer(String ip, TableView<Variables> tbVariables) throws InterruptedException, PlcConnectionException, ExecutionException;

    boolean isConnect();
}
