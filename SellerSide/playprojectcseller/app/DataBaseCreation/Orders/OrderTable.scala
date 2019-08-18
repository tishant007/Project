package DataBaseCreation.Orders

import java.sql.Timestamp

import DataBaseCreation.DataBase.{OrderAndSubOrder, OrderTableFormat}
import DataBaseCreation.OrderAndSubOrder.OrderAndSubOrderMethod
import DataBaseCreation.SubOrders.SubOrderMethod
import SellerSide.{OrderId, OrderItemId, Orders, UserAddressId, UserId}
import com.google.inject.{Inject, Singleton}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import slick.driver.PostgresDriver.api._


@Singleton
class OrderTable (tag: Tag) extends Table[OrderTableFormat](tag, "OrderTable"){
  implicit val dateTimeFormat = MappedColumnType.base[DateTime, Timestamp](fromDateTime, fromTimeStamp)

  private def fromDateTime(dateTime: DateTime) = {
    new Timestamp(dateTime.getMillis)
  }

  private def fromTimeStamp(timestamp: Timestamp) = {
    new DateTime(timestamp.getTime)
  }

  def orderId = column[OrderId]("OrderId")

  def userId = column[UserId]("UserId")

  def userAddressId = column[Option[UserAddressId]]("UserAddressId")

  def status = column[String]("Status")

  def paymentInstructions = column[String]("PaymentInstructions")

  def orderedAt = column[DateTime]("OrderedAt")

  def * = (orderId, userId, userAddressId, status, paymentInstructions, orderedAt) <> ((OrderTableFormat.apply _).tupled, OrderTableFormat.unapply)
}
