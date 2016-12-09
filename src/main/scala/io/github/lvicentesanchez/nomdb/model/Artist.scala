package io.github.lvicentesanchez.nomdb.model

import io.github.lvicentesanchez.nomdb.model.graphql.GraphQLContext
import io.github.lvicentesanchez.nomdb.repos.Repository
import io.github.lvicentesanchez.nomdb.schema.Arguments
import sangria.schema.{ Field, ListType, ObjectType, _ }

import scala.concurrent.Future

final case class Artist(
  id: Int,
  stageName: String,
  yearOfBirth: Int
)

object Artist {

  val FilmographyEntryType: ObjectType[GraphQLContext, Movie] =
    ObjectType[GraphQLContext, Movie](
      "FilmographyEntry",
      "Representation of a movie where the artist appeared.",
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
          resolve = _.value.score)
      )
    )

  val Type: ObjectType[GraphQLContext, Artist] =
    ObjectType[GraphQLContext, Artist](
      "Artist",
      "Representation of an artist.",
      fields[GraphQLContext, Artist](
        Field("id", IntType,
          Some("The id of the artist."),
          resolve = _.value.id),
        Field("stageName", StringType,
          Some("The stage name of the artist"),
          resolve = _.value.stageName),
        Field("yearOfBirth", IntType,
          Some("The year when the artist was born."),
          resolve = _.value.yearOfBirth),
        Field("filmography", ListType(FilmographyEntryType),
          Some("The movies in which this artist has participated."),
          arguments = List(Arguments.Year),
          resolve = fetchMovies)
      )
    )

  private def fetchMovies(ctx: Context[GraphQLContext, Artist]): Future[Seq[Movie]] = {
    val repo: Repository = ctx.ctx.repo
    val artistId: Int = ctx.value.id
    val year: Option[Int] = ctx.arg(Arguments.Year)
    year.fold(repo.findMoviesForArtist(artistId))(repo.findMoviesForArtistAndYear(artistId, _))
  }
}
