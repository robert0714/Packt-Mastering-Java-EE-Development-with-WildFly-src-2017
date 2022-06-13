# Chapter 5: Working with Distributed Transactions
Maven Test:

```shell
mvn clean package test -pl business-components/transactions
```
WildFly provides a default authentication for the remote client through the `application-users.properties` and `application-roles.properties`.Its default account is `admin`, password is `admin`.
Wildfy password encryption algorithms always changed . You can see [document](https://docs.wildfly.org/26/WildFly_Elytron_Security.html).