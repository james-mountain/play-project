package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {
  val teapotCode = 418

  def index: Action[AnyContent] = Action {
    Ok("Hello World")
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
    NotFound(<h1>Not Found!</h1>).as(HTML)
  }

  def badRequest : Action[AnyContent] = Action {
    BadRequest(<h1>Bad Request!</h1>).as(HTML)
  }

  def internalServerError : Action[AnyContent] = Action {
    InternalServerError(<h1>Internal Server Error!</h1>).as(HTML)
  }

  def teapot : Action[AnyContent] = Action {
    Status(teapotCode)(<h1>Teapot!</h1>).as(HTML)
  }

  def unfinishedPage: Action[AnyContent] = TODO

  def redirectionToGenericName: Action[AnyContent] = Action {
    Redirect(routes.Application.print("genericname"))
  }

}