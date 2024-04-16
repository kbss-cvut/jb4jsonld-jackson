/*
 * JB4JSON-LD Jackson
 * Copyright (C) 2024 Czech Technical University in Prague
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
package cz.cvut.kbss.jsonld.jackson.example;

import cz.cvut.kbss.jsonld.jackson.example.model.Organization;
import cz.cvut.kbss.jsonld.jackson.example.model.User;
import cz.cvut.kbss.jsonld.jackson.example.model.Vocabulary;

import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class Generator {

    private static final Random RAND = new Random();

    public static User generateUser() {
        final User user = new User();
        final int number = RAND.nextInt(10000);
        user.setUri(URI.create(Vocabulary.s_c_user + "/instance" + number));
        user.setFirstName("FirstName" + number);
        user.setLastName("LastName" + number);
        user.setEmailAddress(user.getFirstName() + user.getLastName() + "@example.org");
        user.setCreated(timestamp());
        return user;
    }

    private static Instant timestamp() {
        return Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public static Organization generateOrganization() {
        final Organization organization = new Organization();
        final int number = RAND.nextInt(10000);
        organization.setUri(URI.create(Vocabulary.s_c_organization + "/instance" + number));
        organization.setName("Organization" + number);
        organization.setEmailAddress(organization.getName() + "@example.org");
        organization.setDateCreated(timestamp());
        organization.setMembers(new HashSet<>(Arrays.asList(generateUser(), generateUser(), generateUser())));
        organization.getMembers().forEach(u -> u.setClinic(organization));
        return organization;
    }
}
