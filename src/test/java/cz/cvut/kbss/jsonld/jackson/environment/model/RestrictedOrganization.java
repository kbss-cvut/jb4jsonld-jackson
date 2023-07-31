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
