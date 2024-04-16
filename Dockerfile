FROM bellsoft/liberica-openjdk-alpine:17.0.10

# Install curl and jq
RUN apk add curl jq

# create a workspace
WORKDIR /home/selenium-docker

# Add required files
COPY target/docker-resources ./
COPY runner.sh runner.sh

# Environment Variables
# BROWSER
# HUB_HOST
# TEST_SUITE
# THREAD_COUNT

# Run the tests
#ENTRYPOINT java -cp 'libs/*' \
#            -Dselenium.grid.enabled=true -Dselenium.grid.hubHost=${HUB_HOST} -Dbrowser=${BROWSER} \
#            org.testng.TestNG -threadcount ${THREAD_COUNT} \
#            test-suites/${TEST_SUITE}

# Start the runner.sh
ENTRYPOINT sh runner.sh
