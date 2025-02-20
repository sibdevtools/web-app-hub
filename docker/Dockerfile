FROM openjdk:23-jdk
LABEL maintainer=sibmaks; description="Project is Web Application Hub"

RUN groupadd --gid 1000 docuser && useradd --uid 1000 --gid docuser --shell /bin/bash --create-home docuser
USER docuser

WORKDIR /home/docuser

COPY build/install/web-app-hub-boot web-app-hub

ENTRYPOINT ["./web-app-hub/bin/web-app-hub"]
