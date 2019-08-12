FROM maven:3-jdk-8-alpine AS BUILD

RUN apk add --no-cache git

WORKDIR /usr/src/app

COPY . /usr/src/app

RUN mvn --batch-mode --errors --fail-fast \
      --define maven.javadoc.skip=true \
      --define skipTests=true install

ARG API_VERSION=1.0.0-SNAPSHOT
ENV API_VERSOON ${API_VERSION}

RUN apk add --no-cache git \
 && mkdir -p wps-routing-api/target/unpacked \
 && unzip -d wps-routing-api/target/unpacked \
      wps-routing-api/target/routing-api-${API_VERSION}.jar 

FROM openjdk:8-jdk-alpine

VOLUME /tmp

ARG API_VERSION=1.0.0-SNAPSHOT
ENV API_VERSOON ${API_VERSION}

COPY --from=BUILD /usr/src/app/wps-routing-api/target/unpacked/BOOT-INF/classes /app
COPY --from=BUILD /usr/src/app/wps-routing-api/target/unpacked/BOOT-INF/lib     /app/lib
COPY --from=BUILD /usr/src/app/wps-routing-api/target/unpacked/META-INF         /app/META-INF


ENV SERVER_PORT=8000
ENV SPRING_DATA_MONGODB_HOST=
ENV SPRING_DATA_MONGODB_PORT=
ENV ROUTING_ENDPOINT=
ENV ROUTING_PROCESS=

CMD [ "java", "-cp", "app:app/lib/*", "org.n52.testbed.routing.Application" ]