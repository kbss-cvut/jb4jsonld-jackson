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
package cz.cvut.kbss.jsonld.jackson.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.cvut.kbss.jsonld.common.BeanAnnotationProcessor;
import cz.cvut.kbss.jsonld.common.JsonLdPropertyAccessResolver;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.cvut.kbss.jsonld.common.BeanAnnotationProcessor.isInstanceIdentifier;
import static cz.cvut.kbss.jsonld.common.BeanAnnotationProcessor.isTypesField;

public class JsonPropertyAccessResolver extends JsonLdPropertyAccessResolver {

    private final Map<Class<?>, Set<Field>> ignoredFields = new ConcurrentHashMap<>();

    @Override
    public boolean isReadable(Field field) {
        Objects.requireNonNull(field);
        final JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
        final JsonIgnore jsonIgnore = field.getAnnotation(JsonIgnore.class);
        return jsonIgnore == null && (jsonProperty == null || jsonProperty.access() != JsonProperty.Access.WRITE_ONLY ||
                isInstanceIdentifier(field) || isTypesField(field)) && !isJsonIgnoredPropertiesField(field) && super.isReadable(field);
    }

    private boolean isJsonIgnoredPropertiesField(Field field) {
        final Class<?> cls = field.getDeclaringClass();
        if (!ignoredFields.containsKey(cls)) {
            final JsonIgnoreProperties ann = cls.getAnnotation(JsonIgnoreProperties.class);
            if (ann == null) {
                ignoredFields.put(cls, Collections.emptySet());
            } else {
                final List<Class<?>> classes = BeanAnnotationProcessor.getAncestors(cls);
                final Map<String, Field> fieldMap = new HashMap<>();
                classes.stream().map(Class::getDeclaredFields).flatMap(Stream::of)
                       .forEach(f -> fieldMap.put(f.getName(), f));
                ignoredFields.put(cls, Stream.of(ann.value()).filter(fieldMap::containsKey).map(fieldMap::get)
                                             .collect(Collectors.toSet()));
            }
        }
        return ignoredFields.get(cls).contains(field);
    }

    @Override
    public boolean isWriteable(Field field) {
        Objects.requireNonNull(field);
        final JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
        final JsonIgnore jsonIgnore = field.getAnnotation(JsonIgnore.class);
        return jsonIgnore == null && (jsonProperty == null || jsonProperty.access() != JsonProperty.Access.READ_ONLY) &&
                !isJsonIgnoredPropertiesField(field) && super.isWriteable(field);
    }
}
