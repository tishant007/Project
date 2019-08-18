package DataBaseCreation.OrderAndSubOrder

import DataBaseCreation.DataBase.OrderAndSubOrder
import SellerSide.{OrderId, SubOrderId, SubOrders}
import com.google.inject.ImplementedBy
import slick.driver.PostgresDriver.api._

@ImplementedBy(classOf[OrderAndSubOrderImpl])
trait OrderAndSubOrderMethod {
  def addOrderWithSubOrder(orderAndSubOrder : OrderAndSubOrder) : DBIO[String]
  def getAllSubOrdersByOrderId(orderId: OrderId) : DBIO[List[SubOrders]]
  def getAllOrderBySubOrderId(subOrderId: SubOrderId) : DBIO[List[OrderId]]
}
