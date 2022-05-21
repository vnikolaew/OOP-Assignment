package com.vnikolaev.abstractions;

import com.vnikolaev.datasource.DataSourceOperationResult;

import java.util.List;

public interface JSONDataSource extends DataSource {
    DataSourceOperationResult setElement(String path, String jsonPayload);
    DataSourceOperationResult createElement(String path, String jsonPayload);

    DataSourceOperationResult deleteElement(String path);
    DataSourceOperationResult moveElements(String fromPath, String toPath);

    List<?> searchElement(String key);

    DataSourceOperationResult validateSchema();
    String toFriendlyString();
}
