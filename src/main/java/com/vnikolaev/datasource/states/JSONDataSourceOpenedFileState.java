package com.vnikolaev.datasource.states;

import com.vnikolaev.abstractions.*;
import com.vnikolaev.datasource.*;
import com.vnikolaev.datasource.conversions.JSONConversionResult;
import com.vnikolaev.abstractions.JSONPathInterpreter;

import java.io.IOException;
import java.util.*;

/**
 * A state class representing a file that's currently in an opened state
 * and the JSON schema is validated. This is the class where most of the work
 * and logic related to interacting with the JSON data resides.
 */
public class JSONDataSourceOpenedFileState implements JSONDataSourceState {

    private Map<String, Object> jsonMapData;

    private final JSONDataSourceImpl dataSource;

    private final JSONPathInterpreter pathInterpreter;

    private static final char spaceChar = ' ';

    public JSONDataSourceOpenedFileState(JSONDataSourceImpl dataSource) {
        this.dataSource = dataSource;
        this.jsonMapData = dataSource.getJsonMapData();
        this.pathInterpreter = dataSource.getPathInterpreter();
    }

    @Override
    public DataSourceOperationResult open(String location) {
        return DataSourceOperationResult.failure(
                List.of("There's an already opened file in use."));
    }

    /**
     * Closes the currently opened file (if any) and changes the current
     * file state to 'closed'.
     */
    @Override
    public DataSourceOperationResult close() {
        String currentFile = dataSource.getCurrentFile().getPath();

        dataSource
                .setState(new JSONDataSourceClosedFileState(dataSource));

        return DataSourceOperationResult
                .success("Successfully closed " + currentFile);
    }

    /**
     * Saves all the changes made to the currently opened file
     * but without closing it.
     */
    @Override
    public DataSourceOperationResult save() {
        String filePath = dataSource.getCurrentFile().getPath();
        return saveAs(filePath);
    }

    @Override
    public DataSourceOperationResult saveAs(String location) {

        JSONConversionResult<String> result =
                dataSource.getJsonConverter().mapToString(jsonMapData);

        if(!result.isSuccessful()) {
            return DataSourceOperationResult
                    .failure(List.of("Error: " + result.error()));
        }

        try {
            String jsonString = result.data();

            location = location.replaceAll("%20", String.valueOf(spaceChar));

            dataSource.getFileIO().write(location, jsonString);
            return DataSourceOperationResult
                    .success("Successfully saved " + location);
        } catch (IOException e) {
            return DataSourceOperationResult
                    .failure(List.of("Error trying to open a file: " + e.getMessage()));
        }
    }

    @Override
    public DataSourceOperationResult changeDirectory(String location) {
        return null;
    }

    /**
     * Updates an element in the JSON object given the new JSON payload.
     * Note that if the payload provided has an invalid JSON schema, then
     * the operation fails.
     * @param path The JSON path to the element being updated.
     * @param jsonPayload The new data the elements it going be updated with.
     */
    @Override
    public DataSourceOperationResult setElement(String path, String jsonPayload) {
        String[] segments = pathInterpreter.getSegments(path);

        if(!elementExists(segments)) {
            return DataSourceOperationResult
                    .failure(List.of("Element with that key doesn't exist."));
        }

        JSONConversionResult<Object> conversionResult = dataSource.getJsonConverter().stringToObject(jsonPayload);
        if(!conversionResult.isSuccessful()) {
            return DataSourceOperationResult
                    .failure(List.of("Invalid JSON schema."));
        }

        Object current = traverseJsonObject
                (Arrays.copyOf(segments, segments.length - 1), jsonMapData);
        String lastSegment = segments[segments.length - 1];


        Object jsonObject = conversionResult.data();
        if(isAList(current)) {
            List<Object> list = (List<Object>) current;

            Integer index = tryParseInt(lastSegment);
            if(index == null || isOutOfBounds(index, list)) {
                return DataSourceOperationResult
                        .failure(List.of("Invalid path"));
            }

            list.set(index, jsonObject);

            return DataSourceOperationResult
                    .success("Successfully updated a value.");
        }
        if(isAMap(current)) {
            Map<String, Object> map = (Map<String, Object>) current;

            map.put(lastSegment, jsonObject);
            return DataSourceOperationResult
                    .success("Successfully updated a value.");
        }

        return DataSourceOperationResult
                .failure(List.of("Couldn't update the specified element."));
    }

    /**
     * @param path The path to the new element. Note that if it does not exist, then
     * the algorithm will make sure it is created with blank objects.
     * @param jsonPayload The payload for the newly created element.
     * @return
     */
    @Override
    public DataSourceOperationResult createElement(String path, String jsonPayload) {
        String[] segments = pathInterpreter.getSegments(path);

        if (elementExists(segments)) {
            return DataSourceOperationResult
                    .failure(List.of("Element with that key already exists."));
        }

        JSONConversionResult<Object> conversionResult = dataSource.getJsonConverter().stringToObject(jsonPayload);
        if(!conversionResult.isSuccessful()) {
            return DataSourceOperationResult
                    .failure(List.of("Invalid JSON schema."));
        }

        Object current = traverseJsonObjectAndCreateElementsIfAbsent
                (Arrays.copyOf(segments, segments.length - 1), jsonMapData);
        String lastSegment = segments[segments.length - 1];


        Object jsonObject = conversionResult.data();
        if(isAList(current)) {
            List<Object> list = (List<Object>) current;

            Integer index = tryParseInt(lastSegment);
            if(index == null || index != list.size()) {
                return DataSourceOperationResult.failure(List.of("Invalid path"));
            }

            list.add(jsonObject);
            return DataSourceOperationResult
                    .success("Successfully added a value.");
        }
        if(isAMap(current)) {
            Map<String, Object> map = (Map<String, Object>) current;

            map.put(lastSegment, jsonObject);
            return DataSourceOperationResult
                    .success("Successfully added a value.");
        }

        return DataSourceOperationResult.failure(List.of("Invalid path."));
    }

    /**
     * Deletes the element at the given path (if it exists).
     * @param path The path for the element to be deleted.
     */
    @Override
    public DataSourceOperationResult deleteElement(String path) {
        String[] segments = pathInterpreter.getSegments(path);

        if (!elementExists(segments)) {
            return DataSourceOperationResult
                    .failure(List.of("Element with that key doesn't exist."));
        }

        Object current = traverseJsonObject
                (Arrays.copyOf(segments, segments.length - 1), jsonMapData);
        String lastSegment = segments[segments.length - 1];

        if(isAList(current)) {
            List<Object> list = (List<Object>) current;

            Integer index = tryParseInt(lastSegment);
            if(index == null || isOutOfBounds(index, list)) {
                return DataSourceOperationResult.failure(List.of("Index is out of bounds."));
            }

            list.remove((int) index);
            return DataSourceOperationResult
                    .success("Element successfully deleted.");
        } else if(isAMap(current)) {
            Map<String, Object> map = (Map<String, Object>) current;
            map.remove(lastSegment);

            return DataSourceOperationResult
                    .success("Element successfully deleted.");
        }

        return DataSourceOperationResult
                .failure(List.of("Could not delete the specified element."));
    }

    /**
     * Moves element/s at the given source path to the specified destination path.
     * Note that if the destination does not exist, then the algorithm will make
     * sure they get created with blank objects.
     * @param fromPath The source path from which element/s are going to be moved.
     * @param toPath The destination path to which element/s are going to be moved.
     */
    @Override
    public DataSourceOperationResult moveElements(String fromPath, String toPath) {
        String[] fromSegments = pathInterpreter.getSegments(fromPath);
        String[] toSegments = pathInterpreter.getSegments(toPath);

        if(!elementExists(fromSegments)) {
            return DataSourceOperationResult
                    .failure(List.of("Specified source element(s) do / does not exist."));
        }

        deleteElement(fromPath);

        Object toPathParentElement = traverseJsonObjectAndCreateElementsIfAbsent
                (Arrays.copyOf(toSegments, toSegments.length - 1), jsonMapData);
        String lastToPathSegment = toSegments[toSegments.length - 1];

        Object sourceElement = traverseJsonObject(fromSegments, jsonMapData);
        if(isAMap(toPathParentElement)) {
            Map<String, Object> map = (Map<String, Object>) toPathParentElement;
            map.put(lastToPathSegment, sourceElement);

            return DataSourceOperationResult
                    .success("Successfully moved elements.");
        }
        if(isAList(toPathParentElement)) {
            List<Object> list = (List<Object>) toPathParentElement;
            list.add(sourceElement);

            return DataSourceOperationResult
                    .success("Successfully moved elements.");
        }

        return DataSourceOperationResult
                .failure(List.of("Could not move the specified elements."));
    }

    /**
     * Searches for elements at the specified path. Note that it returns
     * a list of objects / elements instead of a single object result.
     * @param key The path at which to search elements.
     * @return
     */
    @Override
    public List<?> searchElement(String key) {
        String[] segments = pathInterpreter.getSegments(key);

        Object currentJsonElement = traverseJsonObject(segments, jsonMapData);

        return convertObjectToList(currentJsonElement);
    }

    /**
     * Validates the JSON schema of the object in the currently opened file.
     * Note that if the schema is found invalid, then the file will immediately be
     * closed, since we can't work with an invalid JSON object.
     */
    @Override
    public DataSourceOperationResult validateSchema() {
        try {
            FileIODevice ioDevice = dataSource.getFileIO();
            JSONConverter converter = dataSource.getJsonConverter();

            String json = ioDevice.read(dataSource.getCurrentFile().getPath());

            JSONConversionResult<Map<String, Object>> result =
                    converter.stringToMap(json);

            if(!result.isSuccessful()) {
                dataSource
                        .setState(new JSONDataSourceClosedFileState(dataSource));

                return DataSourceOperationResult.failure(List.of(
                        "Invalid JSON schema.", result.error()));
            }

            jsonMapData = result.data();
            dataSource.setJsonMapData(jsonMapData);

            return DataSourceOperationResult.success("JSON Schema is valid.");
        } catch (IOException e) {
            dataSource.setState(new JSONDataSourceClosedFileState(dataSource));

            return DataSourceOperationResult.failure(List.of(
                    "Error trying to open a file: ", e.getMessage()));
        }
    }

    /**
     * Returns a friendly indented string representation of the JSON object.
     */
    @Override
    public String toFriendlyString() {
        return dataSource
                .getJsonConverter()
                .mapToString(jsonMapData)
                .data();
    }

    private Integer tryParseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * A core method for traversing / searching a map structure.
     * @param segments A sequence of keys to search for in the traversal.
     * @param jsonMap A map structure that will be traversed. Note that it's
     *                keys have to be of type String.
     */
    private Object traverseJsonObject(String[] segments, Map<String, ?> jsonMap) {
        if(segments == null) return null;
        if(segments.length == 0) return jsonMap;

        Object current = jsonMap;

        for(String segment : segments) {
            if(isAMap(current)) {

                current = ((Map<String, Object>) current).get(segment);
            } else if(isAList(current)) {
                Integer index = tryParseInt(segment);
                if(index == null) {
                    return null;
                }

                List<Object> list = (List<Object>) current;
                if(isOutOfBounds(index, list)) {
                    return null;
                }

                current = list.get(index);
            } else return null;
        }

        return current;
    }

    /**
     * Another core method for traversing / searching a map structure and
     * creating elements along the way if specified keys are not found.
     * @param segments A sequence of keys to search for in the traversal.
     * @param jsonMap A map structure that will be traversed. Note that it's
     *                keys have to be of type String.
     */
    private Object traverseJsonObjectAndCreateElementsIfAbsent
            (String[] segments, Map<String, ?> jsonMap) {
        if(segments.length == 0) return jsonMap;

        Object current = jsonMap;

        for(String segment : segments) {
            if (isAMap(current)) {
                Map<String, Object> map = (Map<String, Object>) current;

                if (!map.containsKey(segment)) {
                    map.put(segment, new HashMap<>());
                }

                current = map.get(segment);
            } else if (isAList(current)) {
                List<Object> list = (List<Object>) current;

                Integer index = tryParseInt(segment);

                if (index == null || index != list.size()) {
                    return null;
                }

                list.add(new HashMap<>());

                current = list.get(index);
            }
        }

        return current;
    }

    private boolean isOutOfBounds(Integer index, Collection<Object> list) {
        return index < 0 || index >= list.size();
    }

    private List<?> convertObjectToList(Object json) {
        if(isAMap(json)) {
            return new ArrayList<>(((Map<?, ?>) json).entrySet());
        }

        if(isAList(json)) {
            return (List<?>) json;
        }

        if(json != null) {
            return List.of(json);
        }

        return new ArrayList<>();
    }

    private boolean elementExists(String[] segments) {
        return traverseJsonObject(segments, jsonMapData) != null;
    }

    private boolean isAMap(Object value) {
        return value instanceof Map<?, ?>;
    }

    private boolean isAList(Object value) {
        return value instanceof List<?>;
    }
}
