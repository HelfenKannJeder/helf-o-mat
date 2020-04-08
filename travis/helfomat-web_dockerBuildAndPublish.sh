echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
PROJECT_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
echo "build and push image: $PROJECT_VERSION-${TRAVIS_COMMIT:0:8}"
./mvnw package com.google.cloud.tools:jib-maven-plugin:build -DskipTests -D image=helfenkannjeder/helfomat-web:$PROJECT_VERSION-${TRAVIS_COMMIT:0:8} -pl helfomat-web -am