FROM quay.io/quarkus/ubi-quarkus-mandrel-builder-image:23.1-java21 AS native-build
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
RUN ./mvnw package -B -e -Dnative
COPY --chown=quarkus:quarkus src/main/resources/META-INF/resources/publickey.pem /code/publickey.pem

# Create the docker final image
FROM quay.io/quarkus/quarkus-micro-image:2.0
ARG DBUSER
ENV DBUSER=${DBUSER}
ARG DBPASSWORD
ENV DBPASSWORD=${DBPASSWORD}
ARG MAILUSER
ENV QuarkusMailerUsername=${MAILUSER}
ARG QuarkusMailerPassword=${MAILPASSWORD}
WORKDIR /work/
COPY --from=native-build /code/target/*-runner /work/application
COPY --from=native-build /code/publickey.pem /work/publickey.pem
RUN chmod 775 /work
USER nonroot
EXPOSE 8080
ENTRYPOINT [ "/work/application" ]
CMD ["./application", "-Dquarkus.http.host=0.0.0.0", "-Dquarkus.datasource.username=${DBUSER}","-Dquarkus.datasource.password=${DBPASSWORD}", "-Dquarkus.mailer.username=${QuarkusMailerUsername}", "-Dquarkus.mailer.password=${QuarkusMailerPassword}", "-Dmp.jwt.verify.publickey.location=/work/publickey.pem"]