package io.github.lvicentesanchez.nomdb.repos

import io.github.lvicentesanchez.nomdb.db.DB
import io.github.lvicentesanchez.nomdb.model.{ Artist, Movie }

import scala.concurrent.{ ExecutionContext, Future }

trait Repository {

  def createArtist(stageName: String, yearOfBirth: Int, movies: Option[List[Int]]): Future[Artist]

  def createMovie(year: Int, title: String, score: Double, synopsis: String, cast: Option[List[Int]]): Future[Movie]

  def findAllArtist: Future[Seq[Artist]]

  def findAllMovies: Future[Seq[Movie]]

  def findAllMoviesByYear(year: Int): Future[Seq[Movie]]

  def findArtist(artistId: Int): Future[Option[Artist]]

  def findArtistsForMovie(movieId: Int): Future[Seq[Artist]]

  def findMovie(movieId: Int): Future[Option[Movie]]

  def findMoviesForArtist(artistId: Int): Future[Seq[Movie]]

  def findMoviesForArtistAndYear(artistId: Int, year: Int): Future[Seq[Movie]]
}

class RepositoryImpl(context: ExecutionContext, db: DB) extends Repository {

  implicit val ec: ExecutionContext = context

  import db.driver.api._
  import db.tables._

  override def createArtist(stageName: String, yearOfBirth: Int, movies: Option[List[Int]]): Future[Artist] = {
    val q = for {
      aid <- artists.map(_.id).max.result
      row = Artist(aid.fold(1)(_ + 1), stageName, yearOfBirth)
      _ <- artists += row
      _ <- artistsByMovie ++= movies.getOrElse(List()).map((_, row.id))
    } yield row

    db.execute(q)
  }

  def createMovie(year: Int, title: String, score: Double, synopsis: String, cast: Option[List[Int]]): Future[Movie] = {
    val q = for {
      mid <- movies.map(_.id).max.result
      row = Movie(mid.fold(1)(_ + 1), year, title, score, synopsis)
      _ <- movies += row
      _ <- artistsByMovie ++= cast.getOrElse(List()).map((row.id, _))
    } yield row

    db.execute(q)
  }

  override def findAllArtist: Future[Seq[Artist]] =
    db.execute(artists.result)

  override def findAllMovies: Future[Seq[Movie]] =
    db.execute(movies.result)

  override def findAllMoviesByYear(year: Int): Future[Seq[Movie]] =
    db.execute(movies.findByYear(year).result)

  override def findArtist(artistId: Int): Future[Option[Artist]] =
    db.execute(artists.findById(artistId).result.headOption)

  override def findArtistsForMovie(movieId: Int): Future[Seq[Artist]] = {
    val q = for {
      id <- artistsByMovie.filter(_.movieId === movieId).map(_.artistId)
      ar <- artists.filter(_.id === id)
    } yield ar
    db.execute(q.result)
  }

  override def findMovie(movieId: Int): Future[Option[Movie]] =
    db.execute(movies.findById(movieId).result.headOption)

  override def findMoviesForArtist(artistId: Int): Future[Seq[Movie]] = {
    val q = for {
      id <- artistsByMovie.filter(_.artistId === artistId).map(_.movieId)
      mv <- movies.filter(_.id === id)
    } yield mv
    db.execute(q.result)
  }

  override def findMoviesForArtistAndYear(artistId: Int, year: Int): Future[Seq[Movie]] = {
    val q = for {
      id <- artistsByMovie.filter(_.artistId === artistId).map(_.movieId)
      mv <- movies if mv.id === id && mv.year === year
    } yield mv
    db.execute(q.result)
  }
}
