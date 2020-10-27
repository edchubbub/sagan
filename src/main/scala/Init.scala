package scala

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._

import scala.io.StdIn

object Init {

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem(Behaviors.empty, "sagan")
    implicit val ec = system.executionContext

    val route =
      path("add") {
        post ( complete("add user") )
      } ~
      path("list") {
        get ( complete("list all user") )
      } ~
      path("filter") {
        get ( complete("filter user") )
      }

    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())

  }

}
