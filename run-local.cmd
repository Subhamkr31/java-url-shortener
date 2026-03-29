@echo off
cd /d "%~dp0"
set "MONGO_URI=mongodb://127.0.0.1:27017/url-shortener-db"
set "REDIS_URL=redis://127.0.0.1:6379"
set "SERVER_PORT=8083"
call mvnw.cmd spring-boot:run
