package com.vnikolaev.datasource;

import com.vnikolaev.FileNameConstants;
import com.vnikolaev.abstractions.*;
import com.vnikolaev.datasource.conversions.JSONConverterImpl;
import com.vnikolaev.abstractions.JSONPathInterpreter;
import com.vnikolaev.datasource.pathinterpretors.RouteBasedJSONPathInterpreter;
import com.vnikolaev.datasource.states.*;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JSONDataSourceImplTest {

    private JSONDataSourceImpl jsonDataSource;
    private JSONConverter jsonConverter;
    private JSONPathInterpreter pathInterpreter;

    private static final String baseDirectory
            = FileNameConstants.testFilesDirectory;
    private static final String filePath = "personal-info.json";

    @BeforeEach
    void setUp() {
        // Dependencies:
        FileIODevice fileIO = new FileIOMock();
        jsonConverter = new JSONConverterImpl();
        pathInterpreter = new RouteBasedJSONPathInterpreter();

        jsonDataSource = new JSONDataSourceImpl(fileIO, jsonConverter, pathInterpreter);

        jsonDataSource.setCurrentDirectory(baseDirectory);
        jsonDataSource.open("personal-info.json");
    }

    @AfterEach
    void tearDown() {
        jsonDataSource.close();
        jsonDataSource = null;
    }

    @Test
    public void dataSource_FriendlyString_ShouldReturn_CorrectStringRepresentation()
            throws IOException {
        DataSourceOperationResult result = jsonDataSource.validateSchema();

        assertTrue(result.isSuccessful());
        assertNotNull(jsonDataSource.toFriendlyString());
    }

    @Test
    public void dataSource_ShouldReturn_BadResultWhenClosingUnOpenedFile() {
        jsonDataSource.close();
        DataSourceOperationResult result = jsonDataSource.close();

        assertFalse(result.isSuccessful());
        assertTrue(result.getErrors().size() > 0);
    }

    @Test
    public void dataSource_ShouldReturn_InvalidSchema_WhenSuchIsProvided() throws IOException {
        String invalidJsonData = "{\"something\": [\"name: \"Joe\"]}";

        FileIODevice mock = mock(FileIODevice.class);

        jsonDataSource = new JSONDataSourceImpl(mock, jsonConverter, pathInterpreter);
        jsonDataSource.setCurrentFile(new File(""));

        when(mock.read(jsonDataSource.getCurrentFile().getPath()))
                .thenReturn(invalidJsonData);

        DataSourceOperationResult result = jsonDataSource.validateSchema();

        assertFalse(result.isSuccessful());
        assertNotEquals(0, result.getErrors().size());
    }

    @Test
    public void dataSource_Name_Search_ShouldReturn_AnObject() {

        final String key = "name";
        final String keyWithSlash = "/name";

        List<?> resultOne = jsonDataSource
                .searchElement(key);

        List<?> resultTwo = jsonDataSource
                .searchElement(keyWithSlash);

        List<Object> expected = new ArrayList<>();
        expected.add("Me");

        assertIterableEquals(expected, resultOne);
        assertEquals(expected.get(0), resultOne.get(0), "Success");

        assertIterableEquals(expected, resultTwo);
        assertEquals(expected.get(0), resultTwo.get(0), "Success");
    }

    @Test
    public void dataSource_PhoneNumbers_Search_ShouldReturn_AListWithManyValues() {

        final String key = "phone_numbers";
        final String keyWithSlash = "/phone_numbers";

        List<?> resultOne = jsonDataSource
                .searchElement(key);

        List<?> resultTwo = jsonDataSource
                .searchElement(keyWithSlash);

        String[] expected = new String[] {"12415411","51532213","25239621","34634609"};

        assertEquals(4, resultOne.size());
        assertEquals(4, resultTwo.size());

        int i = 0;
        for(String expectedValue : expected) {
            assertEquals(expectedValue, resultOne.get(i));
            assertEquals(expectedValue, resultTwo.get(i));

            ++i;
        }
    }

    @Test
    public void dataSource_Nested_Search_Address_Country_ShouldReturn_AListWithManyValues() {

        final String key = "addresses/0/country";
        final String keyWithSlash = "/addresses/0/country";

        List<?> resultOne = jsonDataSource
                .searchElement(key);

        List<?> resultTwo = jsonDataSource
                .searchElement(keyWithSlash);

        String expected = "BG";

        assertEquals(1, resultOne.size());
        assertEquals(1, resultTwo.size());

        assertEquals(expected, resultOne.get(0));
        assertEquals(expected, resultTwo.get(0));
    }

    @Test
    public void dataSource_Nested_Search_Address_City_ShouldReturn_AListWithManyValues() {

        final String key = "addresses/2/city";
        final String keyWithSlash = "/addresses/2/city";

        List<?> resultOne = jsonDataSource
                .searchElement(key);

        List<?> resultTwo = jsonDataSource
                .searchElement(keyWithSlash);

        String expected = "London";

        assertEquals(1, resultOne.size());
        assertEquals(1, resultTwo.size());

        assertEquals(expected, resultOne.get(0));
        assertEquals(expected, resultTwo.get(0));
    }

    @Test
    public void dataSource_Nested_Search_Addresses_ShouldReturn_AList() {

        final String key = "addresses/1";
        final String keyWithSlash = "/addresses/1";

        List<?> resultOne = jsonDataSource
                .searchElement(key);

        List<?> resultTwo = jsonDataSource
                .searchElement(keyWithSlash);

        List<Map.Entry<String, Object>> expected = new ArrayList<>();
        expected.add(Map.entry("country", "BG"));
        expected.add(Map.entry("city", "Sofia"));
        expected.add(Map.entry("postcode", "1241"));

        assertEquals(3, resultOne.size());
        assertEquals(3, resultTwo.size());

        assertEquals(expected, resultOne);
        assertEquals(expected, resultTwo);
    }

    @Test
    public void dataSource_Address_Search_ShouldReturn_AListOfMaps() {

        final String key = "addresses";
        final String keyWithSlash = "/addresses";

        List<?> resultOne = jsonDataSource
                .searchElement(key);

        List<?> resultTwo = jsonDataSource
                .searchElement(keyWithSlash);

        List<Map<String, Object>> expected = new ArrayList<>();

        assertEquals(3, resultOne.size());
        assertEquals(3, resultTwo.size());

        assertTrue(resultOne.get(0) instanceof Map<?, ?>);
        assertTrue(resultTwo.get(0) instanceof Map<?, ?>);
    }

    @Test
    public void dataSource_Search_OfNonExistingElements_ShouldReturn_AnEmptyList() {
        List<Map<String, Object>> expected = new ArrayList<>();

        List<?> resultOne = jsonDataSource
                .searchElement("/data");

        List<?> resultTwo = jsonDataSource
                .searchElement("data");

        List<?> resultThree = jsonDataSource
                .searchElement("/phone_numbers/4");

        List<?> resultFour = jsonDataSource
                .searchElement("/addresses/1/region");

        assertEquals(expected, resultOne);
        assertEquals(expected, resultTwo);
        assertEquals(expected, resultThree);
        assertEquals(expected, resultFour);
    }


    @Test
    public void dataSource_Deleting_Name_ShouldReturn_ADifferent_Object() {

        DataSourceOperationResult resultOne = jsonDataSource
                .deleteElement("/name");

        assertTrue(resultOne.isSuccessful());
        assertEquals(0, resultOne.getErrors().size());

        List<?> search = jsonDataSource.searchElement("/name");

        assertEquals(0, search.size());

        List<?> fullSearch = jsonDataSource.searchElement("/");

        assertEquals(3, fullSearch.size());
    }

    @Test
    public void dataSource_Deleting_An_Address_ShouldReturn_ADifferent_Object() {

        DataSourceOperationResult resultOne = jsonDataSource
                .deleteElement("/addresses/1");

        assertTrue(resultOne.isSuccessful());
        assertEquals(0, resultOne.getErrors().size());

        List<?> search = jsonDataSource.searchElement("/addresses");
        assertEquals(2, search.size());
    }

    @Test
    public void dataSource_Deleting_Address_City_ShouldReturn_ADifferent_Object() {

        DataSourceOperationResult resultOne = jsonDataSource
                .deleteElement("/addresses/0/city");

        assertTrue(resultOne.isSuccessful());
        assertEquals(0, resultOne.getErrors().size());

        List<?> search = jsonDataSource.searchElement("/addresses/0");
        assertEquals(2, search.size());
    }

    @Test
    public void dataSource_Deleting_NonExistingElement_ShouldReturn_AnError() {
        String expectedErrorMessage = "Element with that key doesn't exist.";

        DataSourceOperationResult resultOne = jsonDataSource
                .deleteElement("/addresses/3");

        assertFalse(resultOne.isSuccessful());
        assertEquals(expectedErrorMessage, resultOne.getErrors().get(0));

        DataSourceOperationResult resultTwo = jsonDataSource
                .deleteElement("/info");

        assertFalse(resultTwo.isSuccessful());
        assertEquals(expectedErrorMessage, resultTwo.getErrors().get(0));
    }


    @Test
    public void dataSource_Creating_New_PhoneNumber_ShouldReturn_ADifferent_Object() {
        String newPhoneNumber = "+3591353505";

        DataSourceOperationResult resultOne = jsonDataSource
                .createElement("/phone_numbers/4", newPhoneNumber);

        assertTrue(resultOne.isSuccessful());
        assertEquals(0, resultOne.getErrors().size());

        List<?> search = jsonDataSource.searchElement("/phone_numbers");

        assertEquals(5, search.size());
        assertTrue(search.contains(newPhoneNumber));
    }

    @Test
    public void dataSource_Creating_An_Existing_Address_City_ShouldReturn_Error() {
        String city = "Paris";

        DataSourceOperationResult resultOne = jsonDataSource
                .createElement("/addresses/1/city", city);

        String expectedErrorMessage = "Element with that key already exists.";

        assertFalse(resultOne.isSuccessful());
        assertTrue(resultOne.getErrors().size() > 0);

        assertEquals(expectedErrorMessage, resultOne.getErrors().get(0));

        List<?> search = jsonDataSource.searchElement("/addresses/1/city");
        assertNotEquals(search.get(0), city);
    }

    @Test
    public void dataSource_Creating_Hobbies_List_ShouldReturn_ADifferent_Object() {
        String hobbiesJson = "[\"football\",\"tennis\",\"poker\"]";

        DataSourceOperationResult resultOne = jsonDataSource
                .createElement("/hobbies", hobbiesJson);

        assertTrue(resultOne.isSuccessful());
        assertEquals(0, resultOne.getErrors().size());

        List<?> search = jsonDataSource.searchElement("/hobbies");
        assertEquals(3, search.size());
    }

    @Test
    public void dataSource_Creating_PersonalInfoObject_ShouldReturn_ADifferent_Object() {
        String personalInfoKey = "/personalInfo";
        String personalInfo = "{\"university\": \"Harvard\",\"grades\": [\"6\",\"4\"], \"favoriteSubject\": \"Maths\"}";

        DataSourceOperationResult resultOne = jsonDataSource
                .createElement(personalInfoKey, personalInfo);

        assertTrue(resultOne.isSuccessful());
        assertEquals(0, resultOne.getErrors().size());

        List<?> search = jsonDataSource.searchElement(personalInfoKey);
        assertEquals(3, search.size());
    }


    @Test
    public void dataSource_Updating_PhoneNumbers_ShouldReturn_ADifferent_Object() {
        String keyPath = "/phone_numbers";
        String newPhoneNumber = "+3532434235";

        DataSourceOperationResult resultOne = jsonDataSource
                .setElement(keyPath + "/2", newPhoneNumber);

        assertTrue(resultOne.isSuccessful());
        assertEquals(0, resultOne.getErrors().size());

        List<?> search = jsonDataSource.searchElement(keyPath);
        assertEquals(search.get(2), newPhoneNumber);
    }

    @Test
    public void dataSource_Updating_NonExistingElement_ShouldReturn_AnError() {
        String keyPath = "/addresses/0/city/part";
        String newPhoneNumber = "Mladost";

        DataSourceOperationResult resultOne = jsonDataSource
                .setElement(keyPath, newPhoneNumber);

        assertFalse(resultOne.isSuccessful());
        assertTrue(resultOne.getErrors().size() > 0);

    }

    @Test
    public void dataSource_Updating_Address_ShouldUpdate_Addresses() {
        String keyPath = "/addresses/0";
        String newAddress = "{\"country\": \"Spain\", \"city\": \"Madrid\", \"postcode\": \"3000\"}";

        DataSourceOperationResult resultOne = jsonDataSource
                .setElement(keyPath, newAddress);

        assertTrue(resultOne.isSuccessful());

        List<?> search = jsonDataSource.searchElement(keyPath);

        assertTrue(search.get(0) instanceof Map.Entry<?, ?>);

        assertEquals(3, search.size());

    }

    @Test
    public void dataSource_Updating_StringElement_WithAnObject_ShouldUpdate_JsonObject() {
        String namePath = "/name";
        String newNameInfo = "{\"first\": \"Victorio\", \"middle\": \"Vasilev\", \"last\": \"Nikolaev\"}";

        DataSourceOperationResult resultOne = jsonDataSource
                .setElement(namePath, newNameInfo);

        assertTrue(resultOne.isSuccessful());

        List<?> search = jsonDataSource.searchElement(namePath);

        assertTrue(search.get(0) instanceof Map.Entry<?, ?>);

        assertEquals(3, search.size());

    }

    @Test
    public void dataSource_Validating_Schema_WithNoOpenedFile_ShouldReturn_Error() {
        jsonDataSource.close();
        String expectedErrorMessage = "Cannot validate a schema when there's no file opened yet.";

        DataSourceOperationResult result = jsonDataSource.validateSchema();

        assertFalse(result.isSuccessful());
        assertEquals(expectedErrorMessage, result.getErrors().get(0));

    }

    @Test
    public void dataSource_OpeningFileShouldSetDataSourceStateToOpened() {
        jsonDataSource.close();
        jsonDataSource.setCurrentDirectory(baseDirectory);
        jsonDataSource.open(filePath);

        assertEquals(JSONDataSourceOpenedFileState.class, jsonDataSource.getState().getClass());
    }

    @Test
    public void dataSource_OpeningNonExistingFileShouldSetDataSourceStateToClosed() {
        jsonDataSource.close();
        jsonDataSource.setCurrentDirectory(baseDirectory);
        jsonDataSource.open("file-does-not-exist.json");

        assertEquals(JSONDataSourceClosedFileState.class, jsonDataSource.getState().getClass());
    }

    @Test
    public void dataSource_ClosingFileShouldSetDataSourceStateToClosed() {
        jsonDataSource.close();

        assertEquals(jsonDataSource.getState().getClass(), JSONDataSourceClosedFileState.class);
    }

    private class FileIOMock implements FileIODevice {

        private String jsonData;

        @Override
        public void write(String filePath, String content) {
        }

        @Override
        public String read(String filePath) throws IOException {
            if(jsonData != null) return jsonData;

            InputStream stream = new FileInputStream(JSONDataSourceImplTest.baseDirectory + "/" + JSONDataSourceImplTest.filePath);
            byte[] data = stream.readAllBytes();

            stream.close();
            return (jsonData = new String(data, StandardCharsets.UTF_8));
        }
    }
}
