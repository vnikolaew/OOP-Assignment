package com.vnikolaev.abstractions;

import com.vnikolaev.datasource.DataSourceOperationResult;

/**
 * A base interface that represents defines common operations that a
 * data source should support, such as opening, closing and saving.
 */
public interface DataSource {
    DataSourceOperationResult open(String location);
    DataSourceOperationResult close();
    DataSourceOperationResult save();
    DataSourceOperationResult saveAs(String location);
    DataSourceOperationResult changeDirectory(String location);
}
