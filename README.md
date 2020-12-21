# kprog-echo

## General

This is a simple web service that can echo back a JSON object.

It is also a simple web server capable of delivering static file contents making
it possible to deliver web pages from the same domain as the web service thus
removing all possible problems with same-origin-policy.

## Running

Run with Java 11+ using the fat jar. The program takes two optional parameters
`-d <webroot>` and `-p <port>` to control where static content is located as
well as setting the web server port.

By default it uses the port 8080 and the directory webroot in the current 
directory.

`java -jar kprog-echo-1.0-all.jar`

The release contains a sample application so run the server and visit:
`http://localhost:8080/index.html`.

## API

The API is very simple and has one endpoint that return JSON data.

### GET /api/echo?data=<json>

It will send back the data.

### POST /api/echo

The POST body must be valid JSON and will be echoed back.
