package cz.cvut.kbss.jsonld.jackson.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.jsonldjava.sesame.SesameJSONLDParserFactory;
import cz.cvut.kbss.jsonld.jackson.environment.Generator;
import cz.cvut.kbss.jsonld.jackson.environment.StatementCopyingHandler;
import cz.cvut.kbss.jsonld.jackson.environment.Vocabulary;
import cz.cvut.kbss.jsonld.jackson.environment.model.Employee;
import cz.cvut.kbss.jsonld.jackson.environment.model.Organization;
import cz.cvut.kbss.jsonld.jackson.environment.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
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
import java.util.Date;

import static org.junit.Assert.assertTrue;

public class JsonLdSerializationTest {

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
        final SimpleModule module = new SimpleModule();
        module.setSerializerModifier(new JsonLdSerializerModifier());
        objectMapper.registerModule(module);
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
        assertTrue(contains(user.getUri(), RDF.TYPE.stringValue(), URI.create(Vocabulary.PERSON)));
        assertTrue(contains(user.getUri(), RDF.TYPE.stringValue(), URI.create(Vocabulary.USER)));
        assertTrue(contains(user.getUri(), Vocabulary.FIRST_NAME, user.getFirstName()));
        assertTrue(contains(user.getUri(), Vocabulary.LAST_NAME, user.getLastName()));
        assertTrue(contains(user.getUri(), Vocabulary.USERNAME, user.getUsername()));
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
        // JSON-LD doesn't have a Date representation, so it will be parsed as a string
        assertTrue(contains(org.getUri(), Vocabulary.DATE_CREATED, org.getDateCreated().toString()));
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
            // JSON-LD implementations store IRIs of other individuals as plain strings, which causes them to be parsed as strings by Sesame
            // The question is: how should the serializer/parser behave in such situations?
            assertTrue(contains(emp.getUri(), Vocabulary.IS_MEMBER_OF, org.getUri().toString()));
            verifyUserAttributes(emp);
        }
    }
}
