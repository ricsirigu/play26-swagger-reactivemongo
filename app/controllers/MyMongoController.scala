package controllers

import javax.inject.Inject

import play.api.libs.json.{JsObject, Json}
import play.api.mvc.{AbstractController, ControllerComponents}
import play.modules.reactivemongo._
import play.modules.reactivemongo.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by Riccardo Sirigu on 07/06/2017.
  */
class MyMongoController @Inject()(
  val reactiveMongoApi: ReactiveMongoApi,
  cc: ControllerComponents) extends AbstractController(cc) with MongoController with ReactiveMongoComponents{

  def test = Action.async {
    val collection: Future[JSONCollection] = database.map(_.collection("users"))
    val query = Json.obj("email" -> "test@gmail.com")
    collection.flatMap(_.find(query).one[JsObject]).map {
      case Some(user) => Ok(user)
      case None => NotFound("email")
    }.recover{case ops => NotFound("email")}
  }
}
