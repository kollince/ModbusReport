package com.modbus.reports.service;

import com.modbus.reports.model.automationProtocols.modbus.Protocols;
import com.modbus.reports.model.modbusVariables.Variables;
import javafx.scene.control.TableView;

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
    @Override
    public List<Float> connecting () throws InterruptedException, ExecutionException {
        return protocols.transfer(node, tbVariables);
    }
    public List<Float> getOutValuesProtocol() {
        return protocols.getOutValuesProtocols();
    }
    @Override
    public boolean isConnectProtocol(){
        return protocols.isConnect();
    }
}
