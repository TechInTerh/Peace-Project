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

Directory contains 4 sbt projects each being independent app:

- `domain`: define the domain model representing our drones.
- `avro`: generates and registers _Avro_ schemas for data used as keys/values in created topics.
- `srvc_drone`: produces drone random data to the specified kafka topic.
- `srvc_analysis`: Consume data from kafka topics to analyze them with Spark.
- `srvc_alert`: Consume data from kafka topics to send alerts to peacewatchers.

With `sbt` each app can be started in separate shell instance with the following command:
`sbt "project <name>" "run"`, for an ex. `sbt "project srvc_drone" "run"`.

Please run `avro` and `srvc_drone` first.
