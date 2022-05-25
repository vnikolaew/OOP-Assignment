package com.vnikolaev.abstractions;

import com.vnikolaev.datasource.DataSourceOperationResult;

import java.util.List;

/**
 * An interface for interacting with JSON files / data sources. On top of
 * the base data source interface capabilities, it also supports
 * various operations on JSON elements like creating, moving and deleting,
 * as well as options for validating the schema and returning a string
 * representation of the JSON data.
 */
public interface JSONDataSource extends DataSource {
    DataSourceOperationResult setElement(String path, String jsonPayload);
    DataSourceOperationResult createElement(String path, String jsonPayload);

    DataSourceOperationResult deleteElement(String path);
    DataSourceOperationResult moveElements(String fromPath, String toPath);

    List<?> searchElement(String key);

    DataSourceOperationResult validateSchema();
    String toFriendlyString();
}
