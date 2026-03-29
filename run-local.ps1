# Local dev: Mongo + Redis on localhost, app on port 8083
# If scripts are disabled, use: .\run-local.cmd
Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot

$env:MONGO_URI = "mongodb://127.0.0.1:27017/url-shortener-db"
$env:REDIS_URL = "redis://127.0.0.1:6379"
$env:SERVER_PORT = "8083"

& .\mvnw.cmd spring-boot:run
