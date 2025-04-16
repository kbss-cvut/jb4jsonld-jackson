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
package cz.cvut.kbss.jsonld.jackson.environment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jsonld.jackson.environment.Vocabulary;

@JsonIgnoreProperties({"employees", "brands"})
@OWLClass(iri = Vocabulary.ORGANIZATION)
public class RestrictedOrganization extends Organization {

    public RestrictedOrganization() {
    }

    public RestrictedOrganization(Organization other) {
        setUri(other.getUri());
        setDateCreated(other.getDateCreated());
        setBrands(other.getBrands());
        setEmployees(other.getEmployees());
        setAge(other.getAge());
        setEmployeeCount(other.getEmployeeCount());
    }
}
