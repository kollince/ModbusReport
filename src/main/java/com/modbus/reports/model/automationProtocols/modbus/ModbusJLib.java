package com.modbus.reports.model.automationProtocols.modbus;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import com.modbus.reports.model.modbusVariables.Variables;
import javafx.scene.control.TableView;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ModbusJLib implements Protocols {
    @Override
    public List<Integer> transfer(String ip, TableView<Variables> tbVariables) throws InterruptedException {
        if (tbVariables != null) {
            ExecutorService executor = Executors.newCachedThreadPool();
            CountDownLatch latch = new CountDownLatch(1);
            executor.execute(new TasksJLib(tbVariables, ip));
            latch.countDown();
            TimeUnit.MILLISECONDS.sleep(200);
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                executor.shutdown();
            }

        }
        return TasksJLib.getOutValues();
    }
    @Override
    public boolean isConnect() {
        return TasksJLib.isDone;
    }
    private record TasksJLib(TableView<Variables> tbVariables, String node) implements Runnable {
        static private List<Integer> outValues;
        static private List<Integer> getOutValues() {
            return outValues;
        }
        static boolean isDone;
        @Override
        public void run() {
            try {
                String ipAddressRegex = "\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9]" +
                        "[0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";
                TcpParameters tcpParameters = new TcpParameters();
                if (node.matches(ipAddressRegex)) {
                    tcpParameters.setHost(InetAddress.getByName(node));
                    InetAddress inetAddress = InetAddress.getByName(node);
                    if (inetAddress.isReachable(2000)) {
                        isDone = true;
                        tcpParameters.setKeepAlive(true);
                        tcpParameters.setPort(Modbus.TCP_PORT);
                        ModbusMaster m = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
                        Modbus.setAutoIncrementTransactionId(true);
                        int slaveId = 1;
                        try {
                            if (!m.isConnected()) {
                                m.connect();
                            }
                            int defaultValue = Integer.parseInt(tbVariables.getItems().get(0).getAddress());
                            int max = defaultValue;
                            int min = defaultValue;
                            for (int i = 0; i < tbVariables.getItems().size(); i++) {
                                if (Integer.parseInt(tbVariables.getItems().get(i).getAddress()) > max) {
                                    max = Integer.parseInt(tbVariables.getItems().get(i).getAddress());
                                }
                                if (Integer.parseInt(tbVariables.getItems().get(i).getAddress()) < min) {
                                    min = Integer.parseInt(tbVariables.getItems().get(i).getAddress());
                                }
                            }
                            int[] registerValues = m.readHoldingRegisters(slaveId, min, max);
                            int start = 0;
                            int size = max - min + 1;
                            outValues = new ArrayList<>();
                            for (int i = 0; i < size; i++) {
                                start = start + 1;
                                for (int j = 0; j < tbVariables.getItems().size(); j++) {
                                    int idStringTable = tbVariables.getItems().get(j).getId();
                                    int address = Integer.parseInt(tbVariables.getItems().get(j).getAddress());
                                    if (start == idStringTable) {
                                        int reg = address - min;
                                        outValues.add(registerValues[reg]);
                                    }
                                }
                            }
                        } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException e) {
                            isDone = false;
                        } finally {
                            try {
                                m.disconnect();
                            } catch (ModbusIOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        isDone = false;
                    }
                } else {
                    isDone = false;
                }
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

