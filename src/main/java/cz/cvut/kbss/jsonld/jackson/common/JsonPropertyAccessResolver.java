package cz.cvut.kbss.jsonld.jackson.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.cvut.kbss.jsonld.common.JsonLdPropertyAccessResolver;

import java.lang.reflect.Field;
import java.util.Objects;

import static cz.cvut.kbss.jsonld.common.BeanAnnotationProcessor.isInstanceIdentifier;
import static cz.cvut.kbss.jsonld.common.BeanAnnotationProcessor.isTypesField;

public class JsonPropertyAccessResolver extends JsonLdPropertyAccessResolver {

    @Override
    public boolean isReadable(Field field) {
        Objects.requireNonNull(field);
        final JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
        final JsonIgnore jsonIgnore = field.getAnnotation(JsonIgnore.class);
        return jsonIgnore == null && (jsonProperty == null || jsonProperty.access() != JsonProperty.Access.WRITE_ONLY ||
                isInstanceIdentifier(field) || isTypesField(field)) && super.isReadable(field);
    }

    @Override
    public boolean isWriteable(Field field) {
        Objects.requireNonNull(field);
        final JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
        final JsonIgnore jsonIgnore = field.getAnnotation(JsonIgnore.class);
        return jsonIgnore == null && (jsonProperty == null || jsonProperty.access() != JsonProperty.Access.READ_ONLY) &&
                super.isWriteable(field);
    }
}
