# Sample Application using SQL Components

Sample Application using SQL Components using mysql as the database. We will use movie database as sample database.
Once we run this app, SQL Components will generate persistence for the given database (init.db/postgres/movie.sql).

## Setup

We need to start the mysql db and initialize database.

```
docker-compose up -d
```

We need to configure maven to pull artifacts from github packages

https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry

github repo: https://maven.pkg.github.com/sqlcomponents/sqlcomponents
```
mvn --s src/main/res/.m2/settings.xml clean package 
```
```
mvn --s src/main/res/.m2/settings.xml -X clean package 
```
```
mvn --s src/main/res/.m2/settings.xml help:active-profiles
```
```
mvn --settings src/main/res/.m2/settings.xml dependency:get -Dartifact=org.sqlcomponents:sqlcomponents-maven-plugin:1.0-SNAPSHOT
```
