# Consent management Kalix project

Instructions how to build a Kalix SDK project uber jar, build and run in it docker container on Linux machine. 
# Developer machine
## Prerequisite
Java 17<br>
Apache Maven 3.6 or higher<br>
## Create Kalix SDK project uber jar
1. Create project uber jar
```
mvn package
```
2. Copy uber jar `consent-management-1.0-SNAPSHOT.jar` to `docker` folder

# Linux machine
## Prerequisite
Docker 20.10.8 or higher (client and daemon)
## Build Kalix SDK project docker image
1. Copy `docker` folder from Developer machine to Linux machine 
2. Build Kalix SDK project docker image:
```
cd docker
docker build -t consent-management .
```
## Run Kalix locally
```
cd docker
docker-compose up
```
## Test
Store fintech app as valid:
```
curl -XPOST -d '{ 
  "validTillTimestamp": "2023-11-16T20:00:00.000Z"
}' http://localhost:9000/fintechapp/1/valid -H "Content-Type: application/json"
```
Get fintech app state:
```
curl -XGET http://localhost:9000/fintechapp/1 -H "Content-Type: application/json"
```
Store fintech app as in-valid:
```
curl -XPOST  http://localhost:9000/fintechapp/1/invalid -H "Content-Type: application/json"
```