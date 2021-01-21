# Step 1: build the native image
FROM oracle/graalvm-ce:1.0.0-rc15 as graalvm
COPY . /home/app
WORKDIR /home/app

# Download and install Maven
ARG USER_HOME_DIR="/root"
ARG SHA=b4880fb7a3d81edd190a029440cdf17f308621af68475a4fe976296e71ff4a4b546dd6d8a58aaafba334d309cc11e638c52808a4b0e818fc0fd544226d952544

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && curl -fsSL -o /tmp/apache-maven.tar.gz https://apache.osuosl.org/maven/maven-3/3.6.1/binaries/apache-maven-3.6.1-bin.tar.gz \
  && echo "${SHA}  /tmp/apache-maven.tar.gz" | sha512sum -c - \
  && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
  && rm -f /tmp/apache-maven.tar.gz \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven
ENV GRAALVM_HOME $JAVA_HOME
RUN $MAVEN_HOME/bin/mvn clean package -Pnative

# Step 2: build the running container
FROM registry.fedoraproject.org/fedora-minimal
WORKDIR /work/
COPY --from=graalvm /home/app/target/*-runner /work/application
RUN chmod 775 /work
EXPOSE 8080
ENTRYPOINT ["./application", "-Dquarkus.http.host=0.0.0.0"]
