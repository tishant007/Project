package DataBaseCreation

import SellerSide.{OrderId, OrderItemId, ProductId, SellerId, SubOrderId, UserAddressId, UserId}
import org.joda.time.DateTime
import play.api.libs.json.{Format, JsResult, JsValue, Json}
import slick.driver.PostgresDriver.api._

trait DataBase
object DataBase {
  case class OrderTableFormat(orderId: OrderId, userId: UserId, userAddressId: Option[UserAddressId], status: String,
                              paymentInstructions: String, orderedAt: DateTime)

  case class SubOrderTableFormat(subOrderId: SubOrderId, sellerId: SellerId, status: String)

  case class OrderAndSubOrder(orderId: OrderId, subOrderId: SubOrderId)

  case class SubOrderAndOrderItem(subOrderId: SubOrderId, orderItemId: OrderItemId)

  case class Product(productId: ProductId, sellerId: SellerId, approvalStatus : String, pricePerUnit: Int, quantity : Int)
  object Product {
    implicit val productFormat = Json.format[Product]
  }

  case class EmailId(id : String)
  object EmailId {
    implicit val emailIdFormat = new Format[EmailId] {
      override def reads(json: JsValue): JsResult[EmailId] = {
        json.validate[String].map(EmailId(_))
      }

      override def writes(o: EmailId): JsValue = {
        Json.toJson(o.id)
      }
    }
    implicit val columnFormat = MappedColumnType.base[EmailId, String](fromEmailId, fromString)
    private def fromEmailId(emailId: EmailId) = {
      emailId.id
    }
    private def fromString(string: String) = {
      EmailId(string)
    }
  }
  case class Seller(sellerId: SellerId, sellerName : String, emailId : EmailId)
}
