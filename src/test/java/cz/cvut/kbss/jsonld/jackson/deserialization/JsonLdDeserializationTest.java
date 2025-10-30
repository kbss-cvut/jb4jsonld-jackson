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
package cz.cvut.kbss.jsonld.jackson.deserialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLAnnotationProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jsonld.ConfigParam;
import cz.cvut.kbss.jsonld.deserialization.DeserializationContext;
import cz.cvut.kbss.jsonld.deserialization.ValueDeserializer;
import cz.cvut.kbss.jsonld.jackson.JsonLdModule;
import cz.cvut.kbss.jsonld.jackson.environment.Environment;
import cz.cvut.kbss.jsonld.jackson.environment.model.AbstractCompany;
import cz.cvut.kbss.jsonld.jackson.environment.model.Company;
import cz.cvut.kbss.jsonld.jackson.environment.model.CompanyUser;
import cz.cvut.kbss.jsonld.jackson.environment.model.Employee;
import cz.cvut.kbss.jsonld.jackson.environment.model.Organization;
import cz.cvut.kbss.jsonld.jackson.environment.model.Person;
import cz.cvut.kbss.jsonld.jackson.environment.model.User;
import cz.cvut.kbss.jsonld.jackson.serialization.JsonLdSerializationTest;
import jakarta.json.JsonValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class JsonLdDeserializationTest {

    private static final URI HALSEY_URI = URI
            .create("http://krizik.felk.cvut.cz/ontologies/jb4jsonld#Catherine+Halsey");
    private static final URI LASKY_URI = URI
            .create("http://krizik.felk.cvut.cz/ontologies/jb4jsonld#Thomas+Lasky");
    private static final URI PALMER_URI = URI
            .create("http://krizik.felk.cvut.cz/ontologies/jb4jsonld#Sarah+Palmer");

    private static final Map<URI, User> USERS = initUsers();
	private static final Map<URI, CompanyUser> COMPANY_USERS = initCompanyUsers();

    private static final URI ORG_URI = URI.create("http://krizik.felk.cvut.cz/ontologies/jb4jsonld#UNSC");
    private static final String ORG_NAME = "UNSC";
    private static final String[] ORG_BRANDS = {"Spartan-II", "Mjolnir IV"};

    private ObjectMapper objectMapper;
    private JsonLdModule jsonLdModule;

    private static Map<URI, User> initUsers() {
        final Map<URI, User> map = new HashMap<>();
        map.put(HALSEY_URI, new User(HALSEY_URI, "Catherine", "Halsey", "halsey@unsc.org", true));
        map.put(LASKY_URI, new User(LASKY_URI, "Thomas", "Lasky", "lasky@unsc.org", false));
        map.put(PALMER_URI, new User(PALMER_URI, "Sarah", "Palmer", "palmer@unsc.org", false));
        return map;
    }

	private static Map<URI, CompanyUser> initCompanyUsers() {
		final Map<URI, CompanyUser> map = new HashMap<>();
        map.put(HALSEY_URI, new CompanyUser(HALSEY_URI, "Catherine", "Halsey"));
        map.put(LASKY_URI, new CompanyUser(LASKY_URI, "Thomas", "Lasky"));
        map.put(PALMER_URI, new CompanyUser(PALMER_URI, "Sarah", "Palmer"));
		return map;
	}

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
        this.jsonLdModule = new JsonLdModule();
        objectMapper.registerModule(jsonLdModule);
    }

    @Test
    void testDeserializeInstanceWithDataProperties() throws Exception {
        final String input = Environment.readData("objectWithDataProperties.json");
        final User result = objectMapper.readValue(input, User.class);
        assertNotNull(result);
        final User expected = USERS.get(HALSEY_URI);
        Environment.verifyUserAttributes(expected, result);
    }

    @Test
    void testDeserializeInstanceWithSingularObjectProperty() throws Exception {
        final String input = Environment.readData("objectWithSingularReference.json");
        final Employee result = objectMapper.readValue(input, Employee.class);
        Environment.verifyUserAttributes(USERS.get(HALSEY_URI), result);
        assertNotNull(result.getEmployer());
        verifyOrganizationAttributes(result.getEmployer());
    }

    private void verifyOrganizationAttributes(Organization actual) {
        assertEquals(ORG_URI, actual.getUri());
        assertEquals(ORG_NAME, actual.getName());
        assertNotNull(actual.getDateCreated());
        for (String b : ORG_BRANDS) {
            assertTrue(actual.getBrands().contains(b));
        }
    }

    @Test
    void testDeserializeCollectionOfInstances() throws Exception {
        final String input = Environment.readData("collectionOfInstances.json");
        final List<Employee> result = objectMapper.readValue(input, new TypeReference<List<Employee>>() {
        });
        assertNotNull(result);
        assertFalse(result.isEmpty());
        result.forEach(e -> {
            final User expected = USERS.get(e.getUri());
            Environment.verifyUserAttributes(expected, e);
        });
    }

    @Test
    void testSupportForIgnoringUnknownProperties() throws Exception {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final String input = Environment.readData("objectWithUnknownProperty.json");
        final User result = objectMapper.readValue(input, User.class);
        assertNotNull(result);
        Environment.verifyUserAttributes(USERS.get(HALSEY_URI), result);
    }

    /**
     * @see JsonLdSerializationTest#serializationIgnoresJsonTypeInfoConfiguration()
     */
    @Test
    void deserializationIgnoresJsonTypeInfo() throws Exception {
        final String input = Environment.readData("objectWithSingularReference.json");
        final Employee result = objectMapper.readValue(input, Employee.class);
        assertNotNull(result);
    }

    @Test
    void deserializationSupportsPolymorphism() throws Exception {
        jsonLdModule.configure(ConfigParam.SCAN_PACKAGE, "cz.cvut.kbss.jsonld.jackson.environment.model");
        final String input = Environment.readData("objectWithSingularReference.json");
        final Person result = objectMapper.readValue(input, Person.class);
        assertInstanceOf(Employee.class, result);
    }

    @Test
    void deserializationSkipsPropertiesMappedToFieldsWithReadOnlyAccess() throws Exception {
        final String input = Environment.readData("objectWithReadOnlyProperty.json");
        final Organization result = objectMapper.readValue(input, Organization.class);
        assertNotNull(result);
        assertNull(result.getEmployeeCount());
    }

    @Test
    void deserializationSupportsCustomDeserializers() throws Exception {
        final ValueDeserializer<Boolean> deserializer = spy(new CustomDeserializer());
        jsonLdModule.registerDeserializer(Boolean.class, deserializer);
        final String input = Environment.readData("objectWithDataProperties.json");
        final User result = objectMapper.readValue(input, User.class);
        assertNotNull(result);
        assertNull(result.getAdmin());
        verify(deserializer).deserialize(any(JsonValue.class), any(DeserializationContext.class));
    }

	@Test
	void testDeserializeListWithJsonLdDeserializationContext() throws Exception {
		final String input = Environment.readData("collectionOfInstancesAbstract.json");
		final List<AbstractCompany> result = objectMapper.readValue(input, new TypeReference<>() {
		});
		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
		AbstractCompany company = result.get(0);
		assertInstanceOf(Company.class, company);
		List<CompanyUser> employees = ((Company) company).getEmployees();
		employees.forEach(employee -> {
			final CompanyUser expected = COMPANY_USERS.get(employee.getUri());
			Environment.verifyCompanyUserAttributes(expected, employee);
		});
	}

    static class CustomDeserializer implements ValueDeserializer<Boolean> {
        @Override
        public Boolean deserialize(JsonValue map, DeserializationContext<Boolean> deserializationContext) {
            return null;
        }
    }

    @Test
    void deserializationSupportsAssumedTargetType() throws Exception {
        jsonLdModule.configure(ConfigParam.ASSUME_TARGET_TYPE, "true");
        jsonLdModule.configure(ConfigParam.SCAN_PACKAGE, "cz.cvut.kbss.jsonld.jackson.deserialization");
        final String input = """
                {
                  "@context": "https://www.w3.org/ns/activitystreams",
                  "summary": "Martin created an image",
                  "type": "Create",
                  "actor": "http://www.test.example/martin",
                  "object": "http://example.org/foo.jpg"
                }
                """;
        final Create result = objectMapper.readValue(input, Create.class);
        assertNotNull(result);
        assertEquals("http://www.test.example/martin", result.actor.id);
        assertEquals("http://example.org/foo.jpg", result.object.id);
        assertEquals("Martin created an image", result.summary);
    }

    @OWLClass(iri = "https://www.w3.org/ns/activitystreams#Create")
    public static class Create {

        @Id
        private String id;

        @OWLAnnotationProperty(iri = "https://www.w3.org/ns/activitystreams#object")
        ActivityPubObject object;

        @OWLObjectProperty(iri = "https://www.w3.org/ns/activitystreams#actor")
        ObjectOrLink actor;

        @OWLDataProperty(iri = "https://www.w3.org/ns/activitystreams#@context")
        String context;

        @OWLDataProperty(iri = "https://www.w3.org/ns/activitystreams#type")
        String type;

        @OWLDataProperty(iri = "https://www.w3.org/ns/activitystreams#summary")
        String summary;
    }

    @OWLClass(iri = "https://www.w3.org/ns/activitystreams#Object")
    public static class ActivityPubObject {
        @Id
        private String id;
    }

    @OWLClass(iri = "https://www.w3.org/ns/activitystreams#Link")
    public static class ObjectOrLink {
        @Id
        private String id;
    }
}
