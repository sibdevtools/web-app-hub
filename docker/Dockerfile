FROM --platform=$BUILDPLATFORM ghcr.io/graalvm/jdk-community:23.0.2 AS builder

WORKDIR /app

COPY build/install/web-app-hub-boot /app/web-app-hub

FROM ghcr.io/graalvm/jdk-community:23.0.2

LABEL maintainer="sibmaks" description="Project is Web Application Hub"

RUN groupadd --gid 1000 docuser && \
    useradd --uid 1000 --gid docuser --shell /bin/bash --create-home docuser

USER docuser

WORKDIR /home/docuser

COPY --from=builder /app/web-app-hub /home/docuser/web-app-hub

ENTRYPOINT ["./web-app-hub/bin/web-app-hub"]