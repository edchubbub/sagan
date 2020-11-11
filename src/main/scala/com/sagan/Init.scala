package com.sagan

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import protobuf.user.User

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Init extends EntityUnmarshallingProtocol {

  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "sagan")
    implicit val ec: ExecutionContextExecutor = system.executionContext

    val route =
      path("add") {
        entity(as[User]) { user =>
          post {
            Elastic.add(user)
            complete(s"""add user user: $user name: ${user.name}""")
          }
        }
      } ~
      path("list") {
        get {
          Elastic.get("aaaa")
          complete("list all user")
        }
      } ~
      path("filter") {
        get ( complete("filter user") )
      }

    val bindingFuture = Http().newServerAt("127.0.0.1", 8080).bind(route)

    println(s"Server online at http://127.0.0.1:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

}
