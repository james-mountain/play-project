package models

import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.OFormat

import scala.collection.mutable.ListBuffer

case class InventoryItem(id : Int, name: String, desc: String, manufacturer: String, warrantyLength: Int, price: Int)

object InventoryItem {
  def newIDToUse : Int = {
    inventoryItems.lastOption match {
      case Some(last) => last.id + 1;
      case None => 0;
    }
  }

  def formApply(name: String, desc: String, manufacturer: String, warrantyLength: Int, price: Int): InventoryItem = {
    InventoryItem(newIDToUse, name, desc, manufacturer, warrantyLength, price)
  }

  def formUnapply(invitem : InventoryItem): Option[(String, String, String, Int, Int)] = {
    Some(invitem.name, invitem.desc, invitem.manufacturer, invitem.warrantyLength, invitem.price)
  }

  val invItemForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "desc" -> nonEmptyText,
      "manufacturer" -> nonEmptyText,
      "warrantyLength" -> number,
      "price" -> number
    )(InventoryItem.formApply)(InventoryItem.formUnapply)
  )

  val inventoryItems = ListBuffer(
    InventoryItem(1, "Computer", "A personal computer.", "DELL", 24, 500),
    InventoryItem(2, "Mouse", "A computer mouse for usage with a computer", "Logitech", 3, 30),
    InventoryItem(3, "System", "A sound system.", "Sony", 24, 200)
  )
}

object InventoryItemJsonFormats {
  import play.api.libs.json.Json

  implicit val inventoryItemFormat: OFormat[InventoryItem] = Json.format[InventoryItem]
}