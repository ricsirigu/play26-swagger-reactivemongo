package controllers

import javax.inject.Inject

import io.swagger.annotations._
import models.JsonFormats._
import models.{Todo, TodoRepository}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by Riccardo Sirigu on 10/08/2017.
  */
@Api(value = "/todos")
class TodoController @Inject()(cc: ControllerComponents, todoRepo: TodoRepository) extends AbstractController(cc) {

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
    value = "Add a new Todo to the list",
    response = classOf[Void]
  )
  @ApiImplicitParams(Array(
      new ApiImplicitParam(value = "The Todo to add, in Json Format", required = true, dataType = "models.Todo", paramType = "body")
    )
  )
  def createTodo() = Action.async(parse.json){ req =>
    req.body.validate[Todo].map{ todo =>
      todoRepo.addTodo(todo).map{ _ =>
        Created
      }
    }.getOrElse(Future.successful(BadRequest("Invalid Json")))
  }

  @ApiOperation(
    value = "Updates a Todo",
    response = classOf[Void]
  )
  @ApiImplicitParams(Array(
      new ApiImplicitParam(value = "The updated Todo, in Json Format", required = true, dataType = "models.Todo", paramType = "body")
    )
  )
  def updateTodo(@ApiParam(value = "The id of the Todo to update")
                 todoId: String) = Action.async(parse.json){ req =>
    req.body.validate[Todo].map{ todo =>
      todoRepo.updateTodo(todoId, todo).map{ _ =>
        Ok
      }
    }.getOrElse(Future.successful(BadRequest("Invalid Json")))
  }

  @ApiOperation(
    value = "Deletes a Todo",
    response = classOf[Void]
  )
  def deleteTodo(@ApiParam(value = "The id of the Todo to delete") todoId: String) = Action.async{ req =>
    todoRepo.deleteTodo(todoId).map{ _ =>
      Ok
    }
  }

}
