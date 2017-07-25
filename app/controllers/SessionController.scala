package controllers

import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

class SessionController extends Controller {
  def addToSessionAction: Action[AnyContent] = Action { implicit request =>
    val body: AnyContent = request.body
    val jsonBody: Option[JsValue] = body.asJson

    jsonBody.map { json =>
      Ok(Json.obj("message" -> "success")).withSession("username" -> (json \ "username").as[String])
    }.getOrElse {
      BadRequest(views.html.badreq("No application/json request body"))
    }
  }

  def printSessionContentsAction: Action[AnyContent] = Action { implicit request =>
    request.session.get("username").map {data =>
      Ok(Json.obj("message" -> "success", "username" -> data))
    }.getOrElse {
      Ok(Json.obj("message" -> "failure"))
    }
  }

  def removeFromSessionAction: Action[AnyContent] = Action { implicit request =>
    Ok(Json.obj("message" -> "success")).withSession(request.session - "username")
  }
}
