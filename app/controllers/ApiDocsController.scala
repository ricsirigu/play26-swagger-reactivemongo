package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
  * Created by Riccardo Sirigu on 10/08/2017.
  */
class ApiDocsController @Inject()(cc: ControllerComponents, configuration: Configuration) extends AbstractController(cc) {

  def redirectToDocs = Action {
    val basePath = configuration.underlying.getString("swagger.api.uri")
    Redirect(
      url = "/assets/lib/swagger-ui/index.html",
      queryString = Map("url" -> Seq(s"$basePath/swagger.json"))
    )
  }

}
