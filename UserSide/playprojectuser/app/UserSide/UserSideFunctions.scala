package UserSide

import DataBaseCreation.DataBase.{EmailId, User}
import com.google.inject.ImplementedBy
import org.joda.time.DateTime
import play.api.libs.json.Json

import scala.concurrent.Future
import slick.driver.PostgresDriver.api._

case class Orders(orderId: OrderId, userId: UserId,
                  userAddressId: Option[UserAddressId], status: String, paymentInstrument: String,
                  orderedAt: DateTime, listOfSuborders : List[SubOrders])
object Orders {
  implicit val orderFormat = Json.format[Orders]
}

case class SubOrders(subOrderId: SubOrderId, sellerId: SellerId, status: String, listOfOrderItems : List[OrderItems])
object SubOrders {
  implicit val subOrderFormat = Json.format[SubOrders]
}

case class OrderItems(orderItemId: OrderItemId, productId: ProductId,
                      productName: String, quantity: Int, price: BigDecimal)
object OrderItems {
  implicit val orderItemFormat = Json.format[OrderItems]
}
case class Product(productId: ProductId, sellerId: SellerId, approvalStatus : String, pricePerUnit: Int, quantity : Int)
object Product {
  implicit val productFormat = Json.format[Product]
}
@ImplementedBy(classOf[UserSideImpl])
trait UserSideFunctions {
  def saveOrder(orders: Orders) : Future[String]
  def userViewProduct : Future[List[Product]]
  def orderCartOfProducts : Future[List[Orders]]
  def viewHistoryOfOrders : Future[List[Orders]]
  def userSignUp(user : User) : Future[String]
  def userLogin(userId : UserId, emailId: EmailId) : Future[User]
  def exec[T](action : DBIO[T]) : Future[T]
}
