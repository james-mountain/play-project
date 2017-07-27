package controllers

import javax.inject.Inject

import models.InventoryItem
import models.InventoryItemJsonFormats._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import play.api.libs.json._
import reactivemongo.play.json._
import reactivemongo.api.Cursor
import reactivemongo.api.commands.WriteResult
import reactivemongo.play.json.collection.JSONCollection

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import scala.util.{Failure, Success}

class InventoryItemController @Inject()(val messagesApi: MessagesApi)(val reactiveMongoApi : ReactiveMongoApi)
  extends Controller with I18nSupport with MongoController with ReactiveMongoComponents {

  implicit val inventoryItemWrites: OWrites[InventoryItem] = Json.writes[InventoryItem]

  def reload : Result = Redirect(routes.InventoryItemController.listInventoryItems())
  def collection: Future[JSONCollection] = database.map(_.collection[JSONCollection]("inventory"))

  def futureInventoryItems: Future[List[InventoryItem]] = {
    val futureCursor: Future[Cursor[InventoryItem]] = collection.map {_.find(Json.obj()).cursor[InventoryItem]()}

    futureCursor.flatMap(_.collect[List]())
  }

  def listInventoryItems: Action[AnyContent] = Action.async { implicit request =>
    futureInventoryItems.map {
      inventoryItems => Ok(views.html.invitems(inventoryItems, InventoryItem.invItemForm, None))
    }
  }

  def removeInvItemByID(id : Int): Future[Future[WriteResult]] = collection.map {_.remove(Json.obj("id" -> id))}

  def deleteInvItem(id : Int): Action[AnyContent] = Action.async { implicit request =>
    removeInvItemByID(id).map(_ => reload)
  }

  def startEditInvItem(id : Int) : Action[AnyContent] = Action.async { implicit request =>
    val futureIDCursor: Future[Cursor[InventoryItem]] = collection.map {_.find(Json.obj("id" -> id)).cursor[InventoryItem]()}
    val futureIDList : Future[List[InventoryItem]] = futureIDCursor.flatMap(_.collect[List]())

    futureIDList.flatMap {
      idItems => idItems.headOption match {
        case Some(item) => futureInventoryItems.map {
          inventoryItems => Ok(views.html.invitems(inventoryItems, InventoryItem.invItemForm.fill(item), Some(id)))
        }
        case None => futureInventoryItems.map {
          inventoryItems => BadRequest(views.html.invitems(inventoryItems, InventoryItem.invItemForm, None))
        }
      }
    }
  }

  def aggregateHighestID(col : JSONCollection): Future[Option[JsLookupResult]] = {
    import col.BatchCommands.AggregationFramework.{
      Group, MaxField
    }

    col.aggregate(Group(JsNull)("highestID" -> MaxField("id"))).map(_.firstBatch.headOption match {
      case Some(res) => Some(res \ "highestID")
      case None => None
    })
  }

  def insertNewInvItem(correctForm : InventoryItem): Future[Result] = {
    val highestID: Future[Int] = collection.flatMap(aggregateHighestID).map {
      case Some(v) => v.as[Int] + 1
      case None => 0
    }

    highestID.flatMap { highID =>
      collection.flatMap(_.insert(correctForm.copy(id = highID)).map(_ => reload))
    }
  }

  def updateInvItem(ovid : Int, correctForm : InventoryItem): Future[Result] = collection.map {
    val updatevals = Json.obj(
      "name" -> correctForm.name,
      "desc" -> correctForm.desc,
      "manufacturer" -> correctForm.manufacturer,
      "price" -> correctForm.price,
      "warrantyLength" -> correctForm.warrantyLength
    )
    _.update(Json.obj("id" -> ovid), Json.obj("$set" -> updatevals))
  }.map(_ => reload)


  def processOverrideID(ovid : Option[Int], correctForm : InventoryItem) : Future[Result] = ovid match {
    case Some(oid) => updateInvItem(oid, correctForm)
    case None => insertNewInvItem(correctForm)
  }

  def postFormAction(overrideID: Option[Int]): Action[AnyContent] = Action.async { implicit request =>
    val formValidationResult = InventoryItem.invItemForm.bindFromRequest

    formValidationResult.fold({ errorForm =>
      futureInventoryItems.map {
        inventoryItems => BadRequest(views.html.invitems(inventoryItems, errorForm, overrideID))
      }
    }, { correctForm =>
      processOverrideID(overrideID, correctForm)
    })
  }
}
