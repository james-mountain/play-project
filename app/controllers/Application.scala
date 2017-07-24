package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {

  def index: Action[AnyContent] = Action {
    Ok("Hello World")
  }

  def print(name : String) : Action[AnyContent] = Action {
    Ok("Hello" + name)
  }

}