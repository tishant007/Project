package DataBaseCreation.OrderAndSubOrder

import DataBaseCreation.DataBase.OrderAndSubOrder
import SellerSide.{OrderId, SubOrderId, SubOrders}
import com.google.inject.{Inject, Singleton}
import slick.driver.PostgresDriver.api._

@Singleton
class OrderAndSubOrderTable(tag: Tag) extends Table[OrderAndSubOrder](tag, "OrderAndSubOrder"){
  def orderId = column[OrderId]("OrderId")

  def subOrderId = column[SubOrderId]("SubOrderId")

  def * = (orderId, subOrderId) <> ((OrderAndSubOrder.apply _).tupled, OrderAndSubOrder.unapply)
}
