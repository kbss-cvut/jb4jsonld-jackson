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
package cz.cvut.kbss.jsonld.jackson.deserialization;


import cz.cvut.kbss.jsonld.deserialization.JsonLdDeserializer;
import tools.jackson.core.FormatSchema;
import tools.jackson.core.TokenStreamFactory;
import tools.jackson.databind.DeserializationConfig;
import tools.jackson.databind.InjectableValues;
import tools.jackson.databind.deser.DeserializationContextExt;
import tools.jackson.databind.deser.DeserializerCache;
import tools.jackson.databind.deser.DeserializerFactory;
import tools.jackson.databind.deser.UnresolvedForwardReference;

/**
 * Copy of {@link Impl}, and adds a reference to ExpandedJsonLdDeserializer so the cleanup method can be called.
 */
public class JsonLdDeserializationContext extends DeserializationContextExt {

	private JsonLdDeserializer jsonLdDeserializer;

	protected JsonLdDeserializationContext(TokenStreamFactory tsf,
										   DeserializerFactory deserializerFactory, DeserializerCache cache,
										   DeserializationConfig config, FormatSchema schema,
										   InjectableValues values) {
		super(tsf, deserializerFactory, cache, config, schema, values);
	}

	public JsonLdDeserializer getJsonLdDeserializer() {
		return jsonLdDeserializer;
	}

	public void setJsonLdDeserializer(JsonLdDeserializer jsonLdDeserializer) {
		this.jsonLdDeserializer = jsonLdDeserializer;
	}

	@Override
	public void checkUnresolvedObjectId() throws UnresolvedForwardReference {
		super.checkUnresolvedObjectId();
		if (jsonLdDeserializer != null) {
			jsonLdDeserializer.cleanup();
		}
	}
}
