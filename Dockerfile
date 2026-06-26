FROM eclipse-temurin:17-jdk AS build

WORKDIR /workspace

ARG KOTLIN_REPO_URL
ARG KOTLIN_VERSION
ARG KOTLIN_API_VERSION
ARG KOTLIN_LANGUAGE_VERSION
ARG KOTLIN_ADDITIONAL_CLI_OPTIONS

COPY gradlew settings.gradle.kts build.gradle.kts gradle.properties ./
COPY gradle ./gradle
COPY buildSrc ./buildSrc

RUN chmod +x ./gradlew

COPY src ./src

RUN set -eux; \
    set -- clean build --no-daemon; \
    if [ -n "${KOTLIN_REPO_URL:-}" ]; then set -- "$@" "-Pkotlin_repo_url=${KOTLIN_REPO_URL}"; fi; \
    if [ -n "${KOTLIN_VERSION:-}" ]; then set -- "$@" "-Pkotlin_version=${KOTLIN_VERSION}"; fi; \
    if [ -n "${KOTLIN_API_VERSION:-}" ]; then set -- "$@" "-Pkotlin_api_version=${KOTLIN_API_VERSION}"; fi; \
    if [ -n "${KOTLIN_LANGUAGE_VERSION:-}" ]; then set -- "$@" "-Pkotlin_language_version=${KOTLIN_LANGUAGE_VERSION}"; fi; \
    if [ -n "${KOTLIN_ADDITIONAL_CLI_OPTIONS:-}" ]; then set -- "$@" "-Pkotlin_additional_cli_options=${KOTLIN_ADDITIONAL_CLI_OPTIONS}"; fi; \
    ./gradlew "$@"
