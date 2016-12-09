package io.github.lvicentesanchez.nomdb.api

import com.twitter.finagle.http.Status
import com.twitter.io.{ Buf, Reader }
import io.circe.Json
import io.finch._
import io.finch.circe._
import io.github.lvicentesanchez.nomdb.model.graphql.{ GraphQLContext, GraphQLRequest }
import io.github.lvicentesanchez.nomdb.repos.Repository
import io.github.lvicentesanchez.nomdb.utils._
import sangria.ast.Document
import sangria.execution.{ ErrorWithResolver, Executor }
import sangria.marshalling.circe._
import sangria.parser.QueryParser
import sangria.renderer.SchemaRenderer
import sangria.schema.Schema

import scala.concurrent.{ ExecutionContext, Future => ScalaFuture }
import scala.util.Try

class GraphQLAPI(schema: Schema[GraphQLContext, Unit], repo: Repository, ctxt: ExecutionContext) {

  private implicit val ec: ExecutionContext = ctxt
  private val executor: Executor[GraphQLContext, Unit] = Executor(schema)

  private val graphQLContext: GraphQLContext = GraphQLContext(repo)

  private val graphQLEditor: Endpoint[Buf] = get("graphql") {
    val file: Reader =
      Reader.fromStream(
        ClassLoader.getSystemResourceAsStream("graphiql/GraphiQL.html")
      )
    Reader.readAll(file).map(Ok(_).withHeader("Content-Type" -> "text/html"))
  }

  private val graphQLSchema: Endpoint[Buf] = get("graphql" :: "schema") {
    val rendered: String =
      SchemaRenderer.renderSchema(schema)
    Ok(Buf.Utf8(rendered)).withHeader("Content-Type" -> "text/plain")
  }

  private val graphQL: Endpoint[Json] = post("graphql" :: body.as[GraphQLRequest]) {
    (request: GraphQLRequest) =>
      val document: Try[Document] = QueryParser.parse(request.query.value)
      val variables: Json = request.variables.fold(Json.obj())(_.value)
      ScalaFuture.fromTry(document).
        flatMap(ast => executor.execute(ast, graphQLContext, (), None, variables)).asTwitter.
        map(Ok).handle {
          case exc: ErrorWithResolver => Output.payload(exc.resolveError, Status.BadRequest)
        }
  }

  val api = graphQLEditor :+: graphQLSchema :+: graphQL
}
