# gcp-db-migration-poc
POC on google cloud for database migration.

## CURL

curl -X POST \
  'http://localhost:8080/migrate/mysql/sql_dump?bucket_name=my-first-bucket-1&path_to_dump_file=gs://mysql-dump-store-1/glerp_dump.sql&db_name=glerp&file_type=SQL' \
  -H 'Authorization: "N/A"' \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: f76cb2f1-2f57-4bf7-8b41-1797884366a4' \
  -H 'cache-control: no-cache' \
  -d '{"name" : "Please help me"}'
