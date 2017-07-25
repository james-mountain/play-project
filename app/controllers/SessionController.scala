package controllers

import play.api.mvc._

class SessionController extends Controller {
  def addToSessionAction: Action[AnyContent] = Action {
    Ok("Added to session").withSession("sessiondata" -> "somedataforsession")
  }

  def printSessionContentsAction: Action[AnyContent] = Action { implicit request =>
    request.session.get("sessiondata").map {data =>
      Ok("Session data found: " + data)
    }.getOrElse {
      Redirect(routes.Application.badRequest(Some("No session data found.")))
    }
  }

  def removeFromSessionAction: Action[AnyContent] = Action { implicit request =>
    Ok("Removed data from session").withSession(request.session - "sessiondata")
  }
}
