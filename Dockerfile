FROM openjdk:11

# Env variables
ENV SBT_VERSION 1.6.2

# Install sbt
RUN curl -L -o sbt-$SBT_VERSION.zip https://github.com/sbt/sbt/releases/download/v$SBT_VERSION/sbt-$SBT_VERSION.zip
RUN unzip sbt-$SBT_VERSION.zip -d ops

# Set current working directory
WORKDIR /workdir

ADD . /workdir
