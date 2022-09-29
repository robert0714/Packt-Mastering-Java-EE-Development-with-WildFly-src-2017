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
mvn org.apache.maven.plugins:maven-dependency-plugin:3.2.0:copy-dependencies -Dmdep.useRepositoryLayout=true -Dmdep.copyPom=true -DoutputDirectory=D:\tmp\mvn_tmp
```

# Standalone.bat
```bat

set "JAVA_OPTS=%JAVA_OPTS% -DCONF_PATH=D:\home\weblogic\System.Property " 
set "JAVA_OPTS=%JAVA_OPTS% -Dserver.port=8080 -DMOrderLogFileLocation=D:\wildflyConfig\log4j.xml" 
set "JAVA_OPTS=%JAVA_OPTS% -Djboss.bind.address=0.0.0.0 " 
set "JAVA_OPTS=%JAVA_OPTS% -javaagent:D:\DATA\JAVA_program\program\server\wildfly\elastic-apm-agent-1.33.0.jar"
set "JAVA_OPTS=%JAVA_OPTS% -Delastic.apm.service_name=robertTest"
set "JAVA_OPTS=%JAVA_OPTS% -Delastic.apm.server_urls=http://192.168.50.92:8200"
set "JAVA_OPTS=%JAVA_OPTS% -Delastic.apm.enabled=true"
set "JAVA_OPTS=%JAVA_OPTS% -Delastic.apm.application_packages=com.cht.* "
set "JAVA_OPTS=%JAVA_OPTS% -Delastic.apm.trace_methods=com.cht.* "
set "JAVA_OPTS=%JAVA_OPTS% -Delastic.apm.stack_trace_limit=180 "
set "JAVA_OPTS=%JAVA_OPTS% -Delastic.apm.trace_methods_duration_threshold=100ms "
# set "JAVA_OPTS=%JAVA_OPTS% -Dfile.encoding=UTF8 "
# set "JAVA_OPTS=%JAVA_OPTS% -Dorg.apache.catalina.connector.URI_ENCODING=UTF-8"
# set "JAVA_OPTS=%JAVA_OPTS% -Dsun.jnu.encoding=UTF-8"


set "JAVA_OPTS=%JAVA_OPTS% -Dfile.encoding=MS950 "
set "JAVA_OPTS=%JAVA_OPTS% -Dorg.apache.catalina.connector.URI_ENCODING=MS950"
set "JAVA_OPTS=%JAVA_OPTS% -Dsun.jnu.encoding=MS950"
set "JAVA_OPTS=%JAVA_OPTS% -Dorg.apache.catalina.connector.USE_BODY_ENCODING_FOR_QUERY_STRING=true"

rem By default debug mode is disable.
```
