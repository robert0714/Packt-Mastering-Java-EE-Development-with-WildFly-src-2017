# Chapter 13: Working with Servlets and JSP
Maven Test:

```shell
mvn install -pl web-programming/packresources
mvn install -pl web-programming -DSkipTests
mvn clean package test -pl web-programming/servlets-jsp
```
WildFly provides a default authentication for the remote client through the `application-users.properties` and `application-roles.properties`.Its default account is `admin`, password is `admin`.
Wildfy password encryption algorithms always changed . You can see [document](https://docs.wildfly.org/26/WildFly_Elytron_Security.html).