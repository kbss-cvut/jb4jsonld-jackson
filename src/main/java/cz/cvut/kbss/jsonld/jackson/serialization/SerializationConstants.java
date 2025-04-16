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
package cz.cvut.kbss.jsonld.jackson.serialization;

public class SerializationConstants {

    /**
     * Configuration parameter indicating in what form the serialization output should be.
     */
    public static final String FORM = "form";

    /**
     * Compact form of the JSON-LD document.
     * <p>
     * This form is without context and uses full IRIs of mapped properties as terms in the resulting JSON-LD object.
     * <p>
     * Used as a value of the {@link #FORM} configuration parameter.
     */
    public static final String FORM_COMPACT = "compact";

    /**
     * Compact form with context.
     * <p>
     * This form creates a context where field names are used as terms in the resulting JSON-LD object and are mapped to
     * IRIs (or more complex term definitions) in the context.
     * <p>
     * Used as a value of the {@link #FORM} configuration parameter.
     */
    public static final String FORM_COMPACT_WITH_CONTEXT = "compact-with-context";

    private SerializationConstants() {
        throw new AssertionError();
    }
}
