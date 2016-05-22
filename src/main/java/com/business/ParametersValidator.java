package com.business;

import br.com.caelum.vraptor.Result;

import static br.com.caelum.vraptor.view.Results.http;

public final class ParametersValidator {

    private ParametersValidator() {

    }

    public static void sendBadRequest(Result result) {
        result.use(http()).setStatusCode(400);
        result.use(http()).body("Invalid parameters");
    }

    /**
     * Check parameters list contains a null value
     *
     * @param parameters list to be checked
     * @return {@code true} if there is a null parameter
     */
    public static boolean containsNullValue(Object... parameters) {
        boolean containsNull = false;
        for (Object parameter : parameters) {
            if (parameter == null) {
                containsNull = true;
                break;
            }
        }

        return containsNull;
    }

    public static boolean isGameIdValid(String id) {
        return id.length() == 24;
    }

}
