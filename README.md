# Peace Project

## PeaceLand

In the perfect world of Peaceland, harmony, peace and serenity are at the core of the society. Everybody praises its faithful leader for this : the Peaceleader.

Everyone experiences happiness, but some lost citizens are finding difficulties to integrate themselves to the great society, and need some reeducation in the peacecamps.

Peacemakers are formed to make quick interventions in order to help anyone that feel agitated in order to teach them peace, and in the most severe cases, bring them to a peacecamp to reeducate them according to the values of peacefulness.

For this they need drones named peacewatchers in order to detect as soon as possible the lowest peacescores, and to make the nearest peacewatchers squad intervene to avoid this agitated citizen to propagate this negativity towards their neighbours.

This is why they called for help 4 student of EPITA : Establishment of Peace and Individuality Totally Autonomous. These students must build an architecture answering these needs. To help them detect the low peacescores, Peaceland gives them access to peacewatchers sending them reports from all the country.

This Project shows the architecture of the solution imagined by the students, as well as the full code written in Scala and Spark, and running with Docker. It is made by :

- Victor Litoux
- Alain Salanie
- Alexandre Rulleau
- Timothée Ribes

## Project description

This directory contains multiple subprojects, each being linked to some specific services.

- `srvc_alert`: Consumes data from kafka topics to send alerts to peacewatchers.
- `srvc_analysis`: Consumes data from our minio bucket to analyze them with Spark.
- `srvc_connect`: Directory containing dockerfile and associated elements to run the schema-registry and init minio.
- `srvc_drone`: Produces random drone data to the specified kafka topic.
- `srvc_notif`: Stock alerts to send them to the front at the appropriate time.
- `srvc_website`: Diplay alerts on a custom website.

## Installation

To run this project, you'll need to install the following:

- [Docker](https://docs.docker.com/engine/install/)
- [Docker Compose](https://docs.docker.com/compose/install/)
- [Scala](https://www.scala-lang.org/download/)
- [SBT](https://www.scala-sbt.org/download.html)

## Dev Environment

### With docker-compose

To start the full environment:

```sh
docker-compose up --build -d
```

To start specific services:

```sh
docker-compose up --build <service_name>
```

To stop the running environment:

```sh
docker-compose down
```

### Without docker-compose

We recommand using `docker-compose` to run the project (as described above).
However you may want to run it without if you want to hurt yourself or for any other reason.

Unfortunately, you'll still need to start some services manually, so to avoid any config problem we recommand using the following command:

```sh
docker compose up -d init-kafka-connector sql-alerts
```

Thanks to the dependancy tree of docker-compose, it will start all external components required to run our services.

Our services and components interact with hostnames defined in the `docker-compose.yml` file.
Consequently, you need to add the following lines to your `/etc/hosts` file in order ro run the services WITHOUT docker-compose:

```txt
127.0.0.1 kafka
127.0.0.1 schema-registry
127.0.0.1 minio
127.0.0.1 srvc_notif
127.0.0.1 sql-alerts
```

This directory contains 3 sbt projects each being independent subprojects:

- `srvc_drone`
- `srvc_analysis`
- `srvc_alert`

With `sbt` each app can be started in separate shell instance.
To do that, use the following command:

```sh
sbt "project <name>" run
```

Example:

```sh
sbt "project srvc_drone" run
```

To remove all generated files, use the following command:

```sh
find . -name target -type d -exec rm -rf {} \;
```

For the others services (`srvc_notif` and `srvc_website`), you can find instructions to run them in their respective directory.
