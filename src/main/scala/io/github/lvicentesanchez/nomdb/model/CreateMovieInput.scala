/*
 * Copyright (c) 2016 Schibsted Media Group. All rights reserved
 */
package io.github.lvicentesanchez.nomdb.model

import io.circe.generic.semiauto._
import io.circe.{ Decoder, Encoder }
import sangria.macros.derive.{ DocumentInputField, InputObjectTypeDescription, InputObjectTypeName, _ }
import sangria.schema.InputObjectType

final case class CreateMovieInput(
  year: Int,
  title: String,
  score: Double,
  synopsis: String,
  cast: Option[List[Int]]
)

object CreateMovieInput {

  val Type: InputObjectType[CreateMovieInput] =
    deriveInputObjectType(
      InputObjectTypeName("CreateMovieInput"),
      InputObjectTypeDescription("Data needed to create a movie."),
      DocumentInputField("year", "The year when the movie was released."),
      DocumentInputField("title", "The title of the movie."),
      DocumentInputField("synopsis", "The synopsis of the movie."),
      DocumentInputField("score", "The score of the movie"),
      DocumentInputField("cast", "The artists that participated in the movie.")
    )

  implicit val decoder: Decoder[CreateMovieInput] = deriveDecoder

  implicit val encoder: Encoder[CreateMovieInput] = deriveEncoder
}
