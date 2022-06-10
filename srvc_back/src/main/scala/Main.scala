package com.example.srvc_back

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  def run(args: List[String]) =
    Srvc_backServer.stream[IO].compile.drain.as(ExitCode.Success)
}
