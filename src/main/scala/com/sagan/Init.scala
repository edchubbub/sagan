package com.sagan

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import com.sagan.Domain.User
import protobuf.user.{User => ProtoUser}

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Init extends ClientMarshallingProtocol {

  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "sagan")
    implicit val ec: ExecutionContextExecutor = system.executionContext

    val route =
      path("add") {
        entity(as[ProtoUser]) { protoUser =>
          post {
            complete(s"add user user: ${protoUser} name: ${protoUser.name}")
          }
        }
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
