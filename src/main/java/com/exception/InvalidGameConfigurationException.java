package com.exception;

import java.util.Map;
import java.util.stream.Collectors;

public class InvalidGameConfigurationException extends ChiefMastermindRuntimeException {

    private Map<String, String> invalidParameters;

    /**
     * Creates a new invalidGameConfigurationException
     * @param invalidParameters map with illegal parameters and their values
     */
    public InvalidGameConfigurationException(Map<String, String> invalidParameters) {
        super(generateMessage(invalidParameters));
        this.invalidParameters = invalidParameters;
    }

    private static String generateMessage(Map<String, String> invalidParameters) {
        String fieldsDescription = invalidParameters.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(","));

        return "The following parameter(s) were wrong -> " + fieldsDescription;
    }

    public Map<String, String> getInvalidParameters() {
        return invalidParameters;
    }
}
