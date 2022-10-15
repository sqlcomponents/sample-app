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

1. set environment variables as in sample-app\src\main\res\.m2\.env
2. docker-compose up -d, it would create Database and Tables as per sample-app\init.db\mysql\init.sql, with credentials as in sample-app\src\main\resources\application.yml
   if any connection errors, it means init.sql has not run and tables are not created, try running manually
```
3. mvn --s src/main/res/.m2/settings.xml clean package 
```
or (for debug mode)
```
mvn --s src/main/res/.m2/settings.xml -X clean package 
```
```
mvn --s src/main/res/.m2/settings.xml help:active-profiles
```
```
mvn --settings src/main/res/.m2/settings.xml dependency:get -Dartifact=org.sqlcomponents:sqlcomponents-maven-plugin:1.0-SNAPSHOT
```
sample-app\src\main\res\.m2\privateaccesstoken.png