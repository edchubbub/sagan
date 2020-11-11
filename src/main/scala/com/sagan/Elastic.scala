package com.sagan

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.requests.common.RefreshPolicy
import com.sksamuel.elastic4s.requests.searches.SearchResponse
import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties, RequestFailure, RequestSuccess}
import protobuf.user.User

object Elastic {

  val host: String = sys.env.getOrElse("ES_HOST", "127.0.0.1")
  val port: String = sys.env.getOrElse("ES_PORT", "9200")
  val props: ElasticProperties = ElasticProperties(s"http://$host:$port")
  implicit val client: ElasticClient = ElasticClient(JavaClient(props))

  val INDEX_NAME = "sagan"

  def add(user: User): Unit = {
    client.execute {
      indexInto(INDEX_NAME).fields(
        "id" -> user.id,
        "name" -> user.name,
        "address" -> ("city" -> user.address.city),
        "email" -> user.email,
        "age" -> user.age,
        "dateCreated" -> user.dateCreated
      ).refresh(RefreshPolicy.Immediate)
    }.await
    client.close()
  }

  def get(predicate: String): Unit = {
    val resp = client.execute {
      search(INDEX_NAME).query(predicate)
    }.await

    println("---- Search Results ----")
    resp match {
      case failure: RequestFailure => println("We failed " + failure.error)
      case results: RequestSuccess[SearchResponse] => println(results.result.hits.hits.toList)
      case results: RequestSuccess[_] => println(results.result)
    }

    client.close()

    resp foreach (search => println(s"There were ${search.totalHits} total hits"))
  }

}
