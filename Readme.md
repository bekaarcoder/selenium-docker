## Running Tests Inside Docker Container

### Maven Plugins

- **maven-dependency-plugin** - This will copy all the project dependencies to the specified directory.
- **maven-jar-plugin** - This will create the jar file for the project.
- **maven-resources-plugin** - This will copy all the resources to specified directory

### Packaging Java Application

To package the jar and skip all the tests. 
```bash
mvn clean package -DskipTests
```
In our case, all the dependencies jar and application jar file will be moved to `target/docker-resources/libs` directory.

### Moving other files present in the root directory of the project

Specify the files in the resource tag under `maven-resources-plugin` inside `pom.xml`.
```xml
<resource>
    <directory>.</directory>
    <includes>
        <include>dummy.txt</include>
    </includes>
</resource>
```

## Running Tests Via Command line

### TestNG Parameters

- Running a test suite.
    ```bash
    java -cp 'libs/*' org.testng.TestNG test-suites/vendor-portal.xml
    ```
  
- Running a test suite with specific thread count.
    ```bash
    java -cp 'libs/*' org.testng.TestNG -threadcount 2 test-suites/vendor-portal.xml
    ```
  This will override the thread-count mentioned in the `xml` file.

- TestNG by default created `test-output` directory. You can change it with `-d` option.
    ```bash
    java -cp 'libs/*' org.testng.TestNG -d result test-suites/vendor-portal.xml
    ```
  
### Passing System Parameters

- To pass the browser option.
    ```bash
    java -Dbrowser=chrome -cp 'libs/*' org.testng.TestNG test-suites/flight-reservation.xml
    ```
  
- To run the test using selenium grid.
    ```bash
    java -Dselenium.grid.enabled=true -Dselenium.grid.hubHost=localhost -cp 'libs/*' org.testng.TestNG test-suites/flight-reservation.xml
    ```

### Creating Docker Image

```dockerfile
FROM bellsoft/liberica-openjdk-alpine:17.0.10

# create a workspace
WORKDIR /home/selenium-docker

# Add required files
COPY target/docker-resources ./
```

**Build the Docker Image**

```bash
docker build -t blitzstriker/selenium .
```

**Create a docker container**

```bash
docker run -it -v ${PWD}/result:/home/selenium-docker/test-output blitzstriker/selenium
```

**Run tests inside container**

```bash
java -Dselenium.grid.enabled=true -Dselenium.grid.hubHost=<System_ip> -cp 'libs/*' org.testng.TestNG test-suites/vendor-portal.xml
```

### Adding Entrypoint to Dockerfile

```dockerfile
FROM bellsoft/liberica-openjdk-alpine:17.0.10

# create a workspace
WORKDIR /home/selenium-docker

# Add required files
COPY target/docker-resources ./

# Environment Variables
# BROWSER
# HUB_HOST
# TEST_SUITE
# THREAD_COUNT

# Run the tests
ENTRYPOINT java -cp 'libs/*' \
            -Dselenium.grid.enabled=true -Dselenium.grid.hubHost=${HUB_HOST} -Dbrowser=${BROWSER} \
            org.testng.TestNG -threadcount ${THREAD_COUNT} \
            test-suites/${TEST_SUITE}
```

### Running Tests Using Environment Variables

```bash
docker run -e BROWSER=chrome -e HUB_HOST=<system_ip> -e TEST_SUITE=vendor-portal.xml -e THREAD_COUNT=3 blitzstriker/selenium
```

**Remove unused images and containers**
```bash
docker system prune -f
```

## Create Entire Infrastructure Using Docker Compose

Add the below service in `docker-compose.yml` file.

```yaml
  vendor-portal:
    image: blitzstriker/selenium-java-docker
    depends_on:
      - chrome
    environment:
      - BROWSER=chrome
      - HUB_HOST=hub
      - THREAD_COUNT=3
      - TEST_SUITE=vendor-portal.xml
    volumes:
      - ./output/vendor-portal:/home/selenium-docker/test-output
```

**Checking Selenium Grid Status**

```bash
curl -s http://localhost:4444/status | jq .value.ready
```

**Adding curl and jq to docker image**

Update the `Dockerfile`

```dockerfile
FROM bellsoft/liberica-openjdk-alpine:17.0.10

# Install curl and jq
RUN apk add curl jq

...
...
```

We will create a `runner.sh` file which will check for the Selenium Grid status to be ready. Once it is ready,
it will run the tests.

Update the `Dockerfile` as below:
```dockerfile
FROM bellsoft/liberica-openjdk-alpine:17.0.10

# Install curl and jq
RUN apk add curl jq

# create a workspace
WORKDIR /home/selenium-docker

# Add required files
COPY target/docker-resources ./
COPY runner.sh runner.sh

# Start the runner.sh
ENTRYPOINT sh runner.sh
```

Build the docker image and run the test with docker compose

```bash
docker compose up -d 
```

**Summary**

1. Package the application
    ```bash
    mvn clean package -DskipTests
    ```

2. Build docker image
    ```bash
    docker build -t blitzstriker/selenium .
    ```
   
3. Run docker container
    ```bash
    docker compose up
    ```
