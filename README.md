# Sample Application using SQL Components
Sample Application using SQL Components using postgress as the database. We will use movie database as sample database. 
Once we run this app, SQL COmponents will generate persistance for the given database (init.db/postgres/movie.sql).

## Setup

We need to start the postgress db and intialize database.

```
docker-compose up -d
```

We need to configure maven to pull artifacts from github packages

https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry


<url>https://maven.pkg.github.com/sqlcomponents/sqlcomponents</url>

