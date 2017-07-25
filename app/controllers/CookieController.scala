package controllers

import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

class CookieController extends Controller {
  def getCookieAction: Action[AnyContent] = Action {
    Ok(Json.obj("message" -> "success")).withCookies(Cookie(name = "testcookie", value = "cookiedata"))
  }

  def printCookieAction: Action[AnyContent] = Action { implicit request =>
    request.cookies.get("testcookie").map {data =>
      Ok(Json.obj("message" -> "success", "cookiename" -> data.name, "cookievalue" -> data.value))
    }.getOrElse {
      Ok(Json.obj("message" -> "failure"))
    }
  }

  def revokedCookieAction: Action[AnyContent] = Action {
    Ok(Json.obj("message" -> "success")).discardingCookies(DiscardingCookie("testcookie"))
  }
}
