package com.vnikolaev.abstractions;

import com.vnikolaev.datasource.DataSourceOperationResult;

public interface DataSource {
    DataSourceOperationResult open(String location);
    DataSourceOperationResult close();
    DataSourceOperationResult save();
    DataSourceOperationResult saveAs(String location);
}
