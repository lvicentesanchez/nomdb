package io.github.lvicentesanchez.nomdb.model

import io.github.lvicentesanchez.nomdb.db.Database
import io.github.lvicentesanchez.nomdb.schema.GraphQLContext
import sangria.schema.{ Field, _ }

final case class Movie(
  id: String,
  title: String,
  year: Int,
  storyline: String
)

object Movie {

  val SiteArgument: Argument[Option[String]] =
    Argument(
      name = "site",
      argumentType = OptionInputType(StringType),
      description = "The site by which to retrieve the score."
    )

  val Type: ObjectType[GraphQLContext, Movie] =
    ObjectType[GraphQLContext, Movie](
      "Movie",
      "Representation of a movie.",
      fields[GraphQLContext, Movie](
        Field("id", IDType,
          Some("The id of the movie."),
          resolve = _.value.id),
        Field("title", StringType,
          Some("The title of the movie."),
          resolve = _.value.title),
        Field("year", IntType,
          Some("The year when the movie was released."),
          resolve = _.value.year),
        Field("storyline", StringType,
          Some("The storyline of the movie."),
          resolve = _.value.storyline),
        Field("scores", ListType(Score.Type),
          Some("The scores that different websites gave to the movie."),
          arguments = List(SiteArgument),
          resolve = fetchScores),
        Field("cast", ListType(Artist.Type),
          Some("The actors and actresses that participated in the movie."),
          resolve = fetchArtist)
      )
    )

  private def fetchArtist(ctx: Context[GraphQLContext, Movie]): List[Artist] = {
    val db: Database = ctx.ctx.db
    val movieId: String = ctx.value.id
    db.findArtistsForMovie(movieId)
  }

  private def fetchScores(ctx: Context[GraphQLContext, Movie]): List[Score] = {
    val db: Database = ctx.ctx.db
    val site: Option[String] = ctx.arg(SiteArgument)
    val movieId: String = ctx.value.id
    site.fold(db.findScoresForMovie(movieId))(db.findScoreForMovieAndSite(movieId, _))
  }
}
