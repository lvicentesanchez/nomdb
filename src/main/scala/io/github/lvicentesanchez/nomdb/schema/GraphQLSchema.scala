package io.github.lvicentesanchez.nomdb.schema

import io.github.lvicentesanchez.nomdb.db.Database
import io.github.lvicentesanchez.nomdb.model.{ Artist, Movie }
import sangria.schema._

object GraphQLSchema {

  val IdArgument: Argument[String] =
    Argument(
      name = "id",
      argumentType = StringType,
      description = "The identifier by which to retrieve the entity."
    )

  val QueryType: ObjectType[GraphQLContext, Unit] =
    ObjectType[GraphQLContext, Unit](
      "Query",
      "Queries pertaining to movies.",
      fields[GraphQLContext, Unit](
        Field("artist", OptionType(Artist.Type),
          Some("Retrieve an artist."),
          arguments = List(IdArgument),
          resolve = fetchArtist),
        Field("artists", ListType(Artist.Type),
          Some("Retrieve a list of artists."),
          arguments = Nil,
          resolve = _.ctx.db.findAllArtist),
        Field("movie", OptionType(Movie.Type),
          Some("Retrieve a movie."),
          arguments = List(IdArgument),
          resolve = ctx => ctx.ctx.db.findMovie(ctx.args.arg(IdArgument))),
        Field("movies", ListType(Movie.Type),
          Some("Retrieve a list of movie titles."),
          arguments = Nil,
          resolve = _.ctx.db.findAllMovies)
      )
    )

  val Root: Schema[GraphQLContext, Unit] = Schema(QueryType)

  private def fetchArtist(ctx: Context[GraphQLContext, Unit]): Option[Artist] = {
    val db: Database = ctx.ctx.db
    val artistId: String = ctx.arg(IdArgument)
    db.findArtist(artistId)
  }

  private def fetchMovie(ctx: Context[GraphQLContext, Unit]): Option[Movie] = {
    val db: Database = ctx.ctx.db
    val movieId: String = ctx.arg(IdArgument)
    db.findMovie(movieId)
  }
}
