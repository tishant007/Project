package SellerSide

import com.google.inject.ImplementedBy
import org.joda.time.DateTime
import play.api.libs.json.{Json, OFormat}
import DataBaseCreation.DataBase.{EmailId, Product, Seller}

import scala.concurrent.Future
import slick.driver.PostgresDriver.api._

case class Orders(orderId: OrderId, userId: UserId,
                  userAddressId: Option[UserAddressId], status: String, paymentInstrument: String,
                  orderedAt: DateTime, listOfSuborders : List[SubOrders])
object Orders {
  implicit val orderFormat: OFormat[Orders] = Json.format[Orders]
}

case class SubOrders(subOrderId: SubOrderId, sellerId: SellerId, status: String, listOfOrderItems : List[OrderItems])
object SubOrders {
  implicit val subOrderFormat: OFormat[SubOrders] = Json.format[SubOrders]
}

case class OrderItems(orderItemId: OrderItemId, productId: ProductId,
                      productName: String, quantity: Int, price: BigDecimal)
object OrderItems {
  implicit val orderItemFormat: OFormat[OrderItems] = Json.format[OrderItems]
}

@ImplementedBy(classOf[SellerSideImpl])
trait SellerSideFunctions {
  def saveOrder(orders: Orders) : Future[String]
  def getOrderByUserId(orderId : OrderId, userId: UserId) : Future[List[Orders]]
  def getListOfOrders(list : List[OrderId]) : Future[List[(OrderId,Boolean)]]
  def getAllOrdersByUserIdForLast15Days(userId: UserId) : Future[List[Orders]]
  def getAllOrdersByOrderItemId(orderItemId: OrderItemId) : Future[List[Orders]]
  def getAllProducts : Future[List[Product]]
  def updateProducts(list : List[Product]) : Future[String]
  def signUpSeller(seller : Seller) : Future[String]
  def loginSeller(sellerId: SellerId, emailId: EmailId) : Future[String]
  def adminApprovesProduct(productId: ProductId, status : String = "Uploaded") : Future[String]
  def sellerSubOrderApproval(subOrderId: SubOrderId, status : String = "Accepted") : Future[String]
  def sellerOnBoardProduct(product: Product) : Future[String]
  def exec[T](action : DBIO[T]) : Future[T]
}
