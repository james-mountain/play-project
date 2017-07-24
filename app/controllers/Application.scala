package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {

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

  def redirectionToGenericName: Action[AnyContent] = Action {
    Redirect(routes.Application.print("genericname"))
  }

}