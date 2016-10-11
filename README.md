# JAXB - JSON-LD for Jackson

JAXB-JSON-LD for Jackson is a binding of JAXB-JSON-LD for [Jackson](https://github.com/FasterXML/jackson).

The core implementation of JAXB-JSON-LD can be found at [https://github.com/kbss-cvut/jaxb-jsonld](https://github.com/kbss-cvut/jaxb-jsonld).

More info can be found at [https://kbss.felk.cvut.cz/web/portal/jaxb-jsonld](https://kbss.felk.cvut.cz/web/portal/jaxb-jsonld).

## Usage

JAXB-JSON-LD is based on annotations from [JOPA](https://github.com/kbss-cvut/jopa), which enable POJO attributes
to be mapped to ontological constructs (i.e. to object, data or annotation properties) and Java classes to ontological
classes.

Use `@OWLDataProperty` to annotate data fields and `@OWLObjectProperty` to annotate fields referencing other mapped entities.

To integrate the library with Jackson, register a `cz.cvut.kbss.jsonld.jackson.JsonLdModule` in Jackson's `ObjectMapper` like this:

`objectMapper.registerModule(new JsonLdModule())`

and you should be good to go. See the `JsonLdSerializionTest` for examples.

See [https://github.com/kbss-cvut/jopa-examples/tree/master/jsonld](https://github.com/kbss-cvut/jopa-examples/tree/master/jsonld) for
an executable example of JAXB JSON-LD for Jackson in action.

## Serialization

The serializer's output has been verified to be a valid JSON-LD and is parsable by Java's JSON-LD reference implementation 
[jsonld-java](https://github.com/jsonld-java/jsonld-java).

The output is basically a context-less compacted JSON-LD, which uses full IRIs for attribute names.

## Deserialization

Since we are using jsonld-java to first process the incoming JSON-LD, it does not matter in which form (expanded, framed, flattened) the
input is.

## Getting JAXB-JSON-LD

There are two ways to get JAXB-JSON-LD for Jackson:

* Clone repository/download zip and build it with maven
* Use a Maven dependency from our maven repo at [http://kbss.felk.cvut.cz/m2repo/](http://kbss.felk.cvut.cz/m2repo/)
