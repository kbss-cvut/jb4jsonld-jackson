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
package cz.cvut.kbss.jsonld.jackson.deserialization;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import cz.cvut.kbss.jsonld.Configuration;
import cz.cvut.kbss.jsonld.common.BeanAnnotationProcessor;
import cz.cvut.kbss.jsonld.common.PropertyAccessResolver;
import cz.cvut.kbss.jsonld.deserialization.ValueDeserializer;

import java.util.Map;

public class JsonLdDeserializerModifier extends BeanDeserializerModifier {

    private final Configuration configuration;

    private final Map<Class<?>, ValueDeserializer<?>> commonDeserializers;

    public JsonLdDeserializerModifier(Configuration configuration, PropertyAccessResolver propertyAccessResolver,
                                      Map<Class<?>, ValueDeserializer<?>> commonDeserializers) {
        this.configuration = configuration;
        this.commonDeserializers = commonDeserializers;
        BeanAnnotationProcessor.setPropertyAccessResolver(propertyAccessResolver);
    }

    @Override
    public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc,
                                                  JsonDeserializer<?> deserializer) {
        if (BeanAnnotationProcessor.isOwlClassEntity(beanDesc.getBeanClass())) {
            return new JacksonJsonLdDeserializer(deserializer, beanDesc.getBeanClass(), configuration,
                                                 commonDeserializers);
        }
        return deserializer;
    }
}
