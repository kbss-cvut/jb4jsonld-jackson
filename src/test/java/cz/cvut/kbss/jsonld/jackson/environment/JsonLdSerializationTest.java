package cz.cvut.kbss.jsonld.jackson.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import cz.cvut.kbss.jsonld.jackson.environment.model.User;
import cz.cvut.kbss.jsonld.jackson.serialization.JsonLdSerializerModifier;
import org.junit.Before;
import org.junit.Test;

public class JsonLdSerializationTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        this.objectMapper = new ObjectMapper();
        final SimpleModule module = new SimpleModule();
        module.setSerializerModifier(new JsonLdSerializerModifier());
        objectMapper.registerModule(module);
    }

    @Test
    public void testSerializeInstanceWithDataProperties() throws Exception {
        final User user = Generator.generateUser();
        final String result = objectMapper.writeValueAsString(user);
        System.out.println(result);
    }
}
