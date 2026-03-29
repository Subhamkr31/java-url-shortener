# URL Shortener (Spring Boot + MongoDB)

A simple proof-of-concept (POC) URL shortener built with Spring Boot and MongoDB for learning purposes.

## Stack

- Java 21;
- Spring Boot;
- MongoDB;
- Docker compose;

## Endpoints

### Shorten URL

- **URL**: `/shorten`
- **Method**: `POST`
- **Request Body**:

```json
{
  "url": "https://example.com",
  "expiresAt": "2025-05-21T11:28:00" // Optional
}
```

- **Response**:

```json
{
  "shortUrl": "http://localhost:8080/abc123",
  "expiresAt": "2025-05-21T11:28:00" // Or null if not set
}
```

### Redirect Shortened URL

- **URL**: `/{id}`
- **Method**: `GET`
- **Response**: Redirects to the original URL.

## Running with Docker Compose

```bash
docker compose --profile production up --build -d
```

## Runing with Docker Swarm

**1. Create Docker secrets**

```bash
echo 8081 | docker secret create APP_PORT -
echo mongo | docker secret create MONGO_HOST -
echo 27017 | docker secret create MONGO_PORT -
echo root | docker secret create MONGO_USER -
echo password | docker secret create MONGO_PASSWORD -
echo url_shortener | docker secret create MONG0_DB -
echo cache | docker secret create REDIS_HOST -
echo 6379 | docker secret create REDIS_PORT -
```

**2. Run with Docker Swarm**

```bash
docker swarm init
```

**3. Deploy the stack**

```bash
docker stack deploy -c docker-swarm.yml url_shortener
```



## **Option A: Run locally with Maven**

From the project root (`d:\url-shortener-java\url-shortener-main`):

*# Optional: only if you don’t use the defaults in application.properties*

$env:MONGO_URI = "mongodb://127.0.0.1:27017/url-shortener-db"   *# or your Atlas / auth URI*

$env:REDIS_URL  = "redis://127.0.0.1:6379"                        *# or rediss://... for Upstash*

.\mvnw.cmd spring-boot:run

Then try `POST http://localhost:8082/shorten` with a JSON body like `{"url":"https://example.com"}`.

---

## **Option B: Docker Compose (app + Mongo + Redis)**

1. Copy `.env.example` to `.env` and adjust variables if needed.
2. Start the **production** profile (builds and runs the app with internal Mongo/Redis):

docker compose --profile production up --build -d

The compose file maps `${APP_PORT}:${APP_PORT}` for the app, so whatever `APP_PORT` is in `.env` is the host port (often **8080** in the example file). Inside the image, the app may still default to 8082 unless you set `SERVER_PORT` / `APP_PORT` consistently—check `.env` for `APP_PORT`.

If you only want Mongo and Redis locally and run the app on the host, bring up `mongo` and `cache` without the `production` profile (no `app` service in that compose file for non-production—actually looking at the compose, app has `profiles: production`. So for full stack you need `--profile production`.









### **Option 1 — Docker Compose (simplest: app + Mongo + Redis)**

1. From the project folder, copy env template and adjust if needed:
  copy .env.example .env
2. Start the **production** profile (builds the image from `Dockerfile` and runs all services):
  cd D:\url-shortener-java\url-shortener-main
  docker compose --profile production up --build -d
3. Call the API on `http://localhost:<APP_PORT>` (from `.env`, default `8080` after the compose fix for `SERVER_PORT`).
4. Logs / stop:
  docker compose --profile production logs -f app
  docker compose --profile production down

**Note:** `mongo` and `cache` are always defined; only `app`, `nginx`, etc. use the `production` profile in your file, so `--profile production` is required for the Spring app container.

---

### **Option 2 — Only Docker (**`docker build` **/** `docker run`**)**

Build:

cd D:\url-shortener-java\url-shortener-main

docker build -t url-shortener-app:v1 .

Run (you must supply **Mongo** and **Redis** yourself; replace hostnames if they run on the same machine):

docker run --rm -p 8080:8080 ^

-e SERVER_PORT=8080 ^

-e MONGO_URI=mongodb://host.docker.internal:27017/url-shortener-db ^

-e REDIS_URL=redis://host.docker.internal:6379 ^

  url-shortener-app:v1

On Linux, `host.docker.internal` may need `--add-host=host.docker.internal:host-gateway` (Docker 20.10+).











run command spring boot:-----     ./mvnw spring-boot:run

run command :-----      ./run-local.cmd
