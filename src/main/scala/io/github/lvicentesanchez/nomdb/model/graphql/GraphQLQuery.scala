package io.github.lvicentesanchez.nomdb.model.graphql

import io.circe.{ Decoder, Encoder }

final case class GraphQLQuery(value: String) extends AnyVal

object GraphQLQuery {

  implicit val decoder: Decoder[GraphQLQuery] =
    Decoder[String].map(GraphQLQuery.apply)

  implicit val encoder: Encoder[GraphQLQuery] =
    Encoder[String].contramap(_.value)
}
