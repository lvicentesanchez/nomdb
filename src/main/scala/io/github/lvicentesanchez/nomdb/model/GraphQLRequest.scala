package io.github.lvicentesanchez.nomdb.model

import io.circe.generic.semiauto._
import io.circe.{ Decoder, Encoder }

final case class GraphQLRequest(
  query: GraphQLQuery,
  variables: Option[GraphQLVariables]
)

object GraphQLRequest {

  implicit val decoder: Decoder[GraphQLRequest] = deriveDecoder[GraphQLRequest]

  implicit val encoder: Encoder[GraphQLRequest] = deriveEncoder[GraphQLRequest]
}
