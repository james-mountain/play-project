package controllers

import javax.inject.Inject

import models.InventoryItem
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

/**
  * Created by Administrator on 26/07/2017.
  */
class InventoryItemController @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {
  def listInventoryItems: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.invitems(InventoryItem.inventoryItems.toList, InventoryItem.invItemForm))
  }

  def postFormAction: Action[AnyContent] = Action { implicit request =>
    val formValidationResult = InventoryItem.invItemForm.bindFromRequest

    formValidationResult.fold({ errorForm =>
      BadRequest(views.html.invitems(InventoryItem.inventoryItems.toList, errorForm))
    }, { correctForm =>
      InventoryItem.inventoryItems.append(correctForm)
      Redirect(routes.InventoryItemController.listInventoryItems())
    })
  }
}
