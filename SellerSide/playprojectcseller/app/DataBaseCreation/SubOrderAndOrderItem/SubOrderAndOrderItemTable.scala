package DataBaseCreation.SubOrderAndOrderItem

import DataBaseCreation.DataBase.SubOrderAndOrderItem
import SellerSide.{OrderItemId, SubOrderId}
import com.google.inject.Singleton
import slick.driver.PostgresDriver.api._


@Singleton
class SubOrderAndOrderItemTable (tag: Tag) extends Table[SubOrderAndOrderItem](tag, "SubOrderAndOrderItem") {
  def subOrderId = column[SubOrderId]("SubOrderId")

  def orderItemId = column[OrderItemId]("OrderItemId")

  def * = (subOrderId, orderItemId) <> ((SubOrderAndOrderItem.apply _).tupled, SubOrderAndOrderItem.unapply)
}
