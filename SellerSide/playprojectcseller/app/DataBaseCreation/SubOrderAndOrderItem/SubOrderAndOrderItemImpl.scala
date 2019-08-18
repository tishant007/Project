package DataBaseCreation.SubOrderAndOrderItem

import DataBaseCreation.DataBase.SubOrderAndOrderItem
import DataBaseCreation.OrderItem.OrderItemMethod
import SellerSide.{OrderItemId, OrderItems, SubOrderId}
import com.google.inject.{Inject, Singleton}

import scala.concurrent.ExecutionContext.Implicits.global
import slick.driver.PostgresDriver.api._

@Singleton
class SubOrderAndOrderItemImpl @Inject()(orderItemMethod : OrderItemMethod) extends SubOrderAndOrderItemMethod {
  private val subOrderAndOrderItemTableQuery = TableQuery[SubOrderAndOrderItemTable]

  private def createTable = {
    subOrderAndOrderItemTableQuery.schema.create
  }

  override def addSubOrderWithOrderItem(subOrderAndOrderItem: SubOrderAndOrderItem): DBIO[String] = {
    (for{
      _ <- subOrderAndOrderItemTableQuery += subOrderAndOrderItem
    } yield {
      "SubOrder with OrderItem has been successfully added"
    }).transactionally
  }

  override def getAllOrderItemBySubOrderId(subOrderId: SubOrderId): DBIO[List[OrderItems]] = {
    (for{
      subOrderAndOrderItemList <- subOrderAndOrderItemTableQuery.filter(_.subOrderId===subOrderId).result
      listOfOrderItems <- getListOfOrderItems(subOrderAndOrderItemList)
    } yield {
      listOfOrderItems
    }).transactionally
  }
  private def getListOfOrderItems(subOrderAndOrderItemList: Seq[SubOrderAndOrderItem]) : DBIO[List[OrderItems]] = {
    DBIO.sequence(subOrderAndOrderItemList.map { subOrderAndOrderItem =>
      orderItemMethod.getOrderItemByOrderItemId(subOrderAndOrderItem.orderItemId)
    }.toList
    )
  }
  override def getAllSubOrdersByOrderItemId(orderItemId: OrderItemId): DBIO[List[SubOrderId]] = {
    subOrderAndOrderItemTableQuery.filter(_.orderItemId === orderItemId).result.map(_.map(_.subOrderId).toList)
  }
}
