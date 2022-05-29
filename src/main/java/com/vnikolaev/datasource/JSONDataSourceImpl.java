package com.vnikolaev.datasource;

import com.vnikolaev.abstractions.*;
import com.vnikolaev.datasource.conversions.JSONConverterImpl;
import com.vnikolaev.abstractions.JSONPathInterpreter;
import com.vnikolaev.datasource.pathinterpretors.ModernJSONPathInterpreter;
import com.vnikolaev.datasource.states.*;
import com.vnikolaev.io.FileIO;

import java.nio.file.Path;
import java.util.*;
import java.io.File;

/**
 * The main class for interacting with JSON objects and JSON-type files.
 * It provides common capabilities such as creating, moving, updating and
 * deleting JSON elements, as well as searching functionality. It also offers
 * possibility for saving made changes to a file.
 */
public final class JSONDataSourceImpl implements JSONDataSource {

    private final FileIODevice fileIO;

    private final JSONConverter jsonConverter;

    private final JSONPathInterpreter pathInterpreter;

    private File currentFile;

    private String currentDirectory = "";

    private JSONDataSourceState state;

    private Map<String, Object> jsonMapData;

    private static final String pathDelimiter = File.separator;

    /**
     * Poor man's DI :( ...
     */
    public JSONDataSourceImpl() {
        this(new FileIO(), new JSONConverterImpl(), new ModernJSONPathInterpreter());
    }

    public JSONDataSourceImpl(
            FileIODevice fileIO,
            JSONConverter jsonConverter,
            JSONPathInterpreter pathInterpreter) {

        this.state = new JSONDataSourceClosedFileState(this);

        this.pathInterpreter = pathInterpreter;
        this.fileIO = fileIO;
        this.jsonConverter = jsonConverter;
    }

    public void setCurrentFile(File file) {
        this.currentFile = file;
    }

    public void setCurrentDirectory(String currentDirectory) {
        this.currentDirectory = currentDirectory;
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

    public void setState(JSONDataSourceState state) {
        this.state = state;
    }

    public Map<String, Object> getJsonMapData() {
        return jsonMapData;
    }

    public void setJsonMapData(Map<String, Object> jsonMapData) {
        this.jsonMapData = jsonMapData;
    }

    public JSONPathInterpreter getPathInterpreter() {
        return pathInterpreter;
    }

    @Override
    public DataSourceOperationResult open(String location) {
        DataSourceOperationResult fileOpenResult
                = state.open(currentDirectory + pathDelimiter + location);

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
        return state.saveAs(location);
    }

    @Override
    public DataSourceOperationResult changeDirectory(String location) {
        location = location.replaceAll("%20", " ");

        // User is trying to go one directory up in the file tree.
        if(location.equals("..")) {
            File newDirectory = new File(currentDirectory).getParentFile();

            if(isValidDirectory(newDirectory)) {
                currentDirectory = newDirectory.toString();
                return DataSourceOperationResult
                        .success("Successfully changed directory to "
                                + currentDirectory);
            }
            return DataSourceOperationResult
                    .failure(List.of("Error. A directory with the name "
                            + newDirectory + " doesn't exist."));
        }

        Path newPath = Path.of(currentDirectory, location);

        File relativeDirectory = new File(newPath.toString());
        File absoluteDirectory = new File(location);

        boolean isValidRelativeDir = isValidDirectory(relativeDirectory);
        boolean isValidAbsoluteDir = isValidDirectory(absoluteDirectory);

        if(isValidAbsoluteDir || isValidRelativeDir) {
            currentDirectory = isValidAbsoluteDir
                    ? absoluteDirectory.toString()
                    : relativeDirectory.toString();

            return DataSourceOperationResult
                    .success("Successfully changed directory to "
                            + currentDirectory);
        }

        return DataSourceOperationResult
                .failure(List.of("Error. A directory with the name "
                        + location + " doesn't exist."));
    }

    private boolean isValidDirectory(File relativeDirectory) {
        return relativeDirectory.exists() && relativeDirectory.isDirectory();
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
