FROM eclipse-temurin:21 as builder

RUN apt update && apt install wget

WORKDIR /app

RUN wget https://raw.githubusercontent.com/sbt/sbt/1.10.x/sbt && chmod +x ./sbt

COPY build.sbt build.sbt
COPY project/plugins.sbt project/plugins.sbt
COPY project/build.properties project/build.properties
COPY App.scala App.scala

RUN ./sbt stage

FROM ghcr.io/graalvm/jdk-community:22
RUN mkdir -p /opt
COPY --from=builder /app/target/universal/stage /opt/app

ENV SMITHY4S_PORT=80

EXPOSE 80

CMD ["/opt/app/bin/app"]
