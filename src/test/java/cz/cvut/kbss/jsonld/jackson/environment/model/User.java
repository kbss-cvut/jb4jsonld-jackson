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
package cz.cvut.kbss.jsonld.jackson.environment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.cvut.kbss.jopa.model.annotations.OWLAnnotationProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jsonld.jackson.environment.Vocabulary;

import java.net.URI;

@JsonIgnoreProperties("subordinates")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "javaClass")
@OWLClass(iri = Vocabulary.USER)
public class User extends Person {

    @OWLDataProperty(iri = Vocabulary.USERNAME)
    private String username;

    @OWLAnnotationProperty(iri = Vocabulary.IS_ADMIN)
    private Boolean admin;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OWLDataProperty(iri = Vocabulary.PASSWORD)
    private String password;

    @OWLDataProperty(iri = Vocabulary.EMPLOYEE_COUNT)
    private Integer subordinates;

    public User() {
    }

    public User(URI uri, String firstName, String lastName, String username, Boolean admin) {
        setUri(uri);
        setFirstName(firstName);
        setLastName(lastName);
        this.username = username;
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(Integer subordinates) {
        this.subordinates = subordinates;
    }
}
