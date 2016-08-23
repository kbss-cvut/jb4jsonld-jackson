/**
 * Copyright (C) 2016 Czech Technical University in Prague
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cvut.kbss.jsonld.jackson.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdProcessor;
import cz.cvut.kbss.jsonld.deserialization.JsonLdDeserializer;
import cz.cvut.kbss.jsonld.exception.JsonLdDeserializationException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JacksonJsonLdDeserializer extends DelegatingDeserializer {

    private ObjectMapper mapper = new ObjectMapper();

    private final Class<?> resultType;

    public JacksonJsonLdDeserializer(JsonDeserializer<?> delegatee, Class<?> resultType) {
        super(delegatee);
        this.resultType = resultType;
    }

    @Override
    protected JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> newDelegatee) {
        // TODO Is the null ok? Perhaps we should use a different base class, we are not delegating deserialization to anything else anyway
        return new JacksonJsonLdDeserializer(newDelegatee, null);
    }

    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
        try {
            final Object input = parseJsonObject(jp);
            final List<Object> expanded = JsonLdProcessor.expand(input);
            final JsonLdDeserializer deserializer = JsonLdDeserializer.createExpandedDeserializer();
            return deserializer.deserialize(expanded, resultType);
        } catch (JsonLdError e) {
            throw new JsonLdDeserializationException("Unable to expand the input JSON.", e);
        }
    }

    private Object parseJsonObject(JsonParser parser) throws IOException {
        Object value = null;
        final JsonToken initialToken = parser.getCurrentToken();
        parser.setCodec(mapper);
        if (initialToken == JsonToken.START_ARRAY) {
            value = parser.readValueAs(new TypeReference<List<?>>() {
            });
        } else if (initialToken == JsonToken.START_OBJECT) {
            value = parser.readValueAs(new TypeReference<Map<?, ?>>() {
            });
        } else if (initialToken == JsonToken.VALUE_STRING) {
            value = parser.readValueAs(String.class);
        } else if (initialToken == JsonToken.VALUE_FALSE || initialToken == JsonToken.VALUE_TRUE) {
            value = parser.readValueAs(Boolean.class);
        } else if (initialToken == JsonToken.VALUE_NUMBER_FLOAT || initialToken == JsonToken.VALUE_NUMBER_INT) {
            value = parser.readValueAs(Number.class);
        } else if (initialToken == JsonToken.VALUE_NULL) {
            value = null;
        }
        return value;
    }
}
