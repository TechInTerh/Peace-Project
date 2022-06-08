package com.example.srvc_back

import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import scala.language.higherKinds

object Srvc_backRoutes {

  def alertRoutes[F[_]: Sync](A: Alerts[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case POST -> Root / "alert" =>
        Ok(A.get)
    }
  }
}