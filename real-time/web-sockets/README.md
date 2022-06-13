# Chapter 7: Implementing WebSockets
Maven Test:

```shell
mvn clean package test -pl real-time/web-sockets
```
WildFly provides a default authentication for the remote client through the `application-users.properties` and `application-roles.properties`.Its default account is `admin`, password is `admin`.
Wildfy password encryption algorithms always changed . You can see [document](https://docs.wildfly.org/26/WildFly_Elytron_Security.html).