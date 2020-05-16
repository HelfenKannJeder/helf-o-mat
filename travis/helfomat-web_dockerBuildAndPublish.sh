set -xe
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
PROJECT_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
BUILD_TAG=$PROJECT_VERSION-${TRAVIS_COMMIT:0:8}
echo "build and push image: $BUILD_TAG"
./mvnw package com.google.cloud.tools:jib-maven-plugin:build -Phelfomat-web,azure -DskipTests -D image=helfenkannjeder/helfomat-web:$BUILD_TAG -pl helfomat-web -am
./mvnw package com.google.cloud.tools:jib-maven-plugin:build -Phelfomat-import -DskipTests -D image=helfenkannjeder/helfomat-import:$BUILD_TAG -pl helfomat-import -am
./mvnw package -Phelfomat-web-ui,docker -DskipTests
docker build -t helfenkannjeder/helfomat-web-ui:$BUILD_TAG helfomat-web-ui/docker/
docker push helfenkannjeder/helfomat-web-ui:$BUILD_TAG