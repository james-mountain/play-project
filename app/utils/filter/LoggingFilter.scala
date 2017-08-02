package utils.filter

import javax.inject.Inject

import akka.stream.Materializer
import play.api.Logger
import play.api.mvc.{Filter, RequestHeader, Result}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Administrator on 02/08/2017.
  */
class LoggingFilter @Inject() (implicit val mat: Materializer, ec: ExecutionContext) extends Filter {
  override def apply(f: (RequestHeader) => Future[Result])(rh: RequestHeader): Future[Result] = {
    val startTime = System.currentTimeMillis()

    f(rh).map { result =>
      val requestTime = System.currentTimeMillis() - startTime

      Logger.info(s"${rh.method} ${rh.uri} took ${requestTime}ms and return ${result.header.status}")
      result.withHeaders("Request-Time" -> requestTime.toString)
    }
  }
}
