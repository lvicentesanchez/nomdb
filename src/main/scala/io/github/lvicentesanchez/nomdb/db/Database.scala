package io.github.lvicentesanchez.nomdb.db

import io.github.lvicentesanchez.nomdb.model.{ Artist, Movie, Score }

trait Database {

  def findAllArtist: List[Artist]

  def findAllMovies: List[Movie]

  def findArtist(artistId: String): Option[Artist]

  def findArtistsForMovie(movieId: String): List[Artist]

  def findMovie(movieId: String): Option[Movie]

  def findMoviesForArtist(artistId: String): List[Movie]

  def findScoresForMovie(movieId: String): List[Score]

  def findScoreForMovieAndSite(movieId: String, site: String): List[Score]
}
class DatabaseImpl() extends Database {

  private val artists: List[ArtistEntry] = List(
    ArtistEntry("1", "Peter Parker", "Spiderman", 19, List("1", "3", "5")),
    ArtistEntry("2", "Sam Actor", "Sam Actor", 39, List("7", "9")),
    ArtistEntry("3", "John Doe", "Max Power", 27, List("2", "4", "6")),
    ArtistEntry("4", "James Brown", "James Dark", 21, List("8", "10")),
    ArtistEntry("5", "Arthur King", "King Arthur", 18, List("1", "2", "3", "4", "5")),
    ArtistEntry("6", "Random Guy", "The Actor", 50, List("6", "7", "8", "9", "10")),
    ArtistEntry("7", "Frank Castle", "Punisher", 45, List("1", "4", "7", "10")),
    ArtistEntry("8", "Clark Kent", "Superman", 33, List("2", "5", "8")),
    ArtistEntry("9", "Bruce Wayne", "Batman", 31, List("1", "2", "4", "5")),
    ArtistEntry("10", "Aquaman", "Aquaman", 26, List("3", "6", "7", "8", "9", "10"))
  )

  private val movies: List[MovieEntry] = List(
    MovieEntry("1", "1st movie", 1983, "Movie about things."),
    MovieEntry("2", "A great story", 2015, "It's really great."),
    MovieEntry("3", "That movie", 1991, "I didn't pay attention."),
    MovieEntry("4", "Not very good", 1993, "Horrible story."),
    MovieEntry("5", "Short", 1997, "It's very short."),
    MovieEntry("6", "Some movie", 2016, "I'm not sure."),
    MovieEntry("7", "1984", 2011, "Big brother."),
    MovieEntry("8", "Almost there", 2007, "We are getting there."),
    MovieEntry("9", "It's a movie", 2005, "It's a very good movie."),
    MovieEntry("10", "Wow!", 2009, "I'm speechless.")
  )

  private val scores: List[ScoreEntry] = List(
    ScoreEntry("1", "Rotten Tomatoes", 7.3),
    ScoreEntry("1", "Metacritic", 6.8),
    ScoreEntry("1", "Guardian", 7.1),
    ScoreEntry("2", "Rotten Tomatoes", 4.3),
    ScoreEntry("2", "Metacritic", 5.8),
    ScoreEntry("2", "Guardian", 3.1),
    ScoreEntry("3", "Rotten Tomatoes", 8.8),
    ScoreEntry("3", "Metacritic", 9.0),
    ScoreEntry("3", "Guardian", 6.5),
    ScoreEntry("4", "Rotten Tomatoes", 3.5),
    ScoreEntry("4", "Metacritic", 3.5),
    ScoreEntry("4", "Guardian", 3.5),
    ScoreEntry("5", "Rotten Tomatoes", 6.3),
    ScoreEntry("5", "Metacritic", 6.7),
    ScoreEntry("5", "Guardian", 5.9),
    ScoreEntry("6", "Rotten Tomatoes", 9.7),
    ScoreEntry("6", "Metacritic", 10.0),
    ScoreEntry("6", "Guardian", 8.8),
    ScoreEntry("7", "Rotten Tomatoes", 6.5),
    ScoreEntry("7", "Metacritic", 6.3),
    ScoreEntry("7", "Guardian", 8.9),
    ScoreEntry("8", "Rotten Tomatoes", 10.0),
    ScoreEntry("8", "Metacritic", 10.0),
    ScoreEntry("8", "Guardian", 10.0),
    ScoreEntry("9", "Rotten Tomatoes", 5.1),
    ScoreEntry("9", "Metacritic", 5.1),
    ScoreEntry("9", "Guardian", 5.0),
    ScoreEntry("10", "Rotten Tomatoes", 4.9),
    ScoreEntry("10", "Metacritic", 5.1),
    ScoreEntry("10", "Guardian", 5.3)
  )

  override def findAllArtist: List[Artist] =
    artists.map {
      case ArtistEntry(id, rn, an, yr, _) => Artist(id, rn, an, yr)
    }

  override def findAllMovies: List[Movie] =
    movies.map {
      case MovieEntry(id, ti, yr, su) => Movie(id, ti, yr, su)
    }

  override def findArtist(artistId: String): Option[Artist] =
    artists.find(_.id == artistId).map {
      case ArtistEntry(id, rn, an, yr, _) => Artist(id, rn, an, yr)
    }

  override def findArtistsForMovie(movieId: String): List[Artist] =
    artists.filter(_.movies.contains(movieId)).map {
      case ArtistEntry(id, rn, an, yr, _) => Artist(id, rn, an, yr)
    }

  override def findMovie(movieId: String): Option[Movie] =
    movies.find(_.id == movieId).map {
      case MovieEntry(id, ti, yr, su) => Movie(id, ti, yr, su)
    }

  override def findMoviesForArtist(artistId: String): List[Movie] =
    artists.find(_.id == artistId).fold(List[Movie]()) {
      case ArtistEntry(_, _, _, _, mv) =>
        mv.flatMap(str => movies.find(_.id == str)).map {
          case MovieEntry(id, ti, yr, su) => Movie(id, ti, yr, su)
        }
    }

  override def findScoresForMovie(movieId: String): List[Score] =
    scores.filter(_.movieId == movieId).map {
      case ScoreEntry(id, si, sc) => Score(si, sc)
    }

  override def findScoreForMovieAndSite(movieId: String, site: String): List[Score] =
    findScoresForMovie(movieId).filter(_.site == site)

}
