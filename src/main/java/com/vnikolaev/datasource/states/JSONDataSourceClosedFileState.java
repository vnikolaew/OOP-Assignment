package com.vnikolaev.datasource.states;

import com.vnikolaev.datasource.DataSourceOperationResult;
import com.vnikolaev.datasource.JSONDataSourceImpl;

import java.io.File;
import java.util.List;

public class JSONDataSourceClosedFileState implements JSONDataSourceState {

    private final JSONDataSourceImpl dataSource;

    public JSONDataSourceClosedFileState(JSONDataSourceImpl dataSource) {
        this.dataSource = dataSource;

        dataSource.setCurrentFile(null);
        dataSource.setJsonMapData(null);
    }

    @Override
    public DataSourceOperationResult open(String location) {

        File currentFile = new File(location);
        if(!currentFile.exists()) {
            return DataSourceOperationResult
                    .failure(List.of("File not found."));
        }

        dataSource.setCurrentFile(currentFile);
        dataSource.setCurrentState(new JSONDataSourceOpenedFileState(dataSource));

        return DataSourceOperationResult
                .success("Successfully opened file " + location);
    }

    @Override
    public DataSourceOperationResult close() {
        return DataSourceOperationResult
                .failure(List.of("There's no file opened yet."));
    }

    @Override
    public DataSourceOperationResult save() {
        return DataSourceOperationResult
                .failure(List.of("There's no file opened yet."));
    }

    @Override
    public DataSourceOperationResult saveAs(String location) {
        return DataSourceOperationResult
                .failure(List.of("There's no file opened yet."));
    }

    @Override
    public DataSourceOperationResult setElement(String path, String jsonPayload) {
        return DataSourceOperationResult
                .failure(List.of("There's no file opened yet."));
    }

    @Override
    public DataSourceOperationResult createElement(String path, String jsonPayload) {
        return DataSourceOperationResult
                .failure(List.of("There's no file opened yet."));
    }

    @Override
    public DataSourceOperationResult deleteElement(String path) {
        return DataSourceOperationResult
                .failure(List.of("There's no file opened yet."));
    }

    @Override
    public DataSourceOperationResult moveElements(String fromPath, String toPath) {
        return DataSourceOperationResult
                .failure(List.of("There's no file opened yet."));
    }

    @Override
    public List<?> searchElement(String key) {
        return null;
    }

    @Override
    public DataSourceOperationResult validateSchema() {
        return DataSourceOperationResult
                .failure(List.of("Cannot validate a schema when there's no file opened yet."));
    }

    @Override
    public String toFriendlyString() {
        return null;
    }
}
