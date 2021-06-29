# Executable methods

## How to run

Build and run application

```shell
mvn clean package
java -jar target/cdi-exec-methods.jar
```

Execute endpoints
```shell
curl -i http://localhost:7001/resource1
curl -i http://localhost:7001/resource1?message=something
curl -i http://localhost:7001/resource2
curl -i http://localhost:7001/resource1?message=somethingElse
```

## jakarta.cdi.rest

Example scaled down framework for rest endpoints.

- `@GET` - get method annotation, annotated with Executable (this is ingored in this example)
- `@Path` - resource class annotation defining the URI
- `@QueryParam` - a qualifier to inject query parameters by name
- `@RemoteAddress` - a qualifier to inject remote address


## jakarta.cdi.context

- `@Executable` - meta annotation to mark annotations as triggering executable methods
- `ExecutableMethod` - extends `AnnotatedMethod` by adding the `invoke` method

## jakarta.cdi.inject.spi

- `ProcessExecutableMethod` - simple event to process the methods

## jakarta.cdi.sandbox

- `ExecutableMethodImpl` - POC implementation to invoke an actual method
- `RequestDataProducer` - produces information from the request to be injectable
- `RestExtension` - extension that processes the executable methods (now faked through `ProcessAnnotatedType`)
    The extension is a full implementation, that collects all get methods and exposes them through a web server
    using the executable method APIs
- `ScopeActivator` - to active request scope for inbound requests

## jakarta.cdi.example.app

- `ConfigBean` - application scoped bean using MP Config
- `Main` - just starts CDI (Weld is hidden under Helidon main class)
- `Resource1` and `Resource2` - rest endpoints showing injection of request scoped and application scoped

## Questions

- how to handle correct typing - can we return an object from the producer and type correctly based on
the injection point?