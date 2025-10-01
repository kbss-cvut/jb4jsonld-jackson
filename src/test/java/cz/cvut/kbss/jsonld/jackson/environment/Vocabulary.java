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
package cz.cvut.kbss.jsonld.jackson.environment;

public class Vocabulary {

    public static final String PERSON = "http://onto.fel.cvut.cz/ontologies/ufo/Person";
    public static final String USER = "http://krizik.felk.cvut.cz/ontologies/jb4jsonld/User";
    public static final String EMPLOYEE = "http://krizik.felk.cvut.cz/ontologies/jb4jsonld/Employee";
    public static final String ORGANIZATION = "http://krizik.felk.cvut.cz/ontologies/jb4jsonld/Organization";
	public static final String COMPANY = "http://krizik.felk.cvut.cz/ontologies/jb4jsonld/Company";
	public static final String COMPANY_USERS = "http://krizik.felk.cvut.cz/ontologies/jb4jsonld/companyUsers";
	public static final String COMPANY_USER = "http://krizik.felk.cvut.cz/ontologies/jb4jsonld/CompanyUser";

    public static final String FIRST_NAME = "http://xmlns.com/foaf/0.1/firstName";
    public static final String LAST_NAME = "http://xmlns.com/foaf/0.1/lastName";
    public static final String USERNAME = "http://xmlns.com/foaf/0.1/accountName";
    public static final String DATE_CREATED = "http://purl.org/dc/terms/created";
    public static final String IS_MEMBER_OF = "http://krizik.felk.cvut.cz/ontologies/jb4jsonld/isMemberOf";
    public static final String HAS_MEMBER = "http://krizik.felk.cvut.cz/ontologies/jb4jsonld/hasMember";
    public static final String BRAND = "http://krizik.felk.cvut.cz/ontologies/jb4jsonld/brand";
    public static final String IS_ADMIN = "http://krizik.felk.cvut.cz/ontologies/jb4jsonld/isAdmin";
    public static final String PASSWORD = "http://krizik.felk.cvut.cz/ontologies/jb4jsonld/password";
    public static final String EMPLOYEE_COUNT = "http://krizik.felk.cvut.cz/ontologies/jb4jsonld/employeeCount";
    public static final String SALARY = "http://krizik.felk.cvut.cz/ontologies/jb4jsonld/salary";

    private Vocabulary() {
        throw new AssertionError();
    }
}
