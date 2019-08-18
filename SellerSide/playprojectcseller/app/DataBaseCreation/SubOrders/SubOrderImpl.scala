package DataBaseCreation.SubOrders

import DataBaseCreation.DataBase.{SubOrderAndOrderItem, SubOrderTableFormat}
import DataBaseCreation.OrderItem.OrderItemMethod
import DataBaseCreation.SubOrderAndOrderItem.SubOrderAndOrderItemMethod
import SellerSide.{SubOrderId, SubOrders}
import com.google.inject.{Inject, Singleton}
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class SubOrderImpl @Inject()(orderItemMethod: OrderItemMethod,
                             subOrderAndOrderItemMethod: SubOrderAndOrderItemMethod) extends SubOrderMethod {
  private val subOrderTableQuery = TableQuery[SubOrderTable]

  private def createTable = {
    Database.forConfig("mydb").run(subOrderTableQuery.schema.create)
  }

  override def addSubOrder(subOrders: SubOrders): DBIO[String] = {
    (for{
      _ <- subOrderTableQuery += SubOrderTableFormat(subOrders.subOrderId, subOrders.sellerId, subOrders.status)
    } yield {
      subOrders.listOfOrderItems.map{ orderItems =>
        orderItemMethod.addOrderItem(orderItems)
        subOrderAndOrderItemMethod.addSubOrderWithOrderItem(SubOrderAndOrderItem(subOrders.subOrderId, orderItems.orderItemId))
      }
      "SubOrders have been Successfully Added"
    }).transactionally
  }

  override def getSubOrderBySubOrderId(subOrderId: SubOrderId): DBIO[SubOrderTableFormat] = {
    subOrderTableQuery.filter(_.subOrderId===subOrderId).result.head
  }

  override def updateSubOrderStatus(status: String, subOrderId: SubOrderId): DBIO[String] = {
    for{
      _ <- subOrderTableQuery.filter(_.subOrderId===subOrderId).map(_.status).update(status)
    } yield {
      "SubOrder has been approved by seller"
    }
  }
}
