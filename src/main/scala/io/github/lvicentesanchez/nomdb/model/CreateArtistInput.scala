/*
 * Copyright (c) 2016 Schibsted Media Group. All rights reserved
 */
package io.github.lvicentesanchez.nomdb.model

import io.circe.generic.semiauto._
import io.circe.{ Decoder, Encoder }
import sangria.macros.derive._
import sangria.schema.InputObjectType

final case class CreateArtistInput(
  stageName: String,
  yearOfBirth: Int,
  movies: Option[List[Int]]
)

object CreateArtistInput {

  val Type: InputObjectType[CreateArtistInput] =
    deriveInputObjectType(
      InputObjectTypeName("CreateArtistInput"),
      InputObjectTypeDescription("Data needed to create an artist."),
      DocumentInputField("stageName", "The stage name of the artist."),
      DocumentInputField("yearOfBirth", "The year of birth of the artist."),
      DocumentInputField("movies", "The stage name of the artist.")
    )

  implicit val decoder: Decoder[CreateArtistInput] = deriveDecoder

  implicit val encoder: Encoder[CreateArtistInput] = deriveEncoder
}
