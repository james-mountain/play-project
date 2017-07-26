package controllers

import javax.inject.Inject

import models.InventoryItem
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

import scala.collection.mutable.ListBuffer

class InventoryItemController @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {
  def listInventoryItems: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.invitems(InventoryItem.inventoryItems.toList, InventoryItem.invItemForm, None))
  }

  def removeInvItemByID(id : Int): ListBuffer[InventoryItem] = {
    InventoryItem.inventoryItems.find(invitem => invitem.id == id) match {
      case Some(item) => InventoryItem.inventoryItems -= item
      case None => InventoryItem.inventoryItems;
    }
  }

  def deleteInvItem(id : Int): Action[AnyContent] = Action { implicit request =>
    if (InventoryItem.inventoryItems.length != removeInvItemByID(id).length) {
      Ok(views.html.invitems(InventoryItem.inventoryItems.toList, InventoryItem.invItemForm, None))
    } else {
      BadRequest(views.html.invitems(InventoryItem.inventoryItems.toList, InventoryItem.invItemForm, None))
    }
  }

  def startEditInvItem(id : Int) : Action[AnyContent] = Action { implicit request =>
    InventoryItem.inventoryItems.find(invitem => invitem.id == id) match {
      case Some(item) => Ok(views.html.invitems(InventoryItem.inventoryItems.toList, InventoryItem.invItemForm.fill(item), Some(id)))
      case None => BadRequest(views.html.invitems(InventoryItem.inventoryItems.toList, InventoryItem.invItemForm, None))
    }
  }

  def postFormAction(overrideID: Option[Int]): Action[AnyContent] = Action { implicit request =>
    val formValidationResult = InventoryItem.invItemForm.bindFromRequest

    formValidationResult.fold({ errorForm =>
      BadRequest(views.html.invitems(InventoryItem.inventoryItems.toList, errorForm, overrideID))
    }, { correctForm =>
      overrideID match {
        case Some(id) => InventoryItem.inventoryItems.update(InventoryItem.inventoryItems.indexWhere(item => item.id == id), correctForm)
        case None => InventoryItem.inventoryItems.append(correctForm)
      }

      Redirect(routes.InventoryItemController.listInventoryItems())
    })
  }
}
