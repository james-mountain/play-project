package controllers

import play.api.libs.json.Json
import play.api.mvc._

class SessionController extends Controller {
  def addToSessionAction: Action[AnyContent] = Action {
    Ok(Json.obj("message" -> "success")).withSession("sessiondata" -> "somedataforsession")
  }

  def printSessionContentsAction: Action[AnyContent] = Action { implicit request =>
    request.session.get("sessiondata").map {data =>
      Ok(Json.obj("message" -> "success", "sessiondata" -> data))
    }.getOrElse {
      Ok(Json.obj("message" -> "failure"))
    }
  }

  def removeFromSessionAction: Action[AnyContent] = Action { implicit request =>
    Ok(Json.obj("message" -> "success")).withSession(request.session - "sessiondata")
  }
}
