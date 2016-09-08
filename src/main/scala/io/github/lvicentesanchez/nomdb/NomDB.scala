package io.github.lvicentesanchez.nomdb

import com.twitter.finagle.Http
import com.twitter.server.TwitterServer
import com.twitter.util._
import io.finch.circe._
import io.github.lvicentesanchez.nomdb.api.GraphQLAPI
import io.github.lvicentesanchez.nomdb.db.DatabaseImpl
import io.github.lvicentesanchez.nomdb.schema.GraphQLSchema

import scala.concurrent.ExecutionContext.Implicits

object NomDB extends TwitterServer {

  def main(): Unit = {

    val graphQLAPI: GraphQLAPI =
      new GraphQLAPI(GraphQLSchema.Root, new DatabaseImpl(), Implicits.global)

    val server =
      Http.server.
        withStatsReceiver(statsReceiver).
        serve(s":8080", graphQLAPI.api.toService)

    onExit { server.close() }

    Await.ready(adminHttpServer)
  }
}