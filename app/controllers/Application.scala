package controllers

import play.api._
import play.api.libs.json.Json
import play.api.mvc._

class Application extends Controller {
  val teapotCode = 418

  def index: Action[AnyContent] = Action {
    Ok(views.html.index())
  }

  def staticprint : Action[AnyContent] = Action { implicit request =>
    request.session.get("username").map {data =>
      Ok(views.html.message("Hello", "Hello " + data + "!"))
    }.getOrElse {
      Ok(views.html.message("Hello Unknown Person", "Hello whoever that may be."))
    }
  }

  def print(name : String) : Action[AnyContent] = Action {
    Ok(views.html.message("Hello", "Hello " + name + "!"))
  }

  def optionalPrint(option : Option[String]) : Action[AnyContent] = Action {
    option match {
      case Some(str) => Ok(views.html.message("Option Found", "Option found: " + str))
      case None => Ok(views.html.message("No Option Found", "No option found!"))
    }
  }

  def notFoundAction : Action[AnyContent] = Action {
    NotFound(views.html.notfound())
  }

  def badRequest(requestMessage : Option[String]) : Action[AnyContent] = Action {
    BadRequest(views.html.badreq(requestMessage.getOrElse("Unknown bad request.")))
  }

  def internalServerError : Action[AnyContent] = Action {
    InternalServerError(views.html.intserverror())
  }

  def teapot : Action[AnyContent] = Action {
    Status(teapotCode)(views.html.message("Teapot Request!", "Teapot request found."))
  }

  def unfinishedPage: Action[AnyContent] = TODO

  def firstAction: Action[AnyContent] = Action {
    Ok(views.html.message("First Action", "The first action was called."))
  }

  def secondAction: Action[AnyContent] = Action {
    Ok(views.html.message("Second Action", "The second action was called."))
  }

  def redirectionToGenericName: Action[AnyContent] = Action {
    Redirect(routes.Application.print("genericname"))
  }

}