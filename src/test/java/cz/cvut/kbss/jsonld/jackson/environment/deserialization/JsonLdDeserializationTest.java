package cz.cvut.kbss.jsonld.jackson.environment.deserialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.kbss.jsonld.jackson.JsonLdModule;
import cz.cvut.kbss.jsonld.jackson.environment.Environment;
import cz.cvut.kbss.jsonld.jackson.environment.model.Employee;
import cz.cvut.kbss.jsonld.jackson.environment.model.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonLdDeserializationTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JsonLdModule());
    }

    @Ignore
    @Test
    public void testDeserializeInstanceWithDataProperties() throws Exception {
        final String input = Environment.readData("objectWithDataProperties.json");
        final User expected = new User();
        expected.setUri(URI.create("http://krizik.felk.cvut.cz/ontologies/jaxb-jsonld#787088083"));
        expected.setFirstName("Catherine");
        expected.setLastName("Halsey");
        expected.setUsername("halsey@unsc.org");
        expected.setAdmin(true);
        final User result = objectMapper.readValue(input, User.class);
        assertNotNull(result);
        assertEquals(expected.getUri(), result.getUri());
        assertEquals(expected.getFirstName(), result.getFirstName());
        assertEquals(expected.getLastName(), result.getLastName());
        assertEquals(expected.getUsername(), result.getUsername());
        assertEquals(expected.getAdmin(), result.getAdmin());
    }

    @Ignore
    @Test
    public void testDeserializeInstanceWithSingularObjectProperty() throws Exception {
        final String input = Environment.readData("objectWithSingularReference.json");
        final Employee result = objectMapper.readValue(input, Employee.class);
        // TODO
    }
}
