package controllers

import javax.inject.Inject

import models.InventoryItem
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import play.api.libs.json._
import reactivemongo.play.json._
import reactivemongo.api.Cursor
import reactivemongo.api.commands.WriteResult
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future

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
      idItems => futureInventoryItems.map {
        inventoryItems => idItems.headOption.fold(
          BadRequest(views.html.invitems(inventoryItems, InventoryItem.invItemForm, None))
        )(res => Ok(views.html.invitems(inventoryItems, InventoryItem.invItemForm.fill(res), Some(id))))
      }
    }
  }

  def aggregateHighestID(col : JSONCollection): Future[Option[JsLookupResult]] = {
    import col.BatchCommands.AggregationFramework.{Group, MaxField}

    col.aggregate(Group(JsNull)("highestID" -> MaxField("id"))).map(_.firstBatch.headOption.map(res => res \ "highestID"))
  }

  def insertNewInvItem(correctForm : InventoryItem): Future[Result] = {
    val highestID: Future[Int] = collection.flatMap(aggregateHighestID).map(lk => lk.fold(0)(res => res.as[Int] + 1))

    highestID.flatMap { highID =>
      collection.flatMap(_.insert(correctForm.copy(id = highID)).map(_ => reload))
    }
  }

  def updateInvItem(ovid : Int, correctForm : InventoryItem): Future[Result] = collection.map {
    _.update(Json.obj("id" -> ovid), correctForm.copy(id = ovid))
  }.map(_ => reload)

  def processOverrideID(ovid : Option[Int], correctForm : InventoryItem) : Future[Result] =
    ovid.fold(insertNewInvItem(correctForm))(oid => updateInvItem(oid, correctForm))

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
