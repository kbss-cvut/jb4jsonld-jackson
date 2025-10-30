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

import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jsonld.annotation.JsonLdType;
import cz.cvut.kbss.jsonld.jackson.environment.Vocabulary;
import java.net.URI;
import java.util.List;

@JsonLdType(iri = Vocabulary.COMPANY)
public class Company extends AbstractCompany {

	@OWLObjectProperty(iri = Vocabulary.COMPANY_USERS)
	private List<CompanyUser> employees;

	public Company() {
	}

	public Company(URI uri) {
		super(uri);
	}

	public Company(URI uri, List<CompanyUser> employees) {
		super(uri);
		this.employees = employees;
	}


	public List<CompanyUser> getEmployees() {
		return employees;
	}

	public void setEmployees(List<CompanyUser> employees) {
		this.employees = employees;
	}
}
