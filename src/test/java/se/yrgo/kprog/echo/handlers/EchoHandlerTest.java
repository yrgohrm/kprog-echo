/*
 * Copyright 2020 Hampus.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import spark.Request;
import spark.Response;

/**
 * Tests for EchoHandler.
 *
 */
public class EchoHandlerTest {
    private static final String data = "{\"data\":1.0}";

    /**
     * Test GET method.
     */
    @Test
    public void testGetEcho() {
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        when(request.queryParams("data")).thenReturn(data);

        String result = EchoHandler.getEcho(request, response);
        assertEquals(data, result);
    }

    /**
     * Test GET method with bad input.
     */
    @Test
    public void testGetEchoBad() {
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        String testData = "22" + data;

        when(request.queryParams("data")).thenReturn(testData);

        String result = EchoHandler.getEcho(request, response);

        assertNotEquals(testData, result);
        verify(response, times(1)).status(400);
    }

    /**
     * Test GET method with delay parameter.
     */
    @Test
    public void testGetEchoDelay() {
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        when(request.queryParams("data")).thenReturn(data);
        when(request.queryParams("delay")).thenReturn("1");

        long before = System.currentTimeMillis();
        EchoHandler.getEcho(request, response);
        long diff = System.currentTimeMillis() - before;

        // allow some leniency when it comes to timing
        assertTrue(diff >= 950);
    }

    /**
     * Test GET method with bad delay parameter (>60).
     */
    @Test
    public void testGetEchoBadDelay() {
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        when(request.queryParams("data")).thenReturn(data);
        when(request.queryParams("delay")).thenReturn("61");

        String result = EchoHandler.getEcho(request, response);

        assertNotEquals(data, result);
        verify(response, times(1)).status(400);
    }

    /**
     * Test POST method.
     */
    @Test
    public void testPostEcho() {
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        when(request.body()).thenReturn(data);

        String result = EchoHandler.postEcho(request, response);
        assertEquals(data, result);
    }

    /**
     * Test POST method with bad input.
     */
    @Test
    public void testPostEchoBad() {
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        String testData = "22" + data;

        when(request.body()).thenReturn(testData);

        String result = EchoHandler.postEcho(request, response);

        assertNotEquals(testData, result);
        verify(response, times(1)).status(400);
    }
}
