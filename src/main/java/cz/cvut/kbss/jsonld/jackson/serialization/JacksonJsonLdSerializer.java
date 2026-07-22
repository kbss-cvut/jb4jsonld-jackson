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
package cz.cvut.kbss.jsonld.jackson.serialization;

import cz.cvut.kbss.jsonld.Configuration;
import cz.cvut.kbss.jsonld.serialization.JsonLdSerializer;
import cz.cvut.kbss.jsonld.serialization.serializer.ValueSerializer;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.jsontype.TypeSerializer;

import java.util.Collection;
import java.util.Map;

class JacksonJsonLdSerializer<T> extends tools.jackson.databind.ValueSerializer<T> {

    private final Configuration configuration;

    private final Map<Class<?>, ValueSerializer<?>> commonSerializers;

    private final tools.jackson.databind.ValueSerializer<T> baseSerializer;

    JacksonJsonLdSerializer(Configuration configuration, Map<Class<?>, ValueSerializer<?>> commonSerializers,
                            tools.jackson.databind.ValueSerializer<T> baseSerializer) {
        this.configuration = configuration;
        this.commonSerializers = commonSerializers;
        this.baseSerializer = baseSerializer;
    }

    @Override
    public void serialize(T value, JsonGenerator jsonGenerator,
                          SerializationContext serializerProvider) {
        if (shouldUseBaseSerializer(value)) {
            baseSerializer.serialize(value, jsonGenerator, serializerProvider);
        } else {
            final JsonLdSerializer serializer = createSerializer(jsonGenerator);
            serializer.serialize(value);
        }
    }

    private boolean shouldUseBaseSerializer(T value) {
        if (!(value instanceof Collection<?> col)) {
            return false;
        }
        if (col.isEmpty()) {
            return false;
        }
        // Peek at the first element to see if we should handle it
        final Object firstElem = col.iterator().next();
        return firstElem == null || !JsonLdSerializerModifier.isJsonLdCompatible(firstElem.getClass());
    }

    private JsonLdSerializer createSerializer(JsonGenerator jsonGenerator) {
        final cz.cvut.kbss.jsonld.serialization.JsonGenerator writer = new JacksonJsonWriter(jsonGenerator);
        final JsonLdSerializer serializer;
        if (SerializationConstants.FORM_COMPACT_WITH_CONTEXT.equals(
                configuration.get(SerializationConstants.FORM, SerializationConstants.FORM_COMPACT))) {
            serializer =
                    JsonLdSerializer.createContextBuildingJsonLdSerializer(writer, new Configuration(configuration));
        } else {
            serializer = JsonLdSerializer.createCompactedJsonLdSerializer(writer, new Configuration(configuration));
        }
        commonSerializers.forEach((type, valueSerializer) -> serializer.registerSerializer((Class) type,
                                                                                           (ValueSerializer) valueSerializer));
        return serializer;
    }

    @Override
    public void serializeWithType(T value, JsonGenerator gen, SerializationContext serializers,
                                  TypeSerializer typeSer) {
        serialize(value, gen, serializers);
    }
}
