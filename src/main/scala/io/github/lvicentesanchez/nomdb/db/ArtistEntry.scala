package io.github.lvicentesanchez.nomdb.db

final case class ArtistEntry(
  id: String,
  realName: String,
  stageName: String,
  age: Int,
  movies: List[String]
)
