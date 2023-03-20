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
## Test
```
curl -XPOST -d '{ 
  "validTillTimestamp": "2023-11-16T20:00:00.000Z"
}' http://localhost:9000/fintechapp/1/valid -H "Content-Type: application/json"
```