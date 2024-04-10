FROM openjdk:17-jdk-alpine
RUN apk update && apk add --no-cache git maven
WORKDIR /projects
RUN git clone https://github.com/Tani-QA/users-load-hwb.git
WORKDIR /projects/users-load-hwb/users
RUN mvn clean package install
EXPOSE 8080 8778
CMD ["java", "-javaagent:jolokia-jvm-1.7.2.jar=port=8778,host=0.0.0.0", "-jar", "target/users-0.0.1-SNAPSHOT.jar"]