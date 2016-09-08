package io.github.lvicentesanchez.nomdb.model

import sangria.schema._

final case class Score(
  site: String,
  score: Double
)

object Score {

  val Type: ObjectType[Unit, Score] =
    ObjectType[Unit, Score](
      "Score",
      "Representation of a score.",
      fields[Unit, Score](
        Field("site", StringType,
          Some("The website that scored the movie."),
          resolve = _.value.site),
        Field("score", FloatType,
          Some("The score of the movie."),
          resolve = _.value.score)
      )
    )
}
