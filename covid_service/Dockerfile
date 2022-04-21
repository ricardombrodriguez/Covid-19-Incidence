FROM maven:latest

WORKDIR /backend

COPY . /backend/

RUN mvn -DskipTests clean package

ENTRYPOINT ["java","-jar","/backend/target/covid_service-0.0.1-SNAPSHOT.jar"]