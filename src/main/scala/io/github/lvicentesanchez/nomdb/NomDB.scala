package io.github.lvicentesanchez.nomdb

import javax.sql.DataSource

import com.twitter.finagle.Http
import com.twitter.server.AbstractTwitterServer
import com.twitter.util._
import com.zaxxer.hikari.HikariDataSource
import io.finch.circe._
import io.github.lvicentesanchez.nomdb.api.GraphQLAPI
import io.github.lvicentesanchez.nomdb.db.DB
import io.github.lvicentesanchez.nomdb.repos.RepositoryImpl
import io.github.lvicentesanchez.nomdb.schema.GraphQLSchema
import org.flywaydb.core.Flyway
import slick.driver.H2Driver

import scala.concurrent.ExecutionContext.Implicits

object NomDB extends AbstractTwitterServer {

  val dataSource: DataSource = {
    val temp: HikariDataSource = new HikariDataSource()
    temp.setJdbcUrl("jdbc:h2:./nomdb;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1")
    //temp.setJdbcUrl("jdbc:h2:mem:nomdb;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1")
    temp.setUsername("admin")
    temp.setPassword("pass")
    temp
  }

  val db: DB = new DB(Implicits.global, dataSource, H2Driver)

  val graphQLAPI: GraphQLAPI =
    new GraphQLAPI(GraphQLSchema.Root, new RepositoryImpl(Implicits.global, db), Implicits.global)

  override def onInit(): Unit = {
    val flyway = new Flyway()
    flyway.setDataSource(dataSource)
    flyway.migrate()
  }

  override def main(): Unit = {
    val server =
      Http.server.
        withStatsReceiver(statsReceiver).
        serve(s":8080", graphQLAPI.api.toService)

    onExit { server.close() }

    Await.ready(adminHttpServer)
  }
}