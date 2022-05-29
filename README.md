# Peace Project

TODO: Description

## Installation

To run this project, you'll need to install the following:

- [Docker](https://docs.docker.com/engine/install/)
- [Docker Compose](https://docs.docker.com/compose/install/)
- [Scala](https://www.scala-lang.org/download/)
- [SBT](https://www.scala-sbt.org/download.html)

## Dev Environment

To start the running environment:

```sh
docker-compose up -d
```

To stop the running environment:

```sh
docker-compose down
```

## Running apps

Our apps reach kafka using docker hostnames, so you need to add those two entry in `/etc/hosts`.

```txt
127.0.0.1 kafka
127.0.0.1 schema-registry
```

This directory contains 4 sbt projects each being independent app:

- `avro`: generates and registers _Avro_ schemas for data used as keys/values in created topics.
- `srvc_drone`: produces drone random data to the specified kafka topic.
- `srvc_analysis`: Consume data from kafka topics to analyze them with Spark.
- `srvc_alert`: Consume data from kafka topics to send alerts to peacewatchers.

With `sbt` each app can be started in separate shell instance.
To do that, use the following command:

```sh
sbt "project <name>" "run"
```

Example:

```sh
sbt "project srvc_drone" "run"
```

Please run `avro` and `srvc_drone` first.
To remove all generated files, use the following command:

```sh
find . -name target -type d -exec rm -rf {} \;
```
