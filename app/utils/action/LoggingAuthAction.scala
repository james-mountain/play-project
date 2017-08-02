package utils.action

import play.api.mvc.ActionBuilder
import utils.request.AuthRequest

/**
  * Created by Administrator on 02/08/2017.
  */
object LoggingAuthAction {
  val action: ActionBuilder[AuthRequest] = LoggingAction andThen AuthAction
}
