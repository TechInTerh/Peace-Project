package model


package object DroneReportModel {

    case class Citizen(name: String, peaceScore: Int)

    case class DroneReport(
        droneId: Int,
        latitude: Double,
        longitude: Double,
        citizens: Array[Citizen],
        words: Array[String],
        timestamp: Long // Epoch format
    )
}
