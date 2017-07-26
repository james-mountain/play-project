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

  def processOverrideID(ovid : Option[Int], correctForm : InventoryItem) : Unit = ovid match {
    case Some(oid) => {
      val existingItemIndex = InventoryItem.inventoryItems.indexWhere(invitem => invitem.id == oid)
      val newInvItem = InventoryItem.inventoryItems(existingItemIndex).copy(
        oid, correctForm.name, correctForm.desc, correctForm.manufacturer, correctForm.warrantyLength, correctForm.price
      )
      InventoryItem.inventoryItems.update(existingItemIndex, newInvItem)
    }
    case None => InventoryItem.inventoryItems.append(correctForm)
  }

  def postFormAction(overrideID: Option[Int]): Action[AnyContent] = Action { implicit request =>
    val formValidationResult = InventoryItem.invItemForm.bindFromRequest

    formValidationResult.fold({ errorForm =>
      BadRequest(views.html.invitems(InventoryItem.inventoryItems.toList, errorForm, overrideID))
    }, { correctForm =>
      processOverrideID(overrideID, correctForm)

      Redirect(routes.InventoryItemController.listInventoryItems())
    })
  }
}
