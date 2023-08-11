FROM quay.io/quarkus/ubi-quarkus-mandrel-builder-image:22.3-java17 AS native-build
ARG DBUSER
ARG DBPASSWORD
ARG QuarkusMailerUsername
ARG QuarkusMailerPassword
COPY --chown=quarkus:quarkus mvnw /code/mvnw
COPY --chown=quarkus:quarkus .mvn /code/.mvn
COPY --chown=quarkus:quarkus pom.xml /code/
COPY --chown=quarkus:quarkus .editorconfig /code/.editorconfig
USER root
RUN chown -R quarkus:quarkus /code
USER quarkus
WORKDIR /code
RUN ./mvnw -B org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline
COPY src /code/src
RUN ./mvnw package -B -e -Dnative -Dquarkus.datasource.username=${DBUSER} -Dquarkus.datasource.password=${DBPASSWORD} -Dquarkus.mailer.username=${QuarkusMailerUsername} -Dquarkus.mailer.password=${QuarkusMailerPassword}
COPY --chown=quarkus:quarkus src/main/resources/META-INF/resources/publickey.pem /build/publickey.pem

# Create the docker final image
FROM quay.io/quarkus/quarkus-micro-image:2.0
WORKDIR /work/
COPY --from=native-build /code/target/*-runner /work/application
COPY --from=native-build /code/publickey.pem /work/publickey.pem
RUN chmod 775 /work
EXPOSE 8080
ENTRYPOINT [ "/work/application" ]
CMD ["./application", "-Dquarkus.http.host=0.0.0.0", "-Dmp.jwt.verify.publickey.location=/work/publickey.pem"]