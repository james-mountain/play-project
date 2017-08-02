package utils.request

import play.api.mvc.{Request, WrappedRequest}

/**
  * Created by Administrator on 02/08/2017.
  */
class AuthRequest[A](val user: String, val request: Request[A]) extends WrappedRequest[A](request)
