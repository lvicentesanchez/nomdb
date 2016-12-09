package io.github.lvicentesanchez.nomdb.model.graphql

import io.circe.parser._
import io.circe.{ Decoder, DecodingFailure, Encoder, Json }

final case class GraphQLVariables(value: Json) extends AnyVal

object GraphQLVariables {

  implicit val decoder: Decoder[GraphQLVariables] =
    Decoder[String].flatMap(string => Decoder.instance(
      _ => parse(string).leftMap(failure => DecodingFailure(failure.message, List()))
    )).or(Decoder[Json]).map(GraphQLVariables.apply)

  implicit val encoder: Encoder[GraphQLVariables] =
    Encoder[Json].contramap(_.value)
}
