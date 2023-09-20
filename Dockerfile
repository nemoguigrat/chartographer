FROM maven:3.8-adoptopenjdk-11 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=/usr/src/app/target/chartographer-1.0.0.jar
COPY --from=build ${JAR_FILE} /usr/app/app.jar
RUN mkdir /usr/app/media
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/app.jar", "/usr/app/media"]