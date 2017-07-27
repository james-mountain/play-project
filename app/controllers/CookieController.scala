package controllers

import play.api.libs.json.Json
import play.api.mvc._

class CookieController extends Controller {
  def getCookieAction: Action[AnyContent] = Action {
    Ok(Json.obj("message" -> "success")).withCookies(Cookie(name = "testcookie", value = "cookiedata"))
  }

  def printCookieAction: Action[AnyContent] = Action { implicit request =>
    request.cookies.get("testcookie").fold(Ok(Json.obj("message" -> "failure"))) { data =>
      Ok(Json.obj("message" -> "success", "cookiename" -> data.name, "cookievalue" -> data.value))
    }
  }

  def revokedCookieAction: Action[AnyContent] = Action {
    Ok(Json.obj("message" -> "success")).discardingCookies(DiscardingCookie("testcookie"))
  }
}
