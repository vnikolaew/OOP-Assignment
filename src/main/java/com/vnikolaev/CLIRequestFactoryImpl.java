package com.vnikolaev;

import com.vnikolaev.abstractions.*;
import com.vnikolaev.commands.*;
import com.vnikolaev.queries.*;

import java.util.*;
import java.lang.reflect.InvocationTargetException;

/**
 * A factory class responsible for translating user input / arguments into
 * application specific commands / queries.
 */
public class CLIRequestFactoryImpl implements CLIRequestFactory {

    private static final Map<String, Class<?>> requestMap = new HashMap<>();

    private final JSONDataSource dataSource;

    static {
        populateMap();
    }

    public CLIRequestFactoryImpl(JSONDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * A method for interpreting the user's request into a corresponding
     * action and additional arguments and converting it into a proper
     * application request object. Note that any unknown / unsupported
     * action will be translated to an Invalid command.
     */
    @Override
    public CLIRequest createRequest(String requestString) {
        String[] requestSegments = getRequestSegments(requestString);
        String action = getAction(requestSegments);
        String[] arguments = getArguments(requestSegments);

        Class<?> requestType = requestMap.getOrDefault(action, InvalidCommand.class);

        return makeRequest(requestType, arguments);
    }

    private CLIRequest makeRequest(Class<?> requestType, String[] arguments) {
        try {
            return resolveRequest(requestType, arguments);
        } catch (Exception e) {
            return new InvalidCommand();
        }
    }

    private CLIRequest resolveRequest(Class<?> requestType, String[] arguments)
            throws InstantiationException,
            IllegalAccessException,
            InvocationTargetException {
        if(requestType.equals(ExitCommand.class)) {
            return (CLIRequest) requestType.getConstructors()[0].newInstance();
        }

        if(requestType.equals(HelpCommand.class)) {
            return new HelpCommand(new DefaultRequestDescriptionFormatter());
        }

        if(requestType.equals(ValidateSchemaCommand.class)) {
            return new ValidateSchemaCommand(dataSource);
        }

        return (CLIRequest) requestType
                        .getConstructors()[0]
                        .newInstance(dataSource, arguments);
    }

    private String[] getRequestSegments(String requestString) {
        return requestString.trim().split(" ");
    }

    private String[] getArguments(String[] requestSegments) {
        return Arrays.copyOfRange(requestSegments, 1, requestSegments.length);
    }

    private String getAction(String[] requestSegments) {
        return requestSegments[0].toLowerCase();
    }

    private static void populateMap() {
        requestMap.put("open", OpenCommand.class);
        requestMap.put("close", CloseCommand.class);
        requestMap.put("save", SaveCommand.class);
        requestMap.put("saveas", SaveAsCommand.class);
        requestMap.put("print", PrintQuery.class);
        requestMap.put("search", SearchQuery.class);
        requestMap.put("set", UpdateCommand.class);
        requestMap.put("create", CreateCommand.class);
        requestMap.put("delete", DeleteCommand.class);
        requestMap.put("exit", ExitCommand.class);
        requestMap.put("help", HelpCommand.class);
        requestMap.put("move", MoveCommand.class);
        requestMap.put("validate", ValidateSchemaCommand.class);
        requestMap.put("cd", ChangeDirectoryCommand.class);
    }
}
