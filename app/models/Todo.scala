package models

import javax.inject.Inject

import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.ReadPreference
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

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

  def getAll: Future[Seq[Todo]] = {
    val query = Json.obj()
    todosCollection.flatMap(_.find(query)
      .cursor[Todo](ReadPreference.primary)
      .collect[Seq]()
    )
  }

  def getTodo(id: BSONObjectID): Future[Option[Todo]] = {
    val query = BSONDocument("_id" -> id)
    todosCollection.flatMap(_.find(query).one[Todo])
  }

  def addTodo(todo: Todo): Future[WriteResult] = {
    todosCollection.flatMap(_.insert(todo))
  }

  def updateTodo(id: BSONObjectID, todo: Todo): Future[Option[Todo]] = {

    val selector = BSONDocument("_id" -> id)
    val updateModifier = BSONDocument(
      "$set" -> BSONDocument(
        "title" -> todo.title,
        "completed" -> todo.completed)
    )

    todosCollection.flatMap(
      _.findAndUpdate(selector, updateModifier, fetchNewObject = true).map(_.result[Todo])
    )
  }

  def deleteTodo(id: BSONObjectID): Future[Option[Todo]] = {
    val selector = BSONDocument("_id" -> id)
    todosCollection.flatMap(_.findAndRemove(selector).map(_.result[Todo]))
  }

}
