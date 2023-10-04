module com.modbus.reports {
    requires java.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.prefs;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires org.apache.commons.io;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.poi.scratchpad;
    requires xdocreport;
    requires java.desktop;
    requires jlibmodbus;
    opens com.modbus.reports to javafx.fxml;
    exports com.modbus.reports;
    exports com.modbus.reports.model.modbusVariables;
    exports com.modbus.reports.model.parsers.docx;
    opens com.modbus.reports.model.parsers.docx to javafx.fxml;
    exports com.modbus.reports.model.controllers;
    opens com.modbus.reports.model.controllers to javafx.fxml;
    exports com.modbus.reports.model.automationProtocols.modbus;
    opens com.modbus.reports.model.automationProtocols.modbus to javafx.fxml;
    exports com.modbus.reports.view;
    opens com.modbus.reports.view to javafx.fxml;
}