package models

import play.api.data.Form
import play.api.data.Forms._

import scalaz._
import Scalaz._
import scala.collection.mutable.ListBuffer

case class InventoryItem(name: String, desc: String, manufacturer: String, warrantyLength: Int, price: Int)

object InventoryItem {
  val invItemForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "desc" -> nonEmptyText,
      "manufacturer" -> nonEmptyText,
      "warrantyLength" -> number,
      "price" -> number
    )(InventoryItem.apply)(InventoryItem.unapply)
  )

  val inventoryItems = ListBuffer(
    InventoryItem("Computer", "A personal computer.", "DELL", 24, 500),
    InventoryItem("Mouse", "A computer mouse for usage with a computer", "Logitech", 3, 30),
    InventoryItem("System", "A sound system.", "Sony", 24, 200)
  )
}
