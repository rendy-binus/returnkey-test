FROM alpine as build

ARG JDK_VERSION=11

RUN apk update && apk upgrade

RUN apk add openjdk${JDK_VERSION}

RUN java --version

WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)


FROM alpine

ARG JDK_VERSION=11

RUN apk update && apk upgrade

RUN apk add openjdk${JDK_VERSION}

RUN java --version

VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java", "-cp", "app:app/lib/*", "com.example.returnkeytest.ReturnkeyTestApplication"]