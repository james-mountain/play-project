package models

import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.OFormat
import play.api.libs.json.Json

case class InventoryItem(id : Int, name: String, desc: String, manufacturer: String, warrantyLength: Int, price: Int)

object InventoryItem {
  def formApply(name: String, desc: String, manufacturer: String, warrantyLength: Int, price: Int): InventoryItem = {
    InventoryItem(0, name, desc, manufacturer, warrantyLength, price)
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

  implicit val inventoryItemFormat: OFormat[InventoryItem] = Json.format[InventoryItem]
}