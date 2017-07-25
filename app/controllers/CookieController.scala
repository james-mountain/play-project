package controllers

import play.api.mvc._

class CookieController extends Controller {
  def getCookieAction: Action[AnyContent] = Action {
    Ok("Obtained a cookie").withCookies(Cookie(name = "testcookie", value = "cookiedata"))
  }

  def printCookieAction: Action[AnyContent] = Action { implicit request =>
    request.cookies.get("testcookie").map {data =>
      Ok("Cookie found: " + data.toString)
    }.getOrElse {
      Redirect(routes.Application.badRequest(Some("No cookie data found.")))
    }
  }

  def revokedCookieAction: Action[AnyContent] = Action {
    Ok("Revoked cookie").discardingCookies(DiscardingCookie("testcookie"))
  }
}
