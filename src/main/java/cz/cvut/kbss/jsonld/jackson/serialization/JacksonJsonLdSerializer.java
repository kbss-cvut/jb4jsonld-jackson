/**
 * Copyright (C) 2022 Czech Technical University in Prague
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
package cz.cvut.kbss.jsonld.jackson.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.github.jsonldjava.utils.Obj;
import cz.cvut.kbss.jsonld.Configuration;
import cz.cvut.kbss.jsonld.serialization.JsonLdSerializer;
import cz.cvut.kbss.jsonld.serialization.ValueSerializer;

import java.util.Map;

class JacksonJsonLdSerializer<T> extends JsonSerializer<T> {

    private final Configuration configuration;

    private final Map<Class<?>, ValueSerializer<?>> commonSerializers;

    JacksonJsonLdSerializer(Configuration configuration, Map<Class<?>, ValueSerializer<?>> commonSerializers) {
        this.configuration = configuration;
        this.commonSerializers = commonSerializers;
    }

    @Override
    public void serialize(T value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        final cz.cvut.kbss.jsonld.serialization.JsonGenerator writer = new JacksonJsonWriter(jsonGenerator);
        final JsonLdSerializer serializer =
                JsonLdSerializer.createCompactedJsonLdSerializer(writer, new Configuration(configuration));
        commonSerializers.forEach((type, valueSerializer) -> serializer.registerSerializer((Class) type, (ValueSerializer) valueSerializer));
        serializer.serialize(value);
    }

    @Override
    public void serializeWithType(T value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) {
        serialize(value, gen, serializers);
    }
}
