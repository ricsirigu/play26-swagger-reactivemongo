package models

import javax.inject.Inject

import scala.concurrent.{ ExecutionContext, Future }

import play.api.libs.json.{ Json, JsObject }

import reactivemongo.bson.{ BSONDocument, BSONObjectID }

import reactivemongo.api.{ Cursor, ReadPreference }
import reactivemongo.api.commands.WriteResult

import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection

import play.modules.reactivemongo.ReactiveMongoApi

/**
 * Created by Riccardo Sirigu on 10/08/2017.
 */
case class Todo(_id: Option[BSONObjectID], title: String, completed: Option[Boolean])

object JsonFormats{
  import play.api.libs.json._

  implicit val todoFormat: OFormat[Todo] = Json.format[Todo]
}

class TodoRepository @Inject()(implicit ec: ExecutionContext, reactiveMongoApi: ReactiveMongoApi){

  import JsonFormats._

  def todosCollection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("todos"))

  def getAll(limit: Int = 100): Future[Seq[Todo]] =
    todosCollection.flatMap(_.find(
      selector = Json.obj(/* Using Play JSON */),
      projection = Option.empty[JsObject])
      .cursor[Todo](ReadPreference.primary)
      .collect[Seq](limit, Cursor.FailOnError[Seq[Todo]]())
    )

  def getTodo(id: BSONObjectID): Future[Option[Todo]] =
    todosCollection.flatMap(_.find(
      selector = BSONDocument("_id" -> id),
      projection = Option.empty[BSONDocument])
      .one[Todo])

  def addTodo(todo: Todo): Future[WriteResult] =
    todosCollection.flatMap(_.insert(todo))

  def updateTodo(id: BSONObjectID, todo: Todo): Future[Option[Todo]] = {
    val selector = BSONDocument("_id" -> id)
    val updateModifier = BSONDocument(
      f"$$set" -> BSONDocument(
        "title" -> todo.title,
        "completed" -> todo.completed)
    )

    todosCollection.flatMap(
      _.findAndUpdate(selector, updateModifier, fetchNewObject = true)
        .map(_.result[Todo])
    )
  }

  def deleteTodo(id: BSONObjectID): Future[Option[Todo]] = {
    val selector = BSONDocument("_id" -> id)
    todosCollection.flatMap(_.findAndRemove(selector).map(_.result[Todo]))
  }

}
