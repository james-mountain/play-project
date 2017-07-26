package controllers

import javax.inject.Inject

import models.InventoryItem
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

import scala.collection.mutable.ListBuffer

/**
  * Created by Administrator on 26/07/2017.
  */
class InventoryItemController @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {
  def listInventoryItems: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.invitems(InventoryItem.inventoryItems.toList, InventoryItem.invItemForm, None))
  }

  def removeInvItemByName(name : String): ListBuffer[InventoryItem] = {
    InventoryItem.inventoryItems.find(invitem => invitem.name == name) match {
      case Some(item) => InventoryItem.inventoryItems -= item
      case None => InventoryItem.inventoryItems;
    }
  }

  def deleteInvItem(name : String): Action[AnyContent] = Action { implicit request =>
    if (InventoryItem.inventoryItems.length != removeInvItemByName(name).length) {
      Ok(views.html.invitems(InventoryItem.inventoryItems.toList, InventoryItem.invItemForm, None))
    } else {
      BadRequest(views.html.invitems(InventoryItem.inventoryItems.toList, InventoryItem.invItemForm, None))
    }
  }

  def postFormAction: Action[AnyContent] = Action { implicit request =>
    val formValidationResult = InventoryItem.invItemForm.bindFromRequest

    formValidationResult.fold({ errorForm =>
      BadRequest(views.html.invitems(InventoryItem.inventoryItems.toList, errorForm, None))
    }, { correctForm =>
      InventoryItem.inventoryItems.append(correctForm)
      Redirect(routes.InventoryItemController.listInventoryItems())
    })
  }
}
