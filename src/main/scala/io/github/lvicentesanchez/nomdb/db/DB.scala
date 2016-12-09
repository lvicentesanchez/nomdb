/*
 * Copyright (c) 2016 Schibsted Media Group. All rights reserved
 */
package io.github.lvicentesanchez.nomdb.db

import javax.sql.DataSource

import slick.driver.JdbcDriver

import scala.concurrent.{ ExecutionContext, Future }

final class DB(context: ExecutionContext, database: DataSource, val driver: JdbcDriver) {

  implicit val ec: ExecutionContext = context

  import driver.api._

  private val db: Database = Database.forDataSource(database)

  def execute[A](param: DBIOAction[A, NoStream, Nothing]): Future[A] = db.run(param)

  object tables extends Tables(driver)
}
