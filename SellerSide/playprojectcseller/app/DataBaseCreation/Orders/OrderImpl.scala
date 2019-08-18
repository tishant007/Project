package DataBaseCreation.Orders

import java.sql.Timestamp

import DataBaseCreation.DataBase.{OrderAndSubOrder, OrderTableFormat}
import DataBaseCreation.OrderAndSubOrder.OrderAndSubOrderMethod
import DataBaseCreation.SubOrders.SubOrderMethod
import SellerSide.{OrderId, OrderItemId, Orders, UserId}
import com.google.inject.{Inject, Singleton}
import org.joda.time.DateTime
import slick.driver.PostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class OrderImpl @Inject()(subOrderMethod: SubOrderMethod,
                          orderAndSubOrderMethod: OrderAndSubOrderMethod) extends OrderMethod {
  implicit val dateTimeFormat = MappedColumnType.base[DateTime, Timestamp](fromDateTime, fromTimeStamp)

  private def fromDateTime(dateTime: DateTime) = {
    new Timestamp(dateTime.getMillis)
  }

  private def fromTimeStamp(timestamp: Timestamp) = {
    new DateTime(timestamp.getTime)
  }

  private val orderTableQuery = TableQuery[OrderTable]

  private def createTable = {
    orderTableQuery.schema.create
  }

  override def addOrder(orders: Orders): DBIO[String] = {
    (for {
      _ <- orderTableQuery += OrderTableFormat(orders.orderId, orders.userId, orders.userAddressId,
        orders.status, orders.paymentInstrument, orders.orderedAt)
    } yield {
      orders.listOfSuborders.map { subOrders =>
        subOrderMethod.addSubOrder(subOrders)
        orderAndSubOrderMethod.addOrderWithSubOrder(OrderAndSubOrder(orders.orderId, subOrders.subOrderId))
      }
      "Orders have been successfully added"
    }).transactionally
  }

  override def getAllOrdersByUserId(userId: UserId): DBIO[List[Orders]] = {
    (for {
      listOfTableOrders <- orderTableQuery.filter(_.userId === userId).result
      list <- getListOfOrders(listOfTableOrders)
    } yield {
      list
    }).transactionally
  }

  override def getOrderByOrderId(orderId: OrderId): DBIO[Orders] = {
    (for {
      listOfTableOrders <- orderTableQuery.filter(_.orderId === orderId).result
      list <- getListOfOrders(listOfTableOrders)
    } yield {
      list.head
    }).transactionally
  }

  override def getAllOrdersByDate(dateTime: DateTime, userId: UserId): DBIO[List[Orders]] = {
    val ans = orderTableQuery.filter(orders => orders.userId===userId).result
    (for {
      listOfTableOrders <- orderTableQuery.filter { orders =>
          orders.userId===userId &&
        orders.orderedAt >= dateTime.minus(15) &&
        orders.orderedAt <= dateTime
      }.result
      list <- getListOfOrders(listOfTableOrders)
    } yield {
      list
    }).transactionally
  }

  private def getListOfOrders(listOfTableOrders: Seq[OrderTableFormat]): DBIO[List[Orders]] = {
    DBIO.sequence(listOfTableOrders.map { tableOrders =>
      orderAndSubOrderMethod.getAllSubOrdersByOrderId(tableOrders.orderId).map { list =>
        Orders(tableOrders.orderId, tableOrders.userId, tableOrders.userAddressId,
          tableOrders.status, tableOrders.paymentInstructions, tableOrders.orderedAt, list)
      }
    }.toList)
  }

  override def getAllOrdersByOrderItemId(orderItemId: OrderItemId): DBIO[List[Orders]] = ???

  override def checkOrder(orderId: OrderId): DBIO[Boolean] = {
    (for{
      orders <- orderTableQuery.filter(_.orderId===orderId).result.headOption
    } yield{ orders match {
      case Some(_) => true
      case _ => false
    }
    }).transactionally
  }
}
