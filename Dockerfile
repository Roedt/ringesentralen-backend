# Step 1: Build the code using regular maven
FROM maven:3.8.6-jdk-11 as maven
ARG DBUSER
ARG DBPASSWORD
ARG QuarkusMailerUsername
ARG QuarkusMailerPassword
COPY pom.xml /home/app/
WORKDIR /home/app
RUN mvn verify -B --fail-never
COPY src /home/app/src
RUN mvn package -Dquarkus.package.type=native-sources -B -e -Dquarkus.datasource.username=${DBUSER} -Dquarkus.datasource.password=${DBPASSWORD} -Dquarkus.mailer.username=${QuarkusMailerUsername} -Dquarkus.mailer.password=${QuarkusMailerPassword}

# Stage 2: Build the native image
FROM quay.io/quarkus/ubi-quarkus-mandrel-builder-image:22.2-java17 AS native-build
COPY --chown=quarkus:quarkus --from=maven /home/app/target/native-sources /build
USER quarkus
WORKDIR /build
RUN native-image $(cat native-image.args) -J-Xmx20g
COPY --chown=quarkus:quarkus --from=maven /home/app/src/main/resources/META-INF/resources/publickey.pem /build/publickey.pem

# Stage 3: Create the docker final image
FROM registry.access.redhat.com/ubi8/ubi-minimal:8.6
WORKDIR /work/
COPY --from=native-build /build/*-runner /work/application
COPY --from=native-build /build/publickey.pem /work/publickey.pem
EXPOSE 8080
ENTRYPOINT [ "/work/application" ]
CMD ["./application", "-Dquarkus.http.host=0.0.0.0", "-Dmp.jwt.verify.publickey.location=/work/publickey.pem"]