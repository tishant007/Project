package DataBaseCreation.SubOrders

import DataBaseCreation.DataBase.SubOrderTableFormat
import SellerSide.{SubOrderId, SubOrders}
import com.google.inject.ImplementedBy
import slick.driver.PostgresDriver.api._


@ImplementedBy(classOf[SubOrderImpl])
trait SubOrderMethod {
  def addSubOrder(subOrders: SubOrders) : DBIO[String]
  def getSubOrderBySubOrderId(subOrderId: SubOrderId) : DBIO[SubOrderTableFormat]
  def updateSubOrderStatus(status : String, subOrderId: SubOrderId) : DBIO[String]
}
