package com.example.srvc_back

import cats.effect.Concurrent
import cats.implicits._
import io.circe.{Encoder, Decoder}
import io.circe.generic.semiauto._
import org.http4s._
import org.http4s.implicits._
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.circe._
import org.http4s.Method._
import scala.language.higherKinds

trait Alerts[F[_]]{
  def get: F[Alerts.Alert]
}

object Alerts {
  def apply[F[_]](implicit ev: Alerts[F]): Alerts[F] = ev

  final case class Alert(alert: String) extends AnyVal
  object Alert {
    implicit val alertDecoder: Decoder[Alert] = deriveDecoder[Alert]
    implicit def alertEntityDecoder[F[_]: Concurrent]: EntityDecoder[F, Alert] =
      jsonOf
    implicit val alertEncoder: Encoder[Alert] = deriveEncoder[Alert]
    implicit def alertEntityEncoder[F[_]]: EntityEncoder[F, Alert] =
      jsonEncoderOf
  }

  final case class AlertError(e: Throwable) extends RuntimeException

  def impl[F[_]: Concurrent](C: Client[F]): Alerts[F] = new Alerts[F]{
    val dsl = new Http4sClientDsl[F]{}
    import dsl._
    def get: F[Alerts.Alert] = {
      C.expect[Alert](GET(uri"https://icanhazdadjoke.com/"))
        .adaptError{ case t => AlertError(t)} // Prevent Client Json Decoding Failure Leaking
    }
  }
}
