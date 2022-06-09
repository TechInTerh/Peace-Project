package com.example.srvc_back

import cats.effect.{Async, Resource}
import cats.syntax.all._
import com.comcast.ip4s._
import fs2.Stream
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger
import scala.language.higherKinds

object Srvc_backServer {

  def stream[F[_]: Async]: Stream[F, Nothing] = {
    Stream.resource(EmberClientBuilder.default[F].build)
    .map(Alerts.impl[F](_))
    .map(Srvc_backRoutes.alertRoutes[F](_).orNotFound)
    .map(httpApp=>Logger.httpApp(true, true)(httpApp))
    .flatMap(finalHttpApp=>Stream.resource(
        EmberServerBuilder.default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(finalHttpApp)
          .build >>
        Resource.eval(Async[F].never)
    ))
  }.drain
}
