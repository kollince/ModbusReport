package com.modbus.reports.model.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modbus.reports.model.modbusVariables.Variables;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

public class DataProviderJSON implements DataProvider{
    private final ObjectMapper mapper = new ObjectMapper();
    final Path FILEPATH = Path.of("src/main/resources/data/data.json");
    @Override
    public void save(ObservableList<Variables> dataListVariables) throws IOException {
        mapper.writeValue(FILEPATH.toFile(), dataListVariables);
        //final Path FILEPATH = Path.of("./data.json");
    }

    @Override
    public Collection<Variables> load() throws IOException {
        return mapper.readValue(FILEPATH.toFile(), new TypeReference<>() {
        });
    }
}
