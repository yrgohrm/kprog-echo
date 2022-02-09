/*
 * Copyright 2020 Hampus Ram <hampus.ram@educ.goteborg.se>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.yrgo.kprog.echo.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import spark.Request;
import spark.Response;

/**
 * Handler for echoing back get or post content as json.
 *
 */
public class EchoHandler {
    private EchoHandler() {
    }

    /**
     * Will take the query parameter "data", verify that it is valid JSON and send
     * it back.
     *
     * @param request  the request
     * @param response the response
     * @return a json string
     */
    public static String getEcho(Request request, Response response) {
        try {
            var data = request.queryParams("data");
            var json = validJson(data);

            delay(request);

            response.type("application/json");
            return json;
        }
        catch (JsonSyntaxException | IllegalArgumentException ex) {
            response.status(400);
            return ex.getMessage();
        }
    }

    /**
     * Will take the POST data, verify that it is valid JSON and send it back.
     *
     * @param request  the request
     * @param response the response
     * @return a json string
     */
    public static String postEcho(Request request, Response response) {
        try {
            var json = validJson(request.body());

            delay(request);

            response.type("application/json");
            return json;
        }
        catch (JsonSyntaxException | IllegalArgumentException ex) {
            response.status(400);
            return ex.getMessage();
        }
    }

    /**
     * It will look for the "delay" request parameter and wait for that many
     * seconds. The maximum allowed delay is 60 seconds.
     *
     * @param request
     * @throws IllegalArgumentException
     */
    private static void delay(Request request) throws IllegalArgumentException {
        var delayString = request.queryParams("delay");

        if (delayString != null && delayString.trim().length() > 0) {
            int delay = Integer.parseInt(delayString);
            if (delay > 60) {
                throw new IllegalArgumentException("Too long delay " + delay);
            }

            try {
                Thread.sleep(delay * 1000L);
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Validate that a given string is valid JSON and return valid JSON as a string.
     *
     * The output is not guaranteed to be equal to the input string.
     *
     * It will throw a JsonSyntaxException on invalid input.
     *
     * @param data the input string
     * @return a json object as a string
     */
    private static String validJson(String data) {
        var gson = new Gson();
        var jsonObject = gson.fromJson(data, Object.class);
        return gson.toJson(jsonObject);
    }
}
