package com.sagan

import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshaller}
import com.sksamuel.elastic4s.ElasticDsl.properties
import com.sksamuel.elastic4s.fields.TextField
import com.sksamuel.elastic4s.requests.mappings.MappingDefinition
import scalapb.json4s.JsonFormat
import scalapb.{GeneratedMessage, GeneratedMessageCompanion}

object Mapper {

  val addressMapping: MappingDefinition = {
    properties(
      TextField("city")
    )
  }

  val userMapping: MappingDefinition = {
    properties(
      TextField("id"),
      TextField("name"),
      TextField("address"),
      TextField("email"),
      TextField("age"),
      TextField("dateCreated")
    )
  }
}

trait EntityUnmarshallingProtocol {

  implicit def unmarshalProto[T <: GeneratedMessage with scalapb.Message[T] : GeneratedMessageCompanion : Manifest]: FromEntityUnmarshaller[T] = {
    Unmarshaller.stringUnmarshaller.map(JsonFormat.fromJsonString[T](_))
  }

}