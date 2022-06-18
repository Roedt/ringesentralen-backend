# Step 0: Build the image
FROM maven:3.8.6-jdk-11 as maven
COPY . /home/app
WORKDIR /home/app
ARG DBUSER
ARG DBPASSWORD
ARG QuarkusMailerUsername
ARG QuarkusMailerPassword
RUN mvn package -Dquarkus.package.type=native-sources -B -e -Dquarkus.datasource.username=${DBUSER} -Dquarkus.datasource.password=${DBPASSWORD} -Dquarkus.mailer.username=${QuarkusMailerUsername} -Dquarkus.mailer.password=${QuarkusMailerPassword}

# Step 1: build the native image
FROM quay.io/quarkus/ubi-quarkus-mandrel:21.3-java11 AS native-build
COPY --chown=quarkus:quarkus --from=maven /home/app/target/native-sources /build
USER quarkus
WORKDIR /build
RUN native-image $(cat native-image.args) -J-Xmx6g

## Stage 2 : create the docker final image
FROM registry.access.redhat.com/ubi8/ubi-minimal:8.5
WORKDIR /work/
COPY --from=native-build /build/*-runner /work/application
EXPOSE 8080
ENTRYPOINT [ "/work/application" ]
CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]