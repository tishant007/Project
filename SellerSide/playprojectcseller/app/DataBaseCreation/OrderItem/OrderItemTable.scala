package DataBaseCreation.OrderItem

import slick.driver.PostgresDriver.api._
import SellerSide.{OrderItemId, OrderItems, ProductId}
import com.google.inject.Singleton

@Singleton
class OrderItemTable(tag: Tag) extends Table[OrderItems](tag, "OrderItemTable") {
  def orderItemId = column[OrderItemId]("OrderItemId")

  def productId = column[ProductId]("ProductId")

  def productName = column[String]("ProductName")

  def quantity = column[Int]("Quantity")

  def price = column[BigDecimal]("Price")

  def * = (orderItemId, productId, productName, quantity, price) <> ((OrderItems.apply _).tupled, OrderItems.unapply)
}
