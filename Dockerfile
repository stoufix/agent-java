# Multi-stage Dockerfile (sans build Maven, JAR déjà présent)
FROM amazoncorretto:25-al2023 AS base
ENV TZ=Europe/Amsterdam

# Créer un utilisateur non-root (UID 1000) et préparer le dossier d'exécution
ARG APP_USER=aboutme
ARG APP_UID=1000
RUN set -eux; \
    groupadd -g ${APP_UID} ${APP_USER} || true; \
    useradd -u ${APP_UID} -g ${APP_UID} -m -s /sbin/nologin ${APP_USER} || true; \
    mkdir -p /home/agent-java && chown -R ${APP_UID}:${APP_UID} /home/agent-java

WORKDIR /home/agent-java

FROM base AS final

# Copier le JAR déjà construit depuis le contexte local (chemin correct: ./target/...)
COPY target/aboutme-agent.jar /home/app.jar

# Permissions restrictives pour le JAR
RUN chown ${APP_UID}:${APP_UID} /home/app.jar && \
    chmod 750 /home/app.jar

EXPOSE 8080

# Exécuter en tant qu'utilisateur non-root
USER ${APP_USER}

ENTRYPOINT ["java", "-jar", "/home/agent-java/app.jar"]
