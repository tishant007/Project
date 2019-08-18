package DataBaseCreation.Orders

import SellerSide.{OrderId, OrderItemId, Orders, UserId}
import com.google.inject.ImplementedBy
import org.joda.time.DateTime
import slick.driver.PostgresDriver.api._

@ImplementedBy(classOf[OrderImpl])
trait OrderMethod {
  def addOrder(orders: Orders) : DBIO[String]
  def getAllOrdersByUserId(userId: UserId) : DBIO[List[Orders]]
  def getOrderByOrderId(orderId : OrderId) : DBIO[Orders]
  def getAllOrdersByDate(dateTime: DateTime, userId: UserId) : DBIO[List[Orders]]
  def getAllOrdersByOrderItemId(orderItemId: OrderItemId) : DBIO[List[Orders]]
  def checkOrder(orderId: OrderId) : DBIO[Boolean]
}
