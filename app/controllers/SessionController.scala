package controllers

import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

class SessionController extends Controller {
  val usernames = List("Peter", "Test", "Alex")

  def loginUserToSession: Action[AnyContent] = Action { implicit request =>
    val body: AnyContent = request.body
    val jsonBody: Option[JsValue] = body.asJson

    jsonBody.fold(BadRequest(views.html.badreq("No application/json request body"))) { json => {
      if (usernames.contains((json \ "username").as[String])) {
        Ok(Json.obj("message" -> "success")).withSession("username" -> (json \ "username").as[String])
      } else {
        Ok(Json.obj("message" -> "failure"))
      }
    }
    }
  }

  def printSessionContentsAction: Action[AnyContent] = Action { implicit request =>
    request.session.get("username").fold(Ok(Json.obj("message" -> "failure"))) { data =>
      Ok(Json.obj("message" -> "success", "username" -> data))
    }
  }

  def logoutUserFromSession: Action[AnyContent] = Action { implicit request =>
    Ok(Json.obj("message" -> "success")).withSession(request.session - "username")
  }
}
