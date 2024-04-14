FROM eclipse-temurin:21 as builder

# Wget is needed to bootstrap Scala CLI
RUN apt update && apt install -y wget make

WORKDIR /app

# bootstrap Scala CLI
RUN wget https://raw.githubusercontent.com/VirtusLab/scala-cli/main/scala-cli.sh && mv scala-cli.sh /usr/bin/scala-cli && chmod +x /usr/bin/scala-cli
# Start Scala CLI with a dummy command so that it can pre-fetch all the dependencies required
RUN scala-cli version

# build app
# We copy all the application sources, so the building step will 
# run if there are any modifications
COPY App.scala App.scala
COPY aws-generated aws-generated
COPY Makefile Makefile
RUN make assembly

# this is the start of the runtime container - one we will actually ship to AWS
FROM ghcr.io/graalvm/jdk-community:22
RUN mkdir -p /opt
# copy just the built app folder into this container - it doesn't need Scala CLI to run,
# only JVM
COPY --from=builder /app/assembly /opt/app/assembly

ENV SMITHY4S_PORT=80
EXPOSE 80

# run the app
CMD ["/opt/app/assembly"]
