package io.github.lvicentesanchez.nomdb.db

final case class ScoreEntry(
  movieId: String,
  ite: String,
  score: Double
)