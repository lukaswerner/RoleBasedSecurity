# RoleBasedSecurity

[![Build Status](https://travis-ci.org/lukaswerner/RoleBasedSecurity.svg?branch=master)](https://travis-ci.org/lukaswerner/RoleBasedSecurity)

This is a reference implementation of a role based security model.
It utilizes [Spring Boot](http://projects.spring.io/spring-boot/) and an embedded
[H2 database](http://www.h2database.com/html/main.html). You can start this application out-of-the-box.

At the moment this project is under development, what means, that you should
not yet use it in a productive environment.

The whole concept relies on [Spring Security](http://projects.spring.io/spring-security/).

## Dependencies
This is a [Maven](http://maven.apache.org/)-Project which means that you do not have to handle any dependencies
as Maven takes care about them.


## Build
- Using a terminal
 - Navigate to the root directory of this project
 - Execute `mvn install -DskipTests` to build jars
 - Now a jar-file was created, you can simply run it via
 'java -jar /target/role-base-security-X-Y.jar' where 'X' and 'Y' represent the version you are using
- Using a IDE
 - Switch to the default run configurations, if you changed them
 - Rightclick 'TestApp.java' at 'de.unileipzig.bis.rbs.testApp' and then 'Run TestApp.main()'

## Database
As mentioned before a H2 database is used. The automatically read database schema is located at
'root/src/main/resources/schema.sql' and creates 5 base tables which are necessary for this project.
- rbs_roles, which contains information about the roles used
- rbs_users, where basic user information is stored
- rbs_objects, here are global IDs for all objects, created by custom tables, stored
- rbs_users_roles, this table references between users and roles
- rbs_roles_objects, same as previous but with objects and contains additional information about the
rights a role has for an object


## Test
- Use your favorite tool to execute [JUnit](http://junit.org/junit4/) tests or
simply execute `mvn test` command to unit test the application's methods.


## Test App
The core functionality of this project is demonstrated with a test application which is basically a
Spring Web application using the provided API of this project.
Please consider, that the embedded database in this test app is an in-memory database,
which means, that you should not use important data in this app unless you change
the underlying data source to any persistant one.
As an example we added some test tables to the schema and some test data which is also read automatically
into the database.

## Custom expansions
If you want to add your own tables you simply have to have an 'AUTO_INCREMENT' integer 'id' column and
a suitable java class with [JPA](http://projects.spring.io/spring-data-jpa/) annotations which extends
our 'de.unileipzig.bis.rbs.testApp.model.DataObject' and sets the table name as discriminator value and the
primary key join column with 'id' as name.

## Release Notes

### v0.2 (2016-05-17)
- Created custom error page
- Changed in-memory login to database login
- Logout wasn't possible (fixed)
- Beautified test app
- Improved the database structure and test data
- Made users, roles and objects manageable
- Fixed Travis CI and Deployment

### v0.1 (2016-05-03)
- Created maven project
- Created basic login app
- Integrated [Travis CI](https://travis-ci.org/)
