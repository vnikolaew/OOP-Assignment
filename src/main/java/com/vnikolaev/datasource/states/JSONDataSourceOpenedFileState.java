package com.vnikolaev.datasource.states;

import com.vnikolaev.abstractions.FileIODevice;
import com.vnikolaev.abstractions.JSONConverter;
import com.vnikolaev.datasource.*;
import com.vnikolaev.datasource.conversions.JSONConversionResult;

import java.io.IOException;
import java.util.*;

public class JSONDataSourceOpenedFileState implements JSONDataSourceState {

    private Map<String, Object> jsonMapData;

    private final JSONDataSourceImpl dataSource;

    public JSONDataSourceOpenedFileState(JSONDataSourceImpl dataSource) {
        this.dataSource = dataSource;
        this.jsonMapData = dataSource.getJsonMapData();
    }

    @Override
    public DataSourceOperationResult open(String location) {
        return DataSourceOperationResult.failure(
                List.of("There's an already opened file in use."));
    }

    @Override
    public DataSourceOperationResult close() {
        String currentFile = dataSource.getCurrentFile().getPath();

        dataSource
                .setCurrentState(new JSONDataSourceClosedFileState(dataSource));

        return DataSourceOperationResult
                .success("Successfully closed " + currentFile);
    }

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
            dataSource.getFileIO().write(location, result.data());
            return DataSourceOperationResult
                    .success("Successfully saved " + location);
        } catch (IOException e) {
            return DataSourceOperationResult
                    .failure(List.of("Error trying to open a file: " + e.getMessage()));
        }
    }

    @Override
    public DataSourceOperationResult setElement(String path, String jsonPayload) {
        String[] segments = getPathSegments(path);

        if(!elementExists(segments)) {
            return DataSourceOperationResult
                    .failure(List.of("Element with that key doesn't exist."));
        }

        JSONConversionResult<Object> conversionResult = dataSource.getJsonConverter().stringToObject(jsonPayload);
        if(!conversionResult.isSuccessful()) {
            return DataSourceOperationResult
                    .failure(List.of("Invalid JSON schema."));
        }

        Object jsonObject = conversionResult.data();

        Object current = traverseJsonObject
                (Arrays.copyOf(segments, segments.length - 1), jsonMapData);
        String lastSegment = segments[segments.length - 1];

        if(isAList(current)) {
            List<Object> list = (List<Object>) current;

            Integer index = tryParseInt(lastSegment);
            if(index == null || isOutOfBounds(index, list)) {
                return DataSourceOperationResult.failure(List.of("Invalid path"));
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

    @Override
    public DataSourceOperationResult createElement(String path, String jsonPayload) {
        String[] segments = getPathSegments(path);

        if (elementExists(segments)) {
            return DataSourceOperationResult
                    .failure(List.of("Element with that key already exists."));
        }

        JSONConversionResult<Object> conversionResult = dataSource.getJsonConverter().stringToObject(jsonPayload);
        if(!conversionResult.isSuccessful()) {
            return DataSourceOperationResult
                    .failure(List.of("Invalid JSON schema."));
        }

        Object jsonObject = conversionResult.data();

        Object current = traverseJsonObjectAndCreateElementsIfAbsent
                (Arrays.copyOf(segments, segments.length - 1), jsonMapData);
        String lastSegment = segments[segments.length - 1];

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

    @Override
    public DataSourceOperationResult deleteElement(String path) {
        String[] segments = getPathSegments(path);

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

    @Override
    public DataSourceOperationResult moveElements(String fromPath, String toPath) {
        String[] fromSegments = getPathSegments(fromPath);
        String[] toSegments = getPathSegments(toPath);

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

    @Override
    public List<?> searchElement(String key) {
        String normalizedString = normalizeString(key);

        if(normalizedString.isEmpty()) {
            return convertObjectToList(jsonMapData);
        }

        String[] segments = normalizedString.split("/");

        Object currentJsonElement = traverseJsonObject(segments, jsonMapData);

        return convertObjectToList(currentJsonElement);
    }

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
                        .setCurrentState(new JSONDataSourceClosedFileState(dataSource));

                return DataSourceOperationResult.failure(List.of(
                        "Invalid JSON schema.", result.error()));
            }

            jsonMapData = result.data();
            dataSource.setJsonMapData(result.data());

            return DataSourceOperationResult.success("JSON Schema is valid.");
        } catch (IOException e) {
            dataSource.setCurrentState(new JSONDataSourceClosedFileState(dataSource));

            return DataSourceOperationResult.failure(List.of(
                    "Error trying to open a file: ", e.getMessage()));
        }
    }

    @Override
    public String toFriendlyString() {
        return dataSource
                .getJsonConverter()
                .mapToString(jsonMapData)
                .data();
    }


    private String normalizeString(String key) {
        String newKey = removeWhiteSpaces(key);
        return newKey.startsWith("/")
                ? newKey.substring(1)
                : newKey;
    }

    private String removeWhiteSpaces(String key) {
        return key.replaceAll(" ", "");
    }

    private String[] getPathSegments(String path) {
        return normalizeString(path).split("/");
    }

    private Integer tryParseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Object traverseJsonObject(String[] segments, Map<String, ?> jsonMap) {
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

    private Object traverseJsonObjectAndCreateElementsIfAbsent
            (String[] segments, Map<String, ?> jsonMap) {
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
