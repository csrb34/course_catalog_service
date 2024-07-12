# Course catalog service resources
https://github.com/dilipsundarraj1/kotlin-springboot

# Docker and running tests with containers
* https://github.com/abiosoft/colima
* https://docs.docker.com/reference/cli/docker/compose/

#### Postgres DB container
```bash
docker-compose up
```
```bash
docker-compose up -d
```
```bash
docker-compose down
```
## Running integration tests
* https://testcontainers.com/getting-started/
* https://github.com/dilipsundarraj1/kotlin-springboot/tree/postgres?tab=readme-ov-file#integration-test-using-testcontainers


### Setting up symlink so testcontainers have permissions to create containers in colima context
[Github issue with solution](https://github.com/testcontainers/testcontainers-java/issues/6904)

#### Check contexts to find the file to link, in this case context is _unix:///Users/carla.s/.colima/default/docker.sock_ 
```bash
docker context show
docker context ls
```

#### Create symlink to colima context and restart
```bash
sudo ln -s $HOME/.colima/default/docker.sock /var/run/docker.sock
ls -la /var/run/docker.sock
colima restart
```

#### Check docker is running and there isn't any container up
```bash
docker ps
```
After this integration tests work.

And a _testcontainer_ properties file should exist
```bash
cat ~/.testcontainers.properties
```

#### Remove symlink, just in case current link is not longer valid
```bash
rm /var/run/docker.sock
```


# SpringBoot and Kotlin docs links
* https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html#jpa.query-methods.query-creation
* https://kotlinlang.org/docs/home.html
* https://www.baeldung.com/spring-autowire
* https://kotlinlang.org/docs/scope-functions.html#function-selection
* https://kotlinlang.org/docs/sequences.html