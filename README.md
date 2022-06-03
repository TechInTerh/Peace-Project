# Peace Project

## PeaceLand

In the perfect world of Peaceland, harmony, peace and serenity are at the core of the society. Everybody praises its faithful leader for this : the Peaceleader.
Everyone experiences happiness, but some lost citizens are finding difficulties to integrate themselves to the great society, and need some reeducation in the peacecamps.
Peacemakers are formed to make quick interventions in order to help anyone that feel agitated in order to teach them peace, and in the most severe cases, bring them to a peacecamp to reeducate them according to the values of peacefulness.
For this they need drones named peacewatchers in order to detect as soon as possible the lowest peacescores, and to make the nearest peacewatchers squad intervene to avoid this agitated citizen to propagate this negativity towards their neighbour.

This is why they called for help 4 student of EPITA : Establishment of Peace and Individuality Totally Autonomous. These students must build an architecture answering these needs. To help them detect the low peacescores, Peaceland gives them FIXME sending them reports from all the country.

This Project shows the architecture of the solution imagined by the students, as well as the full code written in Scala and Spark, and running with Docker. It is made by :

- Victor Litoux
- Alain Salanie
- Alexandre Rulleau
- Timothée Ribes

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

TODO: UPDATE THIS PART

Our apps reach kafka using docker hostnames, so you need to add those two entry in `/etc/hosts`.

```txt
127.0.0.1 kafka
127.0.0.1 schema-registry
```

This directory contains 4 sbt projects each being independent app:

- `srvc_schema`: generates and registers _Avro_ schemas for our report datas
- `srvc_drone`: produces drone random data to the specified kafka topic.
- `srvc_analysis`: Consume data from kafka topics to analyze them with Spark.
- `srvc_alert`: Consume data from kafka topics to send alerts to peacewatchers.

With `sbt` each app can be started in separate shell instance.
To do that, use the following command:

```sh
sbt "project <name>" run
```

Example:

```sh
sbt "project srvc_drone" run
```

Please run `srvc_schema` and `srvc_drone` first.
To remove all generated files, use the following command:

```sh
find . -name target -type d -exec rm -rf {} \;
```
