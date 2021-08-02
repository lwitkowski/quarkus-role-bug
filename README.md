# quarkus-role-bug
This project is minimal reproducer for Quarkus 2.0.2.Final bug related to OIDC extension skipping permissions check for certain POST endpoints.

## Steps to reproduce:
### Given
Quarkus: v2.0.2.Final or newer
Extensions: `cdi, oidc, resteasy, resteasy-jackson, security, smallrye-context-propagation`
POST endpoint accepting payload as strongly-typed POJO **private**  class, with `@RolesAllowed("some-group")` annotation 

### When 
When endpoint is requested without valid JWT or when 'groups' claim does not contain required group (role)

### Then
Endpoint method/implementation is called and response is sent to the client despite missing authorization.

`./mvnw verify -Dquarkus.platform.version=2.0.2.Final`

## Expected behaviour (and current behaviour in v2.0.1.Final or older, including 1.X):
REST service returns http 401/403 and endpoint method/implementation is not called.  

`./mvnw clean verify`