package com.modbus.reports.model.automationProtocols.modbus;

import com.modbus.reports.model.modbusVariables.Variables;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface Protocols {
    List<Float> transfer(String ip, TableView<Variables> tbVariables) throws InterruptedException, ExecutionException;
    List<Float> getOutValuesProtocols();

    boolean isConnect();
}
