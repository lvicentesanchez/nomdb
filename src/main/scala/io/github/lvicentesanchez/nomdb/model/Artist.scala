package io.github.lvicentesanchez.nomdb.model

import io.github.lvicentesanchez.nomdb.db.Database
import io.github.lvicentesanchez.nomdb.schema.GraphQLContext
import sangria.schema.{ Field, ListType, ObjectType, _ }

final case class Artist(
  id: String,
  realName: String,
  stageName: String,
  age: Int
)

object Artist {

  val Type: ObjectType[GraphQLContext, Artist] =
    ObjectType[GraphQLContext, Artist](
      "Artist",
      "Representation of an artist.",
      () => fields[GraphQLContext, Artist](
        Field("id", IDType,
          Some("The id of the artist."),
          resolve = _.value.id),
        Field("realName", StringType,
          Some("The real name of the artist."),
          resolve = _.value.realName),
        Field("stageName", StringType,
          Some("The stage name of the artist"),
          resolve = _.value.stageName),
        Field("age", IntType,
          Some("The age of the artist."),
          resolve = _.value.age),
        Field("movies", ListType(Movie.Type),
          Some("The movies in which this artist has participated."),
          resolve = fetchMovies)
      )
    )

  private def fetchMovies(ctx: Context[GraphQLContext, Artist]): List[Movie] = {
    val db: Database = ctx.ctx.db
    val artistId: String = ctx.value.id
    db.findMoviesForArtist(artistId)
  }
}
