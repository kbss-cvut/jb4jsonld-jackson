# JB4JSON-LD Jackson Example

This application is a bare-bones example of how JB4JSON-LD and its integration with Jackson works. It is a command line
application that allows serialization and deserialization of sample data.

## Requirements

The demo requires:

- JDK 8 or later
- Apache Maven 3.3 or later

## How to Run

To run the demo, `mvn exec:java` can be used (note that `mvn package` needs to be run to build the example app first).

The following options can be used as program arguments to specify what the demo application should do.

- `su` - Serialize a single instance of class `User`
- `slu` - Serialize a list of instances of class `User`
- `so` - Serialize a single instance of class Organization having references to a set of instances of class `User` (with backward references)
- `du` - Deserialize a single instance of class `User`. The second argument can be used to specify path to a file containing JSON-LD to deserialize. If not provided, default sample data will be used.
- `do` - Deserialize a single instance of class `Organization`. The second argument can be used to specify path to a file containing JSON-LD to deserialize. If not provided, default sample data will be used.

For example:

`mvn package exec:java -Dexec.args=su` or `mvn package exec:java -Dexec.args="du user.json"`

## Jackson Integration

Jackson `ObjectMapper` is configured in the `Example` class, method `initObjectMapper`.
