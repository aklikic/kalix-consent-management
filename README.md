# Consent management Kalix project

## Create project docker image
Create project uber jar
```
mvn package
```
Copy uber jar to `docker` folder:
```
cp target/consent-management-1.0-SNAPSHOT.jar docker/maven
```
```
cd docker
```
Build docker image:
```
docker build -t consent-management .
```
Run docker-compose:

## Run
### Run Kalix proxy docker compose
```
docker-compose up
```
### Run Kalix project docker compose