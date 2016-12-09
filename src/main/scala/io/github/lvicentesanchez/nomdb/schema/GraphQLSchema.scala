package io.github.lvicentesanchez.nomdb.schema

import io.github.lvicentesanchez.nomdb.model._
import io.github.lvicentesanchez.nomdb.model.graphql.GraphQLContext
import io.github.lvicentesanchez.nomdb.repos.Repository
import sangria.schema.{ Field, _ }

import scala.concurrent.Future

object GraphQLSchema {

  val QueryType: ObjectType[GraphQLContext, Unit] =
    ObjectType[GraphQLContext, Unit](
      "Query",
      "Queries pertaining to movies.",
      fields[GraphQLContext, Unit](
        Field("artist", OptionType(Artist.Type),
          Some("Retrieve an artist."),
          arguments = List(Arguments.Id),
          resolve = fetchArtist),
        Field("artists", ListType(Artist.Type),
          Some("Retrieve a list of artists."),
          arguments = Nil,
          resolve = _.ctx.repo.findAllArtist),
        Field("movie", OptionType(Movie.Type),
          Some("Retrieve a movie."),
          arguments = List(Arguments.Id),
          resolve = fetchMovie),
        Field("movies", ListType(Movie.Type),
          Some("Retrieve a list of movie titles."),
          arguments = List(Arguments.Year),
          resolve = fetchAllMovies)
      )
    )

  val MutationType: ObjectType[GraphQLContext, Unit] =
    ObjectType[GraphQLContext, Unit](
      "Mutation",
      "Mutations pertaining to movies.",
      fields[GraphQLContext, Unit](
        Field("createArtist", Artist.Type,
          Some("Creates an artist."),
          arguments = List(Arguments.CreateArtist),
          resolve = createArtist),
        Field("createMovie", Movie.Type,
          Some("Creates a movie."),
          arguments = List(Arguments.CreateMovie),
          resolve = createMovie)
      )
    )

  val Root: Schema[GraphQLContext, Unit] = Schema(QueryType, Some(MutationType))

  private def createArtist(ctx: Context[GraphQLContext, Unit]): Future[Artist] = {
    val repo: Repository = ctx.ctx.repo
    val input: CreateArtistInput = ctx.arg(Arguments.CreateArtist)
    repo.createArtist(input.stageName, input.yearOfBirth, input.movies)
  }

  private def createMovie(ctx: Context[GraphQLContext, Unit]): Future[Movie] = {
    val repo: Repository = ctx.ctx.repo
    val input: CreateMovieInput = ctx.arg(Arguments.CreateMovie)
    repo.createMovie(input.year, input.title, input.score, input.synopsis, input.cast)
  }

  private def fetchAllMovies(ctx: Context[GraphQLContext, Unit]): Future[Seq[Movie]] = {
    val repo: Repository = ctx.ctx.repo
    val year: Option[Int] = ctx.arg(Arguments.Year)
    year.fold(repo.findAllMovies)(repo.findAllMoviesByYear)
  }

  private def fetchArtist(ctx: Context[GraphQLContext, Unit]): Future[Option[Artist]] = {
    val repo: Repository = ctx.ctx.repo
    val artistId: Int = ctx.arg(Arguments.Id)
    repo.findArtist(artistId)
  }

  private def fetchMovie(ctx: Context[GraphQLContext, Unit]): Future[Option[Movie]] = {
    val repo: Repository = ctx.ctx.repo
    val movieId: Int = ctx.arg(Arguments.Id)
    repo.findMovie(movieId)
  }
}
