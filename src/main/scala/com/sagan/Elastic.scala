package com.sagan

import com.sagan.Domain.Domain
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.requests.common.RefreshPolicy
import com.sksamuel.elastic4s.requests.searches.SearchResponse
import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties, RequestFailure, RequestSuccess}

object Elastic {

  val host: String = sys.env.getOrElse("ES_HOST", "127.0.0.1")
  val port: String = sys.env.getOrElse("ES_PORT", "9200")
  val props: ElasticProperties = ElasticProperties(s"http://$host:$port")
  implicit val client: ElasticClient = ElasticClient(JavaClient(props))

  val USER_INDEX = "user"
  val ADDRESS_INDEX = "address"

  def add(domain: Domain)(implicit client: ElasticClient): Unit = {
    client.execute {
      createIndex("artists").mapping(
        domain.toMap
      )
    }.await

    client.execute {
      indexInto("artists").fields("name" -> "L.S. Lowry").refresh(RefreshPolicy.Immediate)
    }.await

    client.close()
  }

  def get(): Unit = {
    val resp = client.execute {
      search("artists").query("lowry")
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
