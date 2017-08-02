package utils.action

import play.api.Logger
import play.api.mvc.{ActionBuilder, Request, Result}

import scala.concurrent.Future

/**
  * Created by Administrator on 02/08/2017.
  */
object LoggingAction extends ActionBuilder[Request] {
  override def invokeBlock[A](request: Request[A],
                              block: (Request[A]) => Future[Result]): Future[Result] = {
    Logger.info("Generic Logging Action")
    block(request)
  }
}
