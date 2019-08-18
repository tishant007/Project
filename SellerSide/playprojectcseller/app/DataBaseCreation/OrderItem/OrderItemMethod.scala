package DataBaseCreation.OrderItem

import SellerSide.{OrderItemId, OrderItems}
import com.google.inject.ImplementedBy
import slick.driver.PostgresDriver.api._

@ImplementedBy(classOf[OrderItemImpl])
trait OrderItemMethod {
  def addOrderItem(orderItems : OrderItems) : DBIO[String]
  def getOrderItemByOrderItemId(orderItemId: OrderItemId) : DBIO[OrderItems]
}
