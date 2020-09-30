# Ringesentralen

Backend for Ringesentralen.

## Utvikling
- mvn quarkus:dev

## Deploy til prod
Push til main-greina startar automatisk eit bygg på Google Cloud Build, som gjer mvn package, lager Docker-image og deployar det til Google Cloud Run

## Teknisk
Systemet er bygd i Kotlin, basert på appserveren Quarkus