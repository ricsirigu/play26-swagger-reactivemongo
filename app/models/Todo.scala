package models

import javax.inject.Inject

import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.ReadPreference
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Riccardo Sirigu on 10/08/2017.
  */

case class Todo(id: String, title: String, completed: Boolean, order: Int, uri: String)

class TodoRepository @Inject()(implicit ec: ExecutionContext, reactiveMongoApi: ReactiveMongoApi){

  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("todos"))

  def getAll: Future[Seq[BSONDocument]] = {
    val query = Json.obj()
    collection.flatMap(_.find(query)
      .cursor[BSONDocument](ReadPreference.primary)
      .collect[Seq]()
    )
  }

}
