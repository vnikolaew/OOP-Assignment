package com.vnikolaev.datasource;

import com.vnikolaev.abstractions.*;
import com.vnikolaev.datasource.states.JSONDataSourceClosedFileState;
import com.vnikolaev.datasource.states.JSONDataSourceState;

import java.util.*;
import java.io.File;

public class JSONDataSourceImpl implements JSONDataSource {

    private final FileIODevice fileIO;

    private final JSONConverter jsonConverter;

    private File currentFile;

    private String baseDirectory;

    private JSONDataSourceState state;

    private Map<String, Object> jsonMapData;

    private static final String pathDelimiter = File.separator;


    public JSONDataSourceImpl(FileIODevice fileIO, JSONConverter jsonConverter) {
        this.state = new JSONDataSourceClosedFileState(this);
        this.fileIO = fileIO;
        this.jsonConverter = jsonConverter;
    }

    public void setCurrentFile(File file) {
        this.currentFile = file;
    }

    public void setBaseDirectory(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public FileIODevice getFileIO() {
        return fileIO;
    }

    public JSONConverter getJsonConverter() {
        return jsonConverter;
    }

    public JSONDataSourceState getState() {
        return state;
    }

    public void setCurrentState(JSONDataSourceState state) {
        this.state = state;
    }

    public Map<String, Object> getJsonMapData() {
        return jsonMapData;
    }

    public void setJsonMapData(Map<String, Object> jsonMapData) {
        this.jsonMapData = jsonMapData;
    }

    @Override
    public DataSourceOperationResult open(String location) {
        DataSourceOperationResult fileOpenResult
                = state.open(baseDirectory + pathDelimiter + location);

        if(fileOpenResult.hasFailed()) {
            return fileOpenResult;
        }

        DataSourceOperationResult result = validateSchema();
        return result.isSuccessful()
                ? fileOpenResult
                : result;
    }

    @Override
    public DataSourceOperationResult close() {
        return state.close();
    }

    @Override
    public DataSourceOperationResult save() {
        return state.save();
    }

    @Override
    public DataSourceOperationResult saveAs(String location) {
        return state.saveAs(baseDirectory + pathDelimiter + location);
    }

    @Override
    public DataSourceOperationResult validateSchema() {
        return state.validateSchema();
    }

    @Override
    public String toFriendlyString() {
        return state.toFriendlyString();
    }

    @Override
    public List<?> searchElement(String key) {
        return state.searchElement(key);
    }

    @Override
    public DataSourceOperationResult setElement(String path, String jsonPayload) {
        return state.setElement(path, jsonPayload);
    }

    @Override
    public DataSourceOperationResult createElement(String path, String jsonPayload) {
        return state.createElement(path, jsonPayload);
    }

    @Override
    public DataSourceOperationResult deleteElement(String path) {
        return state.deleteElement(path);
    }

    @Override
    public DataSourceOperationResult moveElements(String fromPath, String toPath) {
        return state.moveElements(fromPath, toPath);
    }

}
