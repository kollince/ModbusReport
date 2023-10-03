package com.modbus.reports.service;

import com.modbus.reports.model.automationProtocols.modbus.Protocols;
import com.modbus.reports.model.modbusVariables.Variables;
import javafx.scene.control.TableView;
import org.apache.plc4x.java.api.exceptions.PlcConnectionException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ProtocolsServiceImpl implements ProtocolsService{
    private TableView<Variables> tbVariables;
    private String node;
    private final Protocols protocols;

    public ProtocolsServiceImpl(Protocols protocols) {
        this.protocols = protocols;
    }

    @Override
    public void setTbVariables(TableView<Variables> tbVariables) {
        this.tbVariables = tbVariables;
    }

    @Override
    public void setNode(String node) {
        this.node = node;
    }

    public List<Integer> connecting () throws InterruptedException, PlcConnectionException, ExecutionException {
        return protocols.transfer(node, tbVariables);
    }
    @Override
    public boolean isConnectProtocol(){
        return protocols.isConnect();
    }
}
