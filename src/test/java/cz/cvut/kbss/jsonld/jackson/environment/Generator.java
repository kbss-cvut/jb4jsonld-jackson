/*
 * JB4JSON-LD Jackson
 * Copyright (C) 2023 Czech Technical University in Prague
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

import cz.cvut.kbss.jsonld.jackson.environment.model.Employee;
import cz.cvut.kbss.jsonld.jackson.environment.model.Organization;
import cz.cvut.kbss.jsonld.jackson.environment.model.User;

import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Generator {

    public static final String URI_BASE = "http://krizik.felk.cvut.cz/ontologies/jb4jsonld#";

    private static final Random RAND = new Random();

    private Generator() {
        throw new AssertionError();
    }

    /**
     * Returns a (pseudo)random positive integer between 1 (inclusive) and {@code max} (exclusive).
     *
     * @param max Upper bound
     * @return random integer
     */
    public static int randomCount(int max) {
        assert max > 1;
        int res;
        do {
            res = RAND.nextInt(max);
        } while (res < 1);
        return res;
    }

    public static boolean randomBoolean() {
        return RAND.nextBoolean();
    }

    public static User generateUser() {
        final User user = new User();
        setUserAttributes(user);
        return user;
    }

    public static Set<User> generateUsers() {
        final Set<User> users = new HashSet<>();
        for (int i = 0; i < randomCount(10); i++) {
            users.add(generateUser());
        }
        return users;
    }

    private static void setUserAttributes(User user) {
        final int number = RAND.nextInt();
        user.setUsername("user" + number);
        user.setFirstName("FirstName" + number);
        user.setLastName("LastName" + number);
        user.setAdmin(randomBoolean());
        user.setUri(generateUri());
    }

    public static URI generateUri() {
        return URI.create(URI_BASE + RAND.nextInt());
    }

    public static Employee generateEmployee() {
        final Employee employee = new Employee();
        setUserAttributes(employee);
        final Organization company = generateOrganization();
        employee.setEmployer(company);
        return employee;
    }

    public static Organization generateOrganization() {
        final Organization org = new Organization();
        org.setUri(generateUri());
        org.setDateCreated(new Date());
        org.setName("Organization" + RAND.nextInt());
        org.setBrands(new HashSet<>());
        for (int i = 0; i < randomCount(10); i++) {
            org.getBrands().add("Brandy" + i);
        }
        return org;
    }
}
