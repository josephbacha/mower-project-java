FROM gradle:7.6-jdk17-alpine AS build
COPY --chown=gradle:gradle ./ /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle build

FROM openjdk:17-alpine

EXPOSE 8080:8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/mower-project-0.0.1-SNAPSHOT.jar /data/mower-project-0.0.1-SNAPSHOT.jar

WORKDIR /data

CMD ["gradle", "test"]

ENTRYPOINT ["java","-jar","mower-project-0.0.1-SNAPSHOT.jar"]
