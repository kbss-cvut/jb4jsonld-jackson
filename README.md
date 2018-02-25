# Java Binding for JSON-LD - Jackson

[![Build Status](https://kbss.felk.cvut.cz/jenkins/buildStatus/icon?job=jaxb-jsonld-jackson)](https://kbss.felk.cvut.cz/jenkins/job/jaxb-jsonld-jackson)

Java Binding for JSON-LD - Jackson (JB4JSON-LD-Jackson) is a binding of JB4JSON-LD for [Jackson](https://github.com/FasterXML/jackson).

The core implementation of JB4JSON-LD with a mapping example can be found at [https://github.com/kbss-cvut/jb4jsonld](https://github.com/kbss-cvut/jb4jsonld).

More info can be found at [https://kbss.felk.cvut.cz/web/kbss/jb4json-ld](https://kbss.felk.cvut.cz/web/kbss/jb4json-ld).

## Usage

JB4JSON-LD is based on annotations from [JOPA](https://github.com/kbss-cvut/jopa), which enable POJO attributes
to be mapped to ontological constructs (i.e. to object, data or annotation properties) and Java classes to ontological
classes.

Use `@OWLDataProperty` to annotate data fields and `@OWLObjectProperty` to annotate fields referencing other mapped entities.

To integrate the library with Jackson, register a `cz.cvut.kbss.jsonld.jackson.JsonLdModule` in Jackson's `ObjectMapper` like this:

`objectMapper.registerModule(new JsonLdModule())`

and you should be good to go. See the `JsonLdSerializionTest` for examples.

See [https://github.com/kbss-cvut/jopa-examples/tree/master/jsonld](https://github.com/kbss-cvut/jopa-examples/tree/master/jsonld) for
an executable example of JB4JSON-LD-Jackson in action.

## Serialization

The serializer's output has been verified to be a valid JSON-LD and is parseable by Java's JSON-LD reference implementation 
[jsonld-java](https://github.com/jsonld-java/jsonld-java).

The output is basically a context-less compacted JSON-LD, which uses full IRIs for attribute names.

## Deserialization

Since we are using jsonld-java to first process the incoming JSON-LD, it does not matter in which form (expanded, framed, flattened) the
input is.

## Getting JB4JSON-LD-Jackson

There are two ways to get JB4JSON-LD-Jackson:

* Clone repository/download zip and build it with Maven,
* Use a [Maven dependency](http://search.maven.org/#search%7Cga%7C1%7Ccz.cvut.kbss.jsonld):

```XML
<dependency>
    <groupId>cz.cvut.kbss.jsonld</groupId>
    <artifactId>jb4jsonld-jackson</artifactId>
</dependency>
```
