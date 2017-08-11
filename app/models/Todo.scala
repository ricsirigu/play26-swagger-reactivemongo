package models

import javax.inject.Inject

import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.ReadPreference
import reactivemongo.api.commands.{UpdateWriteResult, WriteResult}
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

  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("todos"))

  def getAll: Future[Seq[Todo]] = {
    val query = Json.obj()
    collection.flatMap(_.find(query)
      .cursor[Todo](ReadPreference.primary)
      .collect[Seq]()
    )
  }

  def getTodo(id: BSONObjectID): Future[Option[Todo]] = {
    val query = BSONDocument("_id" -> id)
    collection.flatMap(_.find(query).one[Todo])
  }

  def addTodo(todo: Todo): Future[WriteResult] = {
    collection.flatMap(_.insert(todo))
  }

  def updateTodo(id: BSONObjectID, todo: Todo): Future[UpdateWriteResult] = {
    val selector = BSONDocument("_id" -> id)
    val modifier = BSONDocument(
      "$set" -> BSONDocument(
        "title" -> todo.title,
        "completed" -> todo.completed))

    collection.flatMap(_.update(selector, modifier))
  }

  def deleteTodo(id: BSONObjectID): Future[WriteResult] = {
    val selector = BSONDocument("_id" -> id)
    collection.flatMap(_.remove(selector))
  }

}
