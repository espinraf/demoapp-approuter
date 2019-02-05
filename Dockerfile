FROM java:openjdk-8-jdk

MAINTAINER rafael.espino@redpill-linpro.com

RUN cd /opt && mkdir apps

COPY ./build/libs/demoapp-approuter-0.0.1-SNAPSHOT.jar /opt/apps

RUN ls /opt/apps/*

# Define mount points.
VOLUME ["/opt/apps"]

# Define working directory.
WORKDIR /opt/apps

CMD [ "java", "-jar", "/opt/apps/demoapp-approuter-0.0.1-SNAPSHOT.jar"]

EXPOSE 8081
