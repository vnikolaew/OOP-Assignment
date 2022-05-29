package com.vnikolaev.datasource.conversions;

import com.vnikolaev.abstractions.JSONConverter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * A JSON conversion service that effectively acts as an adapter between
 * the foreign JSON library and the interface that our application expects
 * and wants to work with. Supports conversions between the native HashMap
 * and String (and vice/versa), as well as plain Objects and String.
 */
public class JSONConverterImpl implements JSONConverter {

    /**
     * @param jsonMap A map data structure that is going to be converted bac
     *                to a JSON string representation.
     * @return A standard string representing the JSON data structure with
     * an indentation level of 3.
     */
    @Override
    public JSONConversionResult<String> mapToString(Map<String, ?> jsonMap) {
        try {
            String json = new JSONObject(jsonMap).toString(3);
            return JSONConversionResult.success(json);
        } catch (JSONException e) {
            return JSONConversionResult.failure(e.getMessage());
        }
    }

    /**
     * @param jsonPayload A JSON object in a string representation.
     * @return A native HashMap object holding the corresponding JSON
     * data structure.
     */
    @Override
    public JSONConversionResult<Map<String, Object>> stringToMap(String jsonPayload) {
        try {
            Map<String, Object> jsonMap = new JSONObject(jsonPayload).toMap();
            return JSONConversionResult.success(jsonMap);
        } catch (JSONException e) {
            return JSONConversionResult.failure(e.getMessage());
        }
    }

    @Override
    public JSONConversionResult<Object> stringToObject(String jsonPayload) {
        Object jsonObject;
        if((jsonObject = stringToList(jsonPayload).data()) == null) {
            if((jsonObject = stringToMap(jsonPayload).data()) == null) {
                jsonObject = jsonPayload;
            }
        }
        return JSONConversionResult.success(jsonObject);
    }

    @Override
    public JSONConversionResult<List<?>> stringToList(String jsonPayload) {
        try {
            List<?> jsonList = new JSONArray(jsonPayload).toList();
            return JSONConversionResult.success(jsonList);
        } catch (JSONException e) {
            return JSONConversionResult.failure(e.getMessage());
        }
    }

    @Override
    public JSONConversionResult<String> listToString(List<?> listPayload) {
        try {
            String json = new JSONArray(listPayload).toString(3);
            return JSONConversionResult.success(json);
        } catch (JSONException e) {
            return JSONConversionResult.failure(e.getMessage());

        }
    }
}
