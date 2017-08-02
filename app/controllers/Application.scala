package controllers

import javax.inject.Inject

import utils.action.{AuthAction, LoggingAuthAction}
import akka.stream.{Materializer, ThrottleMode}
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.util.ByteString
import play.api.http.{ContentTypes, HttpEntity}
import play.api.libs.Comet
import play.api.mvc._
import play.mvc.Security.AuthenticatedAction

import scala.concurrent.duration._

class Application @Inject()(materializer: Materializer) extends Controller {
  val teapotCode = 418
  val okCode = 200
  val evenNumbers = 100

  def index: Action[AnyContent] = Action {
    Ok(views.html.index())
  }

  def helloResult(personName : String): Result = Ok(views.html.message("Hello", "Hello " + personName + "!"))

  def userConditionalPrint: Action[AnyContent] = Action { implicit request =>
    request.session.get("username").fold(Ok(views.html.message("Hello Unknown Person", "Hello whoever that may be."))) { data =>
      helloResult(data)
    }
  }

  def authRequest: Action[AnyContent] = AuthAction { implicit request =>
    Ok(s"Hello ${request.user}")
  }

  def loggedAuthRequest: Action[AnyContent] = LoggingAuthAction.action { implicit request =>
    Ok(s"Hello ${request.user}, this is a logged auth request!")
  }

  def dynamicPrint(name : String) : Action[AnyContent] = Action {
    helloResult(name)
  }

  def optionalPrint(option : Option[String]) : Action[AnyContent] = Action {
    option match {
      case Some(str) => Ok(views.html.message("Option Found", "Option found: " + str))
      case None => Ok(views.html.message("No Option Found", "No option found!"))
    }
  }

  def notFoundAction : Action[AnyContent] = Action {
    NotFound(views.html.notfound())
  }

  def badRequest(requestMessage : Option[String]) : Action[AnyContent] = Action {
    BadRequest(views.html.badreq(requestMessage.getOrElse("Unknown bad request.")))
  }

  def internalServerError : Action[AnyContent] = Action {
    InternalServerError(views.html.intserverror())
  }

  def teapot : Action[AnyContent] = Action {
    Status(teapotCode)(views.html.message("Teapot Request!", "Teapot request found."))
  }

  def unfinishedPage: Action[AnyContent] = TODO

  def firstAction: Action[AnyContent] = Action {
    Ok(views.html.message("First Action", "The first action was called."))
  }

  def secondAction: Action[AnyContent] = Action {
    Ok(views.html.message("Second Action", "The second action was called."))
  }

  def redirectionToGenericName: Action[AnyContent] = Action {
    Redirect(routes.Application.dynamicPrint("genericname"))
  }

  def serverRenamedFile: Action[AnyContent] = Action {
    Ok.sendFile(
      content = new java.io.File("./public/images/favicon.png"),
      fileName = _ => "icon.png"
    )
  }

  def codeReviewPDFWithLength: Action[AnyContent] = Action {
    val file = new java.io.File("./public/pdfs/codereview.pdf")
    val path: java.nio.file.Path = file.toPath
    val source: Source[ByteString, _] = FileIO.fromPath(path)

    val contentLength = Some(file.length())

    Result(
      header = ResponseHeader(okCode, Map.empty),
      body = HttpEntity.Streamed(source, contentLength, Some("application/pdf"))
    )
  }

  def cometPage: Action[AnyContent] = Action {
    Ok(views.html.comet())
  }

  def cometEvenNumbers: Action[AnyContent] = Action {
    implicit val m = materializer
    def stringSource : Source[String, _] =
      Source(Stream.from(1).take(evenNumbers).filter(x => x % 2 == 0).map(i => i.toString)).throttle(1, 100.millis, 1, ThrottleMode.shaping)

    Ok.chunked(stringSource via Comet.string("parent.cometMessage")).as(ContentTypes.HTML)
  }
}