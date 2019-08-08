package cz.cvut.kbss.jsonld.jackson.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.Types;
import cz.cvut.kbss.jsonld.annotation.JsonLdProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.net.URI;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonPropertyAccessResolverTest {

    private JsonPropertyAccessResolver sut = new JsonPropertyAccessResolver();

    @ParameterizedTest
    @CsvSource({
            "withoutAccessConfig,         true",
            "withReadWriteAccess,         true",
            "withReadOnlyAccess,          true",
            "withReadOnlyAccessOverride,  true",
            "withWriteOnlyAccess,         false,",
            "withWriteOnlyAccessOverride, false",
            "types,                       true",
            "id,                          true",
            "withIgnore,                  false"})
    void isReadable(String fieldName, boolean result) throws Exception {
        assertEquals(result, sut.isReadable(TestClass.class.getDeclaredField(fieldName)));
    }

    @ParameterizedTest
    @CsvSource({
            "withoutAccessConfig,         true",
            "withReadWriteAccess,         true",
            "withReadOnlyAccess,          false",
            "withReadOnlyAccessOverride,  false",
            "withWriteOnlyAccess,         true",
            "withWriteOnlyAccessOverride, true",
            "withIgnore,                  false"})
    void isWriteable(String fieldName, boolean result) throws Exception {
        assertEquals(result, sut.isWriteable(TestClass.class.getDeclaredField(fieldName)));
    }

    @SuppressWarnings("unused")
    private static class TestClass {

        // This is ignored for serialization
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @Id
        private URI id;

        @OWLDataProperty(iri = "http://withoutAccessConfig")
        private String withoutAccessConfig;

        @JsonProperty
        @OWLDataProperty(iri = "http://withReadWriteAccess")
        private String withReadWriteAccess;

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @OWLDataProperty(iri = "http://withReadOnlyAccess")
        private String withReadOnlyAccess;

        // JsonLdProperty overrides JsonProperty access configuration
        @JsonLdProperty(access = JsonLdProperty.Access.READ_ONLY)
        @JsonProperty(access = JsonProperty.Access.READ_WRITE)
        @OWLDataProperty(iri = "http://withReadOnlyAccessOverride")
        private String withReadOnlyAccessOverride;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @OWLDataProperty(iri = "http://withWriteOnlyAccess")
        private String withWriteOnlyAccess;

        // JsonLdProperty overrides JsonProperty access configuration
        @JsonLdProperty(access = JsonLdProperty.Access.WRITE_ONLY)
        @JsonProperty(access = JsonProperty.Access.READ_WRITE)
        @OWLDataProperty(iri = "http://withWriteOnlyAccessOverride")
        private String withWriteOnlyAccessOverride;

        @JsonIgnore
        @OWLDataProperty(iri = "http://withIgnore")
        private String withIgnore;

        // This is ignored for serialization
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @Types
        private Set<String> types;
    }
}
