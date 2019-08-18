package DataBaseCreation.OrderAndSubOrder

import DataBaseCreation.DataBase.OrderAndSubOrder
import DataBaseCreation.SubOrderAndOrderItem.SubOrderAndOrderItemMethod
import DataBaseCreation.SubOrders.SubOrderMethod
import SellerSide.{OrderId, SubOrderId, SubOrders}
import com.google.inject.{Inject, Singleton}
import slick.driver.PostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class OrderAndSubOrderImpl @Inject()(subOrderMethod: SubOrderMethod,
                                     subOrderAndOrderItemMethod: SubOrderAndOrderItemMethod)extends OrderAndSubOrderMethod {
  private val orderAndSubOrderQuery = TableQuery[OrderAndSubOrderTable]

  private def createTable = {
    orderAndSubOrderQuery.schema.create
  }

  override def addOrderWithSubOrder(orderAndSubOrder: OrderAndSubOrder): DBIO[String] = {
    for{
      _ <- orderAndSubOrderQuery += orderAndSubOrder
    } yield {
      "Order with SubOrder has been added successfully"
    }
  }

  override def getAllSubOrdersByOrderId(orderId: OrderId): DBIO[List[SubOrders]] = {
    (for {
      orderWithSuborderList <- orderAndSubOrderQuery.filter(_.orderId === orderId).result
      listOfSubOrders <- getListOfSubOrder(orderWithSuborderList)
    } yield {
      listOfSubOrders
    }).transactionally
  }

  private def getListOfSubOrder(orderWithSubOrderList: Seq[OrderAndSubOrder]): DBIO[List[SubOrders]] = {
    DBIO.sequence(orderWithSubOrderList.map { orderAndSubOrder =>
      (for {
        subOrderTable <- subOrderMethod.getSubOrderBySubOrderId(orderAndSubOrder.subOrderId)
        list <- subOrderAndOrderItemMethod.getAllOrderItemBySubOrderId(subOrderTable.subOrderId)
      } yield {
        SubOrders(subOrderTable.subOrderId, subOrderTable.sellerId, subOrderTable.status, list)
      }).transactionally
    }.toList)
  }

  override def getAllOrderBySubOrderId(subOrderId: SubOrderId): DBIO[List[OrderId]] = {
    orderAndSubOrderQuery.filter(_.subOrderId===subOrderId).result.map(_.map(_.orderId).toList.distinct)
  }
}
