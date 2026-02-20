FROM maven:3.9.9-sapmachine-21 as build_stage

#Change working directory to /app
WORKDIR /app

# Copy files to /app directory
COPY pom.xml ./
COPY . .

#Execute maven build
RUN mvn package

#Copy build from /app to /
RUN cp target/sample-app-backend*.jar /sample-app-backend.jar

#Change working directory to /
WORKDIR /

FROM eclipse-temurin:21-jre-alpine

# COPY compiled jar file to new image
COPY --from=build_stage sample-app-backend.jar .

# Deploy Build
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "sample-app-backend.jar"]
