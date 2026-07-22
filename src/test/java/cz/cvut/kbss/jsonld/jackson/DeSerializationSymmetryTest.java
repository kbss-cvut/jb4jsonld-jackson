/*
 * JB4JSON-LD Jackson
 * Copyright (C) 2026 Czech Technical University in Prague
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
package cz.cvut.kbss.jsonld.jackson;

import tools.jackson.databind.ObjectMapper;
import cz.cvut.kbss.jsonld.jackson.environment.Environment;
import cz.cvut.kbss.jsonld.jackson.environment.Generator;
import cz.cvut.kbss.jsonld.jackson.environment.model.Employee;
import cz.cvut.kbss.jsonld.jackson.environment.model.User;
import cz.cvut.kbss.jsonld.jackson.serialization.SerializationConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DeSerializationSymmetryTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        final JsonLdModule module = new JsonLdModule();
        module.configure(SerializationConstants.FORM, SerializationConstants.FORM_COMPACT_WITH_CONTEXT);
        this.objectMapper = JsonMapper.builder().addModule(module).build();
    }

    @Test
    void deSerializationIsSymmetricForSingleObjectWithDataAttributes() {
        final User user = Generator.generateUser();

        final String json = objectMapper.writeValueAsString(user);
        final User result = objectMapper.readValue(json, User.class);
        Environment.verifyUserAttributes(user, result);
    }

    @Test
    void deSerializationIsSymmetricForObjectWithBackwardReference() {
        final Employee employee = Generator.generateEmployee();
        employee.getEmployer().addEmployee(employee);

        final String json = objectMapper.writeValueAsString(employee);
        final Employee result = objectMapper.readValue(json, Employee.class);
        Environment.verifyUserAttributes(employee, result);
        assertEquals(employee.getSalary(), result.getSalary());
        assertNotNull(result.getEmployer());
        assertThat(result.getEmployer().getEmployees(), hasItem(result));
    }
}
