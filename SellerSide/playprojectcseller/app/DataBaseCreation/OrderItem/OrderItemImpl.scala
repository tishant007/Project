package DataBaseCreation.OrderItem

import SellerSide.{OrderItemId, OrderItems}
import com.google.inject.Singleton
import slick.driver.PostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class OrderItemImpl extends OrderItemMethod {
  private val orderItemTableQuery = TableQuery[OrderItemTable]
  private def createTable = {
    orderItemTableQuery.schema.create
  }
  override def addOrderItem(orderItems: OrderItems): DBIO[String] = {
    (for{
      _ <- orderItemTableQuery += orderItems
    } yield {
      "OrderItem has been successfully added"
    }).transactionally
  }

  override def getOrderItemByOrderItemId(orderItemId: OrderItemId): DBIO[OrderItems] = {
    orderItemTableQuery.filter(_.orderItemId===orderItemId).result.head
  }
}
