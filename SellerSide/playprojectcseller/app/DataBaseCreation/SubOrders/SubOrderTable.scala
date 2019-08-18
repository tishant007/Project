package DataBaseCreation.SubOrders

import DataBaseCreation.DataBase.SubOrderTableFormat
import SellerSide.{SellerId, SubOrderId}
import com.google.inject.Singleton
import slick.driver.PostgresDriver.api._

@Singleton
class SubOrderTable (tag: Tag) extends Table[SubOrderTableFormat](tag, "SubOrderTable"){
  def subOrderId = column[SubOrderId]("SubOrderId")

  def sellerId = column[SellerId]("SellerId")

  def status = column[String]("Status")

  def * = (subOrderId, sellerId, status) <> ((SubOrderTableFormat.apply _).tupled, SubOrderTableFormat.unapply)


}
