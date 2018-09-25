/**
 * Copyright (C) 2017 Czech Technical University in Prague
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package cz.cvut.kbss.jsonld.jackson.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jsonldjava.sesame.SesameJSONLDParserFactory;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.Types;
import cz.cvut.kbss.jopa.vocabulary.RDF;
import cz.cvut.kbss.jsonld.exception.MissingTypeInfoException;
import cz.cvut.kbss.jsonld.jackson.JsonLdModule;
import cz.cvut.kbss.jsonld.jackson.environment.Generator;
import cz.cvut.kbss.jsonld.jackson.environment.StatementCopyingHandler;
import cz.cvut.kbss.jsonld.jackson.environment.Vocabulary;
import cz.cvut.kbss.jsonld.jackson.environment.model.Employee;
import cz.cvut.kbss.jsonld.jackson.environment.model.Organization;
import cz.cvut.kbss.jsonld.jackson.environment.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.RDFParserFactory;
import org.openrdf.sail.memory.MemoryStore;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertTrue;

public class JsonLdSerializationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Repository repository;
    private RepositoryConnection connection;

    private RDFParser parser;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        initRepository();
        final RDFParserFactory factory = new SesameJSONLDParserFactory();
        this.parser = factory.getParser();
        parser.setRDFHandler(new StatementCopyingHandler(connection));

        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JsonLdModule());
    }

    private void initRepository() throws Exception {
        this.repository = new SailRepository(new MemoryStore());
        repository.initialize();
        this.connection = repository.getConnection();
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
        repository.shutDown();
    }

    @Test
    public void testSerializeInstanceWithDataProperties() throws Exception {
        final User user = Generator.generateUser();
        serializeAndStore(user);
        verifyUserAttributes(user);
    }

    private void serializeAndStore(Object instance)
            throws RepositoryException, IOException, RDFParseException, RDFHandlerException {
        final String result = objectMapper.writeValueAsString(instance);
        connection.begin();
        try (final ByteArrayInputStream bin = new ByteArrayInputStream(result.getBytes())) {
            parser.parse(bin, null);
        }
        connection.commit();
    }

    private boolean contains(URI subject, String property, Object value) throws Exception {
        final ValueFactory vf = connection.getValueFactory();
        Value rdfVal = null;
        if (value instanceof URI) {
            rdfVal = vf.createURI(value.toString());
        } else if (value instanceof Integer) {
            rdfVal = vf.createLiteral((Integer) value);
        } else if (value instanceof Long) {
            rdfVal = vf.createLiteral((Long) value);
        } else if (value instanceof Double) {
            rdfVal = vf.createLiteral((Double) value);
        } else if (value instanceof Boolean) {
            rdfVal = vf.createLiteral((Boolean) value);
        } else if (value instanceof Date) {
            rdfVal = vf.createLiteral((Date) value);
        } else if (value instanceof String) {
            rdfVal = vf.createLiteral(value.toString());
        }
        return connection
                .getStatements(vf.createURI(subject.toString()), vf.createURI(property), rdfVal, false).hasNext();
    }

    private void verifyUserAttributes(User user) throws Exception {
        assertTrue(contains(user.getUri(), RDF.TYPE, URI.create(Vocabulary.PERSON)));
        assertTrue(contains(user.getUri(), RDF.TYPE, URI.create(Vocabulary.USER)));
        assertTrue(contains(user.getUri(), Vocabulary.FIRST_NAME, user.getFirstName()));
        assertTrue(contains(user.getUri(), Vocabulary.LAST_NAME, user.getLastName()));
        assertTrue(contains(user.getUri(), Vocabulary.USERNAME, user.getUsername()));
        assertTrue(contains(user.getUri(), Vocabulary.IS_ADMIN, user.getAdmin()));
    }

    @Test
    public void testSerializeInstanceWithSingularReference() throws Exception {
        final Employee employee = Generator.generateEmployee();
        final Organization org = employee.getEmployer();
        serializeAndStore(employee);
        verifyUserAttributes(employee);
        assertTrue(contains(employee.getUri(), Vocabulary.IS_MEMBER_OF, org.getUri()));
        verifyOrganizationAttributes(org);
    }

    private void verifyOrganizationAttributes(Organization org) throws Exception {
        assertTrue(contains(org.getUri(), RDFS.LABEL.stringValue(), org.getName()));
        // There is currently a grey zone of representing dates - we're using long timestamp, but RDF4J parses it XML integer
        assertTrue(contains(org.getUri(), Vocabulary.DATE_CREATED, null));
        for (String brand : org.getBrands()) {
            assertTrue(contains(org.getUri(), Vocabulary.BRAND, brand));
        }
    }

    @Test
    public void testSerializeInstanceWithPluralReference() throws Exception {
        final Organization org = Generator.generateOrganization();
        generateEmployeesForOrganization(org, false);
        serializeAndStore(org);
        verifyOrganizationAttributes(org);
        for (Employee emp : org.getEmployees()) {
            assertTrue(contains(org.getUri(), Vocabulary.HAS_MEMBER, emp.getUri()));
            verifyUserAttributes(emp);
        }
    }

    private void generateEmployeesForOrganization(Organization org, boolean backwardReference) {
        for (int i = 0; i < Generator.randomCount(10); i++) {
            final Employee emp = Generator.generateEmployee();
            emp.setEmployer(backwardReference ? org : null);
            org.addEmployee(emp);
        }
    }

    @Test
    public void testSerializeInstanceWithPluralReferenceAndBackwardReferences() throws Exception {
        final Organization org = Generator.generateOrganization();
        generateEmployeesForOrganization(org, true);
        serializeAndStore(org);
        verifyOrganizationAttributes(org);
        for (Employee emp : org.getEmployees()) {
            assertTrue(contains(org.getUri(), Vocabulary.HAS_MEMBER, emp.getUri()));
            assertTrue(contains(emp.getUri(), Vocabulary.IS_MEMBER_OF, org.getUri()));
            verifyUserAttributes(emp);
        }
    }

    @Test
    public void testSerializeCollectionOfInstances() throws Exception {
        final Set<User> users = new HashSet<>();
        for (int i = 0; i < Generator.randomCount(10); i++) {
            users.add(Generator.generateUser());
        }
        serializeAndStore(users);
        for (User u : users) {
            verifyUserAttributes(u);
        }
    }

    /**
     * Jackson's {@link com.fasterxml.jackson.annotation.JsonTypeInfo} can be ignored, because the
     * serialized/deserialized objects contain type information by virtue of the JSON-LD {@code @type} attribute.
     */
    @Test
    public void serializationIgnoresJsonTypeInfoConfiguration() throws Exception {
        final User emp = Generator.generateEmployee();
        serializeAndStore(emp);
        assertTrue(contains(emp.getUri(), RDF.TYPE, URI.create(Vocabulary.PERSON)));
        assertTrue(contains(emp.getUri(), RDF.TYPE, URI.create(Vocabulary.USER)));
        assertTrue(contains(emp.getUri(), RDF.TYPE, URI.create(Vocabulary.EMPLOYEE)));
    }

    @Test
    public void serializationSupportsClassesWithoutOWLClassAnnotationButWithTypes() throws Exception {
        final PersonNoOWLClass person = new PersonNoOWLClass();
        person.uri = Generator.generateUri();
        person.label = "test";
        person.types = Collections.singleton(Vocabulary.PERSON);
        serializeAndStore(person);
        assertTrue(contains(person.uri, RDF.TYPE, URI.create(Vocabulary.PERSON)));
        assertTrue(contains(person.uri, cz.cvut.kbss.jopa.vocabulary.RDFS.LABEL, person.label));
    }

    public static class PersonNoOWLClass {

        @Id
        private URI uri;

        @OWLDataProperty(iri = cz.cvut.kbss.jopa.vocabulary.RDFS.LABEL)
        private String label;

        @Types
        private Set<String> types;
    }

    @Test
    public void serializationFailsForInstanceWithoutTypeInfo() throws Exception {
        final PersonNoOWLClass person = new PersonNoOWLClass();
        person.uri = Generator.generateUri();
        person.label = "test";
        thrown.expectCause(isA(MissingTypeInfoException.class));
        serializeAndStore(person);
    }
}
