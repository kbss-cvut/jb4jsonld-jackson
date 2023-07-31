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
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jsonld.jackson.environment.Vocabulary;

@JsonIgnoreProperties("subordinates")
@OWLClass(iri = Vocabulary.EMPLOYEE)
public class Employee extends User {

    @OWLObjectProperty(iri = Vocabulary.IS_MEMBER_OF)
    private Organization employer;

    @OWLDataProperty(iri = Vocabulary.EMPLOYEE_COUNT)
    private Integer subordinates;

    public Organization getEmployer() {
        return employer;
    }

    public void setEmployer(Organization employer) {
        this.employer = employer;
    }

    public Integer getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(Integer subordinates) {
        this.subordinates = subordinates;
    }
}
