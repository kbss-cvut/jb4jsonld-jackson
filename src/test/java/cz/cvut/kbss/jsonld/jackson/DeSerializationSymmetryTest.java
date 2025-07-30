package cz.cvut.kbss.jsonld.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.kbss.jsonld.jackson.environment.Environment;
import cz.cvut.kbss.jsonld.jackson.environment.Generator;
import cz.cvut.kbss.jsonld.jackson.environment.model.Employee;
import cz.cvut.kbss.jsonld.jackson.environment.model.User;
import cz.cvut.kbss.jsonld.jackson.serialization.SerializationConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
    }

    @Test
    void deSerializationIsSymmetricForSingleObjectWithDataAttributes() throws Exception {
        final User user = Generator.generateUser();

        final String json = objectMapper.writeValueAsString(user);
        final User result = objectMapper.readValue(json, User.class);
        Environment.verifyUserAttributes(user, result);
    }

    @Test
    void deSerializationIsSymmetricForObjectWithBackwardReference() throws Exception {
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
