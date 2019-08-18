package DataBaseCreation.SubOrderAndOrderItem

import DataBaseCreation.DataBase.SubOrderAndOrderItem
import SellerSide.{OrderItemId, OrderItems, SubOrderId}
import com.google.inject.ImplementedBy
import slick.driver.PostgresDriver.api._

@ImplementedBy(classOf[SubOrderAndOrderItemImpl])
trait SubOrderAndOrderItemMethod {
  def addSubOrderWithOrderItem(subOrderAndOrderItem : SubOrderAndOrderItem) : DBIO[String]
  def getAllOrderItemBySubOrderId(subOrderId: SubOrderId) : DBIO[List[OrderItems]]
  def getAllSubOrdersByOrderItemId(orderItemId: OrderItemId) : DBIO[List[SubOrderId]]
}
