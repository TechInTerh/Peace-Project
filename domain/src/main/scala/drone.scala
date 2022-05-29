package drone

import java.util.Date

package object domain {

    case class DroneId(id: Int)

    case class Latitude(value: Double)
    case class Longitude(value: Double)

    case class Citizens(value: Array[Citizen])
    case class Citizen(name: String, peaceScore: Int)

    case class Words(value: Array[String])
    case class Timestamp(value: Date)
}
