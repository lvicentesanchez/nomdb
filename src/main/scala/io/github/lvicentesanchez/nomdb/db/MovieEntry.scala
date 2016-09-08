package io.github.lvicentesanchez.nomdb.db

final case class MovieEntry(
  id: String,
  title: String,
  year: Int,
  storyline: String
)