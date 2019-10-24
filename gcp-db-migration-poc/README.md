# gcp-db-migration-poc
POC on google cloud for database migration.

## Configuration
Configure application.properties file as per requirement.

## CURL
curl -X POST \
  http://localhost:8080/migrate/mysql/sql_dump \
  -H 'Authorization: "N/A"' \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: 8d0d6710-514f-4d6d-8153-74ab9ffb602c,6c7026ae-210b-4b0b-bbb9-082ab383f6a6' \
  -H 'cache-control: no-cache,no-cache'
