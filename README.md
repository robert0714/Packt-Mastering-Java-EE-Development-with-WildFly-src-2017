# Wildfly Examples Jakarta EE 8 based and tested on Java 8 and Wildfly 26.0.0
Luca Stancapiano's books about:
* https://www.packtpub.com/product/mastering-java-ee-development-with-wildfly/9781787287174
* https://subscription.packtpub.com/book/web-development/9781787287174/pref

# Other Resources
* MDB: https://github.com/wildfly/wildfly/blob/main/testsuite/integration/basic/src/test/java/org/jboss/as/test/integration/ejb/mdb/deliveryactive/MDBTestCase.java


# Executing
```shell
 mvn clean package  -Dmaven.test.failure.ignore=true
```
# Dependencies Export
```shell
pache.maven.plugins:maven-dependency-plugin:3.3.0:copy-dependencies -Dmdep.useRepositoryLayout=true -Dmdep.copyPom=true -DoutputDirectory=D:\tmp\mvn_tmp
```