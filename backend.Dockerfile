FROM maven:latest as build
WORKDIR /src
COPY api ./api
RUN mvn -f api/pom.xml -DskipTests clean package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /src/api/target/api.jar app.jar

### Uncomment to use ROOT-CA.crt in the container
# COPY ROOT-CA.crt .
# RUN keytool -import -trustcacerts -keystore /opt/java/openjdk/lib/security/cacerts -storepass changeit -noprompt -alias app -file /app/ROOT-CA.crt
### Uncomment to use ROOT-CA.crt in the container

ENTRYPOINT ["java", "-jar", "app.jar"]