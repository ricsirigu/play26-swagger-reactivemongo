package controllers

import javax.inject.Inject

import io.swagger.annotations.{Api, ApiOperation, ApiParam}
import models.TodoRepository
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import reactivemongo.play.json._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Riccardo Sirigu on 10/08/2017.
  */
@Api(value = "/todos")
class TodoController @Inject()(cc: ControllerComponents, todoRepo: TodoRepository) extends AbstractController(cc) {

  @ApiOperation(value = "Find all Todos")
  def getAllTodos = Action.async {
    todoRepo.getAll.map{ todos =>
      Ok(Json.toJson(todos))
    }
  }

  @ApiOperation(value = "Add a new Todo to the list")
  def createTodo() = TODO

  @ApiOperation(value = "Updates a Todo")
  def updateTodo(@ApiParam(value = "The Todo to update") todoId: String) = TODO

  @ApiOperation(value = "Deletes a Todo")
  def deleteTodo(@ApiParam(value = "The Todo to delete") todoId: String) = TODO

}
