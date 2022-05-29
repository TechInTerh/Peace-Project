# Running apps

Directory contains 4 sbt projects each being independent app:

* domain: define the domain model representing our drones.
* avro: generates and registers _Avro_ schemas for data used as keys/values in created topics.
* srvc_drone: produces drone random data to the specified kafka topic.
* srvc_analysis: Consume data from kafka topics to analyze them with Spark.
* srvc_alert: Consume data from kafka topics to send alerts to peacewatchers.

With `sbt` each app can be started in separate shell instance with the following command:
`sbt "project <name>" "run"`, for an ex. `sbt "project srvc_drone" "run"`.

Please run `avro` and `srvc_drone` first.
