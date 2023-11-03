/*
 * JB4JSON-LD Jackson
 * Copyright (C) 2023 Czech Technical University in Prague
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package cz.cvut.kbss.jsonld.jackson.example;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import cz.cvut.kbss.jsonld.jackson.JsonLdModule;
import cz.cvut.kbss.jsonld.jackson.example.model.Organization;
import cz.cvut.kbss.jsonld.jackson.example.model.User;
import cz.cvut.kbss.jsonld.jackson.serialization.SerializationConstants;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class Example {

    private final ObjectMapper objectMapper;

    public static void main(String[] args) throws Exception {
        final Example ex = new Example();
        if (args.length < 1) {
            System.err.println("Provide at least 1 argument to determine what the demo should do.\n\n");
            printHelp();
            return;
        }
        final String type = args[0].toLowerCase();
        switch (type) {
            case "su":
                ex.runSerializeSingleUser();
                break;
            case "slu":
                ex.runSerializeListOfUsers();
                break;
            case "so":
                ex.runSerializeSingleOrganization();
                break;
            case "du":
                ex.runDeserializeSingleUser(loadDeserializationInput(args, "singleUser.json"));
                break;
            case "do":
                ex.runDeserializeSingleOrganization(loadDeserializationInput(args, "singleOrganization.json"));
                break;
            default:
                System.err.println("Unsupported option '" + type + "'.\n\n");
                printHelp();
                break;
        }
    }

    private Example() {
        this.objectMapper = initObjectMapper();
    }

    private ObjectMapper initObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        // Include only non-null values in serialization
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // Pretty print serialization output
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        // Ignore unknown properties in deserialization input
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Here we create the JSON-LD serialization/deserialization module
        final JsonLdModule module = new JsonLdModule();
        // Select the serialization output form
        module.configure(SerializationConstants.FORM, SerializationConstants.FORM_COMPACT_WITH_CONTEXT);
        // Register the module
        objectMapper.registerModule(module);
        return objectMapper;
    }

    private static void printHelp() {
        System.out.println("Use one of the following values as program argument:");
        System.out.println("1. 'slu' - serialize a list of User objects.");
        System.out.println("2. 'su' - serialize a single User instance.");
        System.out.println("3. 'so' - serialize a single Organization referencing a list of member Users.");
        System.out.println("4. 'du' - deserialize a single User, optionally provide path to an input file as the second argument.");
        System.out.println("5. 'do' - deserialize a single Organization, optionally provide path to an input file as the second argument.");
    }

    private static String loadDeserializationInput(String[] args, String fallback) throws IOException {
        if (args.length > 1) {
            final String path = args[1];
            final File inputFile = new File(path);
            return String.join("\n", Files.readAllLines(inputFile.toPath(), StandardCharsets.UTF_8));
        } else {
            final InputStream is = Example.class.getClassLoader().getResourceAsStream(fallback);
            assert is != null;
            final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            final StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }

    private void runSerializeListOfUsers() throws IOException {
        final List<User> users =
                Arrays.asList(Generator.generateUser(), Generator.generateUser(), Generator.generateUser());
        System.out.println(objectMapper.writeValueAsString(users));
    }

    private void runSerializeSingleUser() throws IOException {
        final User user = Generator.generateUser();
        System.out.println(objectMapper.writeValueAsString(user));
    }

    private void runSerializeSingleOrganization() throws IOException {
        final Organization organization = Generator.generateOrganization();
        System.out.println(objectMapper.writeValueAsString(organization));
    }

    private void runDeserializeSingleUser(String input) throws IOException {
        final User result = objectMapper.readValue(input, User.class);
        System.out.println(result);
    }

    private void runDeserializeSingleOrganization(String input) throws IOException {
        final Organization result = objectMapper.readValue(input, Organization.class);
        System.out.println(result);
    }
}
