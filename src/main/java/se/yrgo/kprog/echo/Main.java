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
package se.yrgo.kprog.echo;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.yrgo.kprog.echo.handlers.EchoHandler;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

/**
 * Application entrypoint for Echo Service.
 *
 */
public class Main {
    static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = getOptions();

        try {
            CommandLine line = parser.parse(options, args);

            if (line.hasOption('h')) {
                printHelp(options);
            }
            else {
                int port = 8080;
                if (line.hasOption('p')) {
                    String portString = line.getOptionValue('p');
                    port = Short.parseShort(portString);
                }

                String dir = "webroot";
                if (line.hasOption('d')) {
                    dir = line.getOptionValue('d');
                }

                startServer(dir, port);
            }
        }
        catch (ParseException | NumberFormatException ex) {
            logger.error("Unable to parse command line.");
            printHelp(options);
        }
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("kprog-fortune", options);
    }

    private static void startServer(String dir, int port) {
        port(port);

        staticFiles.externalLocation(dir);

        path("/api", () -> {
            get("/echo", EchoHandler::getEcho);
            post("/echo", EchoHandler::postEcho);
        });
    }

    private static Options getOptions() {
        final var options = new Options();

        Option port = Option.builder("p")
                .hasArg()
                .argName("number")
                .longOpt("port")
                .desc("web server port (default 8080)")
                .build();

        options.addOption(port);

        Option dir = Option.builder("d")
                .hasArg()
                .argName("path")
                .longOpt("dir")
                .desc("web root directory (default \"webroot\")")
                .build();

        options.addOption(dir);

        Option help = Option.builder("h")
                .longOpt("help")
                .desc("show help")
                .build();

        options.addOption(help);

        return options;
    }
}
