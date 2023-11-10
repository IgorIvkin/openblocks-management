# build application
FROM amazoncorretto:21-alpine-jdk as build
COPY .mvn /home/app/.mvn
COPY src /home/app/src
COPY pom.xml /home/app/pom.xml
COPY mvnw /home/app/mvnw
WORKDIR /home/app
RUN ./mvnw package

# deploy application
FROM amazoncorretto:21-alpine-jdk
COPY --from=build /home/app/target/management-0.0.1-SNAPSHOT.jar /home/app/target/management.jar
RUN mkdir -p /home/app/filestorage
WORKDIR /home/app/target
EXPOSE 8080
ENV APP_COOKIE_DOMAIN "management.local"
ENV FILE_STORAGE_PATH "/home/app/filestorage/"
ENV DATASOURCE_URL "jdbc:postgresql://host.docker.internal:5432/management_db"
ENV DATASOURCE_USERNAME "management_techuser"
ENV DATASOURCE_PASSWORD "password1"
CMD ["java", "-jar", "management.jar"]