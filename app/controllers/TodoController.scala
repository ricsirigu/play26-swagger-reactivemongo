package controllers

import java.util.UUID

import javax.inject.Inject

import io.swagger.annotations._

import models.{Todo, TodoRepository}

import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by Riccardo Sirigu on 10/08/2017.
  */
@Api(value = "/todos")
class TodoController @Inject()(
  cc: ControllerComponents,
  todoRepo: TodoRepository) extends AbstractController(cc) {

  @ApiOperation(
    value = "Find all Todos",
    response = classOf[Todo],
    responseContainer = "List"
  )
  def getAllTodos = Action.async {
    todoRepo.getAll.map{ todos =>
      Ok(Json.toJson(todos))
    }
  }


  @ApiOperation(
    value = "Get a Todo",
    response = classOf[Todo]
  )
  @ApiResponses(Array(
      new ApiResponse(code = 404, message = "Todo not found")
    )
  )
  def getTodo(@ApiParam(value = "The id of the Todo to fetch") todoId: UUID) =
    Action.async { req =>
      todoRepo.getTodo(todoId).map{ maybeTodo =>
        maybeTodo.map { todo =>
          Ok(Json.toJson(todo))
        }.getOrElse(NotFound)
      }
    }

  @ApiOperation(
    value = "Add a new Todo to the list",
    response = classOf[Void],
    code = 201
  )
  @ApiResponses(Array(
      new ApiResponse(code = 400, message = "Invalid Todo format")
    )
  )
  @ApiImplicitParams(Array(
      new ApiImplicitParam(value = "The Todo to add, in Json Format", required = true, dataType = "models.Todo", paramType = "body")
    )
  )
  def createTodo() = Action.async(parse.json) {
    _.body.validate[Todo].map { todo =>
      todoRepo.addTodo(todo).map { _ =>
        Created
      }
    }.getOrElse(Future.successful(BadRequest("Invalid Todo format")))
  }

  @ApiOperation(
    value = "Update a Todo",
    response = classOf[Todo]
  )
  @ApiResponses(Array(
      new ApiResponse(code = 400, message = "Invalid Todo format")
    )
  )
  @ApiImplicitParams(Array(
      new ApiImplicitParam(value = "The updated Todo, in Json Format", required = true, dataType = "models.Todo", paramType = "body")
    )
  )
  def updateTodo(@ApiParam(value = "The id of the Todo to update")
                 todoId: UUID) = Action.async(parse.json) { req =>
    req.body.validate[Todo].map { todo =>
      todoRepo.updateTodo(todoId, todo).map {
        case Some(todo) => Ok(Json.toJson(todo))
        case _ => NotFound
      }
    }.getOrElse(Future.successful(BadRequest("Invalid Json")))
  }

  @ApiOperation(
    value = "Delete a Todo",
    response = classOf[Todo]
  )
  def deleteTodo(@ApiParam(value = "The id of the Todo to delete") todoId: UUID) = Action.async { req =>
    todoRepo.deleteTodo(todoId).map {
      case Some(todo) => Ok(Json.toJson(todo))
      case _ => NotFound
    }
  }
}
