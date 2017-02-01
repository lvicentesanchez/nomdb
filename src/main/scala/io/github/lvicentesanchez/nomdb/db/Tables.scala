/*
 * Copyright (c) 2016 Schibsted Media Group. All rights reserved
 */
package io.github.lvicentesanchez.nomdb.db

import io.github.lvicentesanchez.nomdb.model.{ Artist, Movie }
import slick.ast.ColumnOption.PrimaryKey
import slick.jdbc.JdbcProfile

class Tables private[db] (val driver: JdbcProfile) {

  import driver.api._

  final class ArtistsByMovieTbl(tag: Tag) extends Table[(Int, Int)](tag, "artists_by_movie") {

    def movieId = column[Int]("movie_id")
    def artistId = column[Int]("artist_id")

    def pk = primaryKey("pk_artists_by_movie", (movieId, artistId))

    def * = (movieId, artistId)
  }

  final class ArtistsTbl(tag: Tag) extends Table[Artist](tag, "artists") {

    def id = column[Int]("artist_id", PrimaryKey)
    def stageName = column[String]("stage_name")
    def yearOfBirth = column[Int]("year_of_birth")

    def * = (id, stageName, yearOfBirth) <> ((Artist.apply _).tupled, Artist.unapply)
  }

  final class MoviesTbl(tag: Tag) extends Table[Movie](tag, "movies") {

    def id = column[Int]("movie_id", PrimaryKey)
    def year = column[Int]("release_year")
    def title = column[String]("title")
    def score = column[Double]("score")
    def synopsis = column[String]("synopsis")

    def * = (id, year, title, score, synopsis) <> ((Movie.apply _).tupled, Movie.unapply)
  }

  object artists extends TableQuery(new ArtistsTbl(_)) {

    val findById = this.findBy(_.id)
  }

  object artistsByMovie extends TableQuery(new ArtistsByMovieTbl(_))

  object movies extends TableQuery(new MoviesTbl(_)) {

    val findById = this.findBy(_.id)

    val findByYear = this.findBy(_.year)
  }
}
