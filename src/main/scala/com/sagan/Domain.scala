package com.sagan

import java.security.Timestamp

import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, PredefinedFromEntityUnmarshallers, Unmarshaller}
import com.sksamuel.elastic4s.ElasticDsl.properties
import com.sksamuel.elastic4s.fields.TextField
import com.sksamuel.elastic4s.requests.mappings.MappingDefinition
import protobuf.user.{User => ProtoUser}
import scalapb.json4s.JsonFormat
import scalapb.{GeneratedMessage, GeneratedMessageCompanion}

object Domain {

  sealed trait Domain {
    def toMap: MappingDefinition
  }

  case class Address(city: String) extends Domain {
    override def toMap: MappingDefinition = {
      properties(
        TextField("city")
      )
    }
  }

  case class User(id: Int, name: String, address: Address, email: String, age: Int, dateCreated: Timestamp) extends Domain {
    override def toMap: MappingDefinition = {
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
}


trait ClientMarshallingProtocol {

  implicit def unmarshalProto[T <: GeneratedMessage with scalapb.Message[T] : GeneratedMessageCompanion : Manifest]: FromEntityUnmarshaller[T] = {
    Unmarshaller.stringUnmarshaller.map(JsonFormat.fromJsonString[T](_))
  }

}