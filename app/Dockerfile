FROM eclipse-temurin:21 as builder

# Wget is needed to bootstrap SBT
RUN apt update && apt install wget

WORKDIR /app

# bootstrap SBT
RUN wget https://raw.githubusercontent.com/sbt/sbt/1.10.x/sbt && chmod +x ./sbt
# we only copy the single file that affects which version of SBT gets bootstrapped
# This allows us to cache these steps if you don't change SBT version
COPY project/build.properties project/build.properties
# Start SBT with a dummy command so that it can pre-fetch all the dependencies required
RUN ./sbt --sbt-create version

# build app
# We copy all the application sources, so the building step will 
# run if there are any modifications
COPY build.sbt build.sbt
COPY project/plugins.sbt project/plugins.sbt
COPY App.scala App.scala
COPY smithy smithy
RUN ./sbt stage

# this is the start of the runtime container - one we will actually ship to AWS
FROM ghcr.io/graalvm/jdk-community:22
RUN mkdir -p /opt
# copy just the built app folder into this container - it doesn't need SBT to run,
# only JVM
COPY --from=builder /app/target/universal/stage /opt/app

ENV SMITHY4S_PORT=80

EXPOSE 80

# run the app
CMD ["/opt/app/bin/app"]
