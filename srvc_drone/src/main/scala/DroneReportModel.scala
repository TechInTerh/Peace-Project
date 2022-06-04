package model


package object DroneReportModel {

    case class Citizen(name: String, peaceScore: Int)

    case class DroneReport(
        droneId: Int,
        latitude: Double,
        longitude: Double,
        citizens: Vector[Citizen],
        words: Vector[String],
        // Avro cringe with Date type, so we'll just use String and set a format
        timestamp: String
    )
}
