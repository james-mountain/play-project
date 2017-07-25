package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {
  val teapotCode = 418

  def index: Action[AnyContent] = Action {
    Ok(views.html.index())
  }

  def staticprint : Action[AnyContent] = Action {
    Ok("Hello whoever that may be")
  }

  def print(name : String) : Action[AnyContent] = Action {
    Ok("Hello " + name)
  }

  def optionalPrint(option : Option[String]) : Action[AnyContent] = Action {
    option match {
      case Some(str) => Ok("Option found: " + str)
      case None => Ok("No option found!")
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
    Status(teapotCode)(<h1>Teapot!</h1>).as(HTML)
  }

  def unfinishedPage: Action[AnyContent] = TODO

  def firstAction: Action[AnyContent] = Action {
    Ok("First Action")
  }

  def secondAction: Action[AnyContent] = Action {
    Ok("Second Action")
  }

  def redirectionToGenericName: Action[AnyContent] = Action {
    Redirect(routes.Application.print("genericname"))
  }

}