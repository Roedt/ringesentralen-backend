# Ringesentralen

Backend for Ringesentralen.

## Utvikling
1. `docker-compose -f mysql.yaml down -v && docker-compose -f mysql.yaml up` (startar databasen frå tom tilstand, køyrer alle databaseskript og legg inn eksempelpersonar)
1. mvn compile quarkus:dev
1. No køyrer applikasjonen på port 8080. Swagger UI er tilgjengeleg på http://localhost:8080/swagger-ui, OpenAPI-definisjonar på http://localhost:8080/openapi
1. sql\toemRingeinfo.sql tilbakestiller registrerte samtalar, så du startar på scratch igjen

Punkt 1 startar databasen på port 3306, og admin-panelet for databasen på port 9080.

## Deploy til prod
Push til main-greina startar automatisk eit bygg på Google Cloud Build, som gjer mvn package, lager Docker-image og deployar det til Google Cloud Run

## Teknisk
Systemet er bygd i Kotlin, basert på appserveren Quarkus