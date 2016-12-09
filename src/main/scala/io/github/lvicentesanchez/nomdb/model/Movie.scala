package io.github.lvicentesanchez.nomdb.model

import io.github.lvicentesanchez.nomdb.model.graphql.GraphQLContext
import io.github.lvicentesanchez.nomdb.repos.Repository
import sangria.schema.{ Field, _ }

import scala.concurrent.Future

final case class Movie(
  id: Int,
  year: Int,
  title: String,
  score: Double,
  synopsis: String
)

object Movie {

  val CastMemberType: ObjectType[GraphQLContext, Artist] =
    ObjectType[GraphQLContext, Artist](
      "CastMember",
      "Representation of one member of the cast of a movie.",
      fields[GraphQLContext, Artist](
        Field("id", IntType,
          Some("The id of the artist."),
          resolve = _.value.id),
        Field("stageName", StringType,
          Some("The stage name of the artist"),
          resolve = _.value.stageName),
        Field("yearOfBirth", IntType,
          Some("The year when the artist was born."),
          resolve = _.value.yearOfBirth)
      )
    )

  val Type: ObjectType[GraphQLContext, Movie] =
    ObjectType[GraphQLContext, Movie](
      "Movie",
      "Representation of a movie.",
      fields[GraphQLContext, Movie](
        Field("id", IntType,
          Some("The id of the movie."),
          resolve = _.value.id),
        Field("year", IntType,
          Some("The year when the movie was released."),
          resolve = _.value.year),
        Field("title", StringType,
          Some("The title of the movie."),
          resolve = _.value.title),
        Field("synopsis", StringType,
          Some("The synopsis of the movie."),
          resolve = _.value.synopsis),
        Field("score", FloatType,
          Some("The score of the movie"),
          resolve = _.value.score),
        Field("cast", ListType(CastMemberType),
          Some("The actors and actresses that participated in the movie."),
          resolve = fetchArtist)
      )
    )

  private def fetchArtist(ctx: Context[GraphQLContext, Movie]): Future[Seq[Artist]] = {
    val repo: Repository = ctx.ctx.repo
    val movieId: Int = ctx.value.id
    repo.findArtistsForMovie(movieId)
  }
}
