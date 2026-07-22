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

    public static final String NAMESPACE = "http://onto.fel.cvut.cz/ontologies/jb4jsonld/";

    public static final String PERSON = "http://onto.fel.cvut.cz/ontologies/ufo/Person";
    public static final String USER = NAMESPACE + "User";
    public static final String EMPLOYEE = NAMESPACE + "Employee";
    public static final String ORGANIZATION = NAMESPACE + "Organization";

    public static final String FIRST_NAME = "http://xmlns.com/foaf/0.1/firstName";
    public static final String LAST_NAME = "http://xmlns.com/foaf/0.1/lastName";
    public static final String USERNAME = "http://xmlns.com/foaf/0.1/accountName";
    public static final String DATE_CREATED = "http://purl.org/dc/terms/created";
    public static final String IS_MEMBER_OF = NAMESPACE + "isMemberOf";
    public static final String HAS_MEMBER = NAMESPACE + "hasMember";
    public static final String BRAND = NAMESPACE + "brand";
    public static final String IS_ADMIN = NAMESPACE + "isAdmin";
    public static final String PASSWORD = NAMESPACE + "password";
    public static final String EMPLOYEE_COUNT = NAMESPACE + "employeeCount";
    public static final String SALARY = NAMESPACE + "salary";
	public static final String COMPANY = NAMESPACE + "Company";
	public static final String COMPANY_USERS = NAMESPACE + "companyUsers";
	public static final String COMPANY_USER = NAMESPACE + "CompanyUser";

    private Vocabulary() {
        throw new AssertionError();
    }
}
