# SibDevTools - Web Applications Hub

Web applications hub - provide access for applications in one place.

## Web Applications

* [JOLT Transformer](https://github.com/sibdevtools/web-app-jolt) - provide JOLT sandbox
* [Base64](https://github.com/sibdevtools/web-app-base64) - base64 encoder/decoder
* [Bytes2PNG](https://github.com/sibdevtools/web-app-bytes2png) - bytes <-> PNG converter
* [Mocks](https://github.com/sibdevtools/web-app-mocks) - http mocks server
* [Settings](https://github.com/sibdevtools/web-app-settings) - ui settings

## To build

Run shell command

```shell
chmod +x gradlew
./gradlew clean build
```

## To start

Required at least `JDK 21`

### From sources

```shell
 ./gradlew bootRun
```

### Compiled Jar

```shell
java -jar web-app-hub-*.jar
```

### Released distributive

```shell
./bin/web-app-hub
```

### Docker

Start latest with default configuration:

```shell
docker compose -f ../docker/docker-compose-local.yaml up -d
```