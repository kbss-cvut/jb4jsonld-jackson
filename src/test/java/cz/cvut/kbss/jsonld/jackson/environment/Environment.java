/*
 * JB4JSON-LD Jackson
 * Copyright (C) 2025 Czech Technical University in Prague
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
package cz.cvut.kbss.jsonld.jackson.environment;

import cz.cvut.kbss.jsonld.jackson.environment.model.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Environment {

    /**
     * Reads test data from the test resources classpath folder.
     *
     * @param fileName File to read
     * @return Content of the file
     */
    public static String readData(String fileName) {
        final File file = new File(Environment.class.getClassLoader().getResource(fileName).getFile());
        assert file.exists();
        try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
            final StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException("Unable to read test file " + fileName, e);
        }
    }

    public static void verifyUserAttributes(User expected, User actual) {
        assertEquals(expected.getUri(), actual.getUri());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getAdmin(), actual.getAdmin());
    }
}
