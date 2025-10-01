/*
 * JB4JSON-LD Jackson
 * Copyright (C) 2025 Czech Technical University in Prague
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


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.cfg.CacheProvider;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializerCache;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.util.ClassUtil;
import cz.cvut.kbss.jsonld.deserialization.JsonLdDeserializer;

/**
 * Copy of {@link Impl}, and adds a reference to ExpandedJsonLdDeserializer so the cleanup method can be called.
 */
public class JsonLdDeserializationContext extends DefaultDeserializationContext {

	private static final long serialVersionUID = 1L;

	private JsonLdDeserializer jsonLdDeserializer;


	public JsonLdDeserializationContext(DeserializerFactory df) {
		super(df, new DeserializerCache());
	}

	private JsonLdDeserializationContext(JsonLdDeserializationContext src,
										 DeserializationConfig config, JsonParser p, InjectableValues values) {
		super(src, config, p, values);
	}

	private JsonLdDeserializationContext(JsonLdDeserializationContext src) {
		super(src);
	}

	private JsonLdDeserializationContext(JsonLdDeserializationContext src, DeserializerFactory factory) {
		super(src, factory);
	}

	private JsonLdDeserializationContext(JsonLdDeserializationContext src, DeserializationConfig config) {
		super(src, config);
	}

	private JsonLdDeserializationContext(JsonLdDeserializationContext src, CacheProvider cp) {
		super(src, cp);
	}

	public JsonLdDeserializer getJsonLdDeserializer() {
		return jsonLdDeserializer;
	}

	public void setJsonLdDeserializer(JsonLdDeserializer jsonLdDeserializer) {
		this.jsonLdDeserializer = jsonLdDeserializer;
	}

	@Override
	public DefaultDeserializationContext copy() {
		ClassUtil.verifyMustOverride(JsonLdDeserializationContext.class, this, "copy");
		return new JsonLdDeserializationContext(this);
	}

	@Override
	public DefaultDeserializationContext createInstance(DeserializationConfig config,
														JsonParser p, InjectableValues values) {
		return new JsonLdDeserializationContext(this, config, p, values);
	}

	@Override
	public DefaultDeserializationContext createDummyInstance(DeserializationConfig config) {
		return new JsonLdDeserializationContext(this, config);
	}

	@Override
	public DefaultDeserializationContext with(DeserializerFactory factory) {
		return new JsonLdDeserializationContext(this, factory);
	}

	@Override
	public DefaultDeserializationContext withCaches(CacheProvider cp) {
		return new JsonLdDeserializationContext(this, cp);
	}

	@Override
	public void checkUnresolvedObjectId() throws UnresolvedForwardReference {
		super.checkUnresolvedObjectId();
		if (jsonLdDeserializer != null) {
			jsonLdDeserializer.cleanup();
		}
	}

}
