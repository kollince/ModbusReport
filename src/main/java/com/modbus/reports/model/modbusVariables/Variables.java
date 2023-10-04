package com.modbus.reports.model.modbusVariables;

public class Variables {
    private int id;
    private String name;
    private String address;
    private String description;
    private String alias;
    private String value;

    public Variables(int id, String name, String address, String description, String alias, String value) {
        super();
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.alias = alias;
        this.value = value;
    }
    public Variables(){

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
