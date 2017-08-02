package utils.action

import play.api.mvc.{ActionBuilder, Request, Result, Results}
import utils.request.AuthRequest

import scala.concurrent.Future

/**
  * Created by Administrator on 02/08/2017.
  */
object AuthAction extends ActionBuilder[AuthRequest] {
  override def invokeBlock[A](request: Request[A], block: (AuthRequest[A])
    => Future[Result]): Future[Result] = {
    request.session.get("username")
      .filter(List("Peter", "Test", "Alex").contains(_))
      .map(user => block(new AuthRequest(user, request)))
      .getOrElse(Future.successful(Results.Forbidden))
  }
}
