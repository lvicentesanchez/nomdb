/*
 * Copyright (c) 2016 Schibsted Media Group. All rights reserved
 */
package io.github.lvicentesanchez.nomdb.schema

import io.github.lvicentesanchez.nomdb.model.{ CreateArtistInput, CreateMovieInput }
import sangria.marshalling.circe._
import sangria.schema._

object Arguments {

  val CreateArtist: Argument[CreateArtistInput] =
    Argument(
      name = "input",
      argumentType = CreateArtistInput.Type,
      description = "The artist we want to create."
    )

  val CreateMovie: Argument[CreateMovieInput] =
    Argument(
      name = "movie",
      argumentType = CreateMovieInput.Type,
      description = "The movie we want to create."
    )

  val Id: Argument[Int] =
    Argument(
      name = "id",
      argumentType = IntType,
      description = "The identifier by which to retrieve the entity."
    )

  val Year: Argument[Option[Int]] =
    Argument(
      name = "year",
      argumentType = OptionInputType(IntType),
      description = "The year when a movie was released."
    )
}
